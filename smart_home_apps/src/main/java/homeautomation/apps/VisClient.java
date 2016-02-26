package homeautomation.apps;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import homeautomation.database.DatabaseAccess;
import homeautomation.database.Measurement;
import homeautomation.foundation.Logger;
import homeautomation.sensors.SensorInformation;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Visualize sensor measurements.
 * 
 * @author Philipp Jenke
 *
 */
public class VisClient extends JFrame implements ActionListener,
    IHomeautomationApp {

  private static final long serialVersionUID = -6328622630078365297L;

  /**
   * Database access.
   */
  private final DatabaseAccess databaseAccess;

  /**
   * GUI components.
   */
  private JComboBox<String> comboType = null;
  // private JComboBox<String> comboLocation = null;
  // private JComboBox<String> comboSensorId = null;
  private JButton buttonSave = null;
  private JButton buttonClear = null;
  private ChartPanel chartPanel;
  private JTextField textFieldStartTimestamp = null;
  private JTextField textFieldEndTimestamp = null;

  // private final String ACTION_COMMAND_COMBO_TYPE =
  // "ACTION_COMMAND_COMBO_TYPE";
  // private final String ACTION_COMMAND_COMBO_LOCATION =
  // "ACTION_COMMAND_COMBO_LOCATION";
  // private final String ACTION_COMMAND_COMBO_SENSOR_ID =
  // "ACTION_COMMAND_COMBO_SENSOR_ID";
  private static final String ACTION_COMMAND_BUTTON_SAVE =
      "ACTION_COMMAND_BUTTON_SAVE";
  private static final String ACTION_COMMAND_BUTTON_CLEAR =
      "ACTION_COMMAND_BUTTON_CLEAR";

  /**
   * List of options to be shown.
   */
  private List<SensorInformation> graphOptions =
      new ArrayList<SensorInformation>();

  private List<SensorInformation> allSensors =
      new ArrayList<SensorInformation>();

  /**
   * Constructor.
   * 
   * @param title
   *          Frame title.
   * @param databaseAccess
   *          Interface to the database.
   */
  public VisClient(String title, DatabaseAccess databaseAccess) {
    super(title);
    this.databaseAccess = databaseAccess;

    // Setup components
    createGui();

    // Create initial chart
    setChartInUi();

    // Cleanup GUI
    pack();
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Activate interaction
    // comboLocation.addActionListener(this);
    comboType.addActionListener(this);
    // /comboSensorId.addActionListener(this);
  }

  /**
   * Create and setup the GUI components.
   */
  private void createGui() {
    JPanel panelMain = new JPanel();
    setContentPane(panelMain);
    panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

    // Type, location, sensor_id
    JPanel panelSettingsLine1 = new JPanel();
    panelSettingsLine1.setLayout(new BoxLayout(panelSettingsLine1,
        BoxLayout.X_AXIS));
    panelMain.add(panelSettingsLine1);
    comboType = new JComboBox<String>();
    // comboType.setActionCommand(ACTION_COMMAND_COMBO_TYPE);
    panelSettingsLine1.add(comboType);
    // comboLocation = new JComboBox<String>();
    // comboLocation.setActionCommand(ACTION_COMMAND_COMBO_LOCATION);
    // panelSettingsLine1.add(comboLocation);
    // comboSensorId = new JComboBox<String>();
    // comboSensorId.setActionCommand(ACTION_COMMAND_COMBO_SENSOR_ID);
    // panelSettingsLine1.add(comboSensorId);

    // Start timestamp, end timestamp
    JPanel panelSettingsLine2 = new JPanel();
    panelSettingsLine2.setLayout(new BoxLayout(panelSettingsLine2,
        BoxLayout.X_AXIS));
    panelMain.add(panelSettingsLine2);
    textFieldStartTimestamp = new JTextField();
    textFieldEndTimestamp = new JTextField();
    panelSettingsLine2.add(createTimestampEdit(textFieldStartTimestamp,
        new Date(Calendar.getInstance().getTime().getTime() - 1000 * 60 * 60
            * 24)));
    panelSettingsLine2.add(createTimestampEdit(textFieldEndTimestamp, Calendar
        .getInstance().getTime()));

    // Save/Show, Clear
    JPanel panelSettingsLine3 = new JPanel();
    panelSettingsLine3.setLayout(new BoxLayout(panelSettingsLine3,
        BoxLayout.X_AXIS));
    panelMain.add(panelSettingsLine3);
    buttonSave = new JButton("Save/Show");
    panelSettingsLine3.add(buttonSave);
    buttonSave.addActionListener(this);
    buttonSave.setActionCommand(ACTION_COMMAND_BUTTON_SAVE);
    buttonClear = new JButton("Clear");
    panelSettingsLine3.add(buttonClear);
    buttonClear.addActionListener(this);
    buttonClear.setActionCommand(ACTION_COMMAND_BUTTON_CLEAR);

    databaseAccess.connect();
    setComboOptions(comboType);
    // setComboOptions(comboLocation,
    // DatabaseConstants.COLUMN_NAME_LOCATION);
    // setComboOptions(comboSensorId,
    // DatabaseConstants.COLUMN_NAME_SENSOR_ID);
    databaseAccess.disconnect();

    chartPanel = new ChartPanel(null);
    panelMain.add(chartPanel);
  }

  /**
   * Initialize edit field for the timestamp.
   * 
   * @param textField
   *          Swing text field.
   * @param date
   *          Date to be set
   * @return Created Swing component.
   */
  private Component createTimestampEdit(JTextField textField, Date date) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    DateFormat dfmt = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    textField.setText(dfmt.format(date));
    panel.add(textField);
    return panel;
  }

  /**
   * Fill the combobox with all available options from the database in the
   * specified field. It is required to connect to the database first!
   * 
   * @param combobox
   *          Combobox to be filled.
   */
  private void setComboOptions(JComboBox<String> combobox) {
    List<SensorInformation> sensors = databaseAccess.getSensors();
    allSensors = sensors;
    if (sensors == null) {
      return;
    }
    combobox.removeAllItems();
    for (SensorInformation sensor : sensors) {
      combobox.addItem(sensor.toString());
    }
    if (combobox.getItemCount() > 0) {
      combobox.setSelectedIndex(0);
    }
  }

  /**
   * Recreate the chart based on the currently selected settings.
   */
  private void setChartInUi() {
    JFreeChart chart = createChart();
    chartPanel.setChart(chart);
  }

  /**
   * Create a chart UI object.
   * 
   * @return JFreeChart-Object.
   */
  private JFreeChart createChart() {
    TimeSeriesCollection timeSerieCollection = new TimeSeriesCollection();
    Date startTimestamp = parseTimestamp(textFieldStartTimestamp);
    Date endTimestamp = parseTimestamp(textFieldEndTimestamp);
    if (graphOptions.size() > 0) {
      databaseAccess.connect();
      for (int i = 0; i < graphOptions.size(); i++) {
        TimeSeries timeSeriesDataset =
            createTimeSeries(graphOptions.get(i), startTimestamp, endTimestamp);
        if (timeSeriesDataset != null) {
          timeSerieCollection.addSeries(timeSeriesDataset);
        }
      }
      databaseAccess.disconnect();
    }
    JFreeChart chart =
        ChartFactory.createTimeSeriesChart("Sensor Measurements", "Timestamp",
            "Value", timeSerieCollection);
    return chart;
  }

  /**
   * Parse a text field for a timestamp.
   * 
   * @param textField
   *          Text field to be parsed.
   * @return Date object from the text description.
   */
  private Date parseTimestamp(JTextField textField) {
    try {
      DateFormat formatter = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
      Date date = formatter.parse(textField.getText());
      return date;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Create a time series instance (required for the chart).
   * 
   * @param sensorInformation
   *          Info object for the sensor.
   * @param startTimestamp
   *          Start timestamp.
   * @param endTimestamp
   *          End timestamp.
   * @return Time series instance.
   */
  private TimeSeries createTimeSeries(SensorInformation sensorInformation,
      Date startTimestamp, Date endTimestamp) {
    TimeSeries timeSeriesDataset = new TimeSeries(sensorInformation.toString());
    List<Measurement> databaseData =
        databaseAccess.getValuesForSensorByKey(
            sensorInformation.getDatabaseKey(), startTimestamp, endTimestamp);

    if (databaseData == null) {
      System.out.println("Dataset is null.");
      return null;
    }
    if (databaseData.size() == 0) {
      System.out.println("Dataset has no content.");
      return null;
    }
    Logger.getInstance().debug(
        "Succesfully grabbed " + databaseData.size()
            + " datasets from database.");
    for (int index = 0; index < databaseData.size(); index++) {
      timeSeriesDataset.add(new Second(databaseData.get(index).timestamp),
          databaseData.get(index).value);
    }
    return timeSeriesDataset;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand().equals(ACTION_COMMAND_BUTTON_SAVE)) {
      // Save current settings
      graphOptions.add(allSensors.get(comboType.getSelectedIndex()));
      setChartInUi();
    } else if (event.getActionCommand().equals(ACTION_COMMAND_BUTTON_CLEAR)) {
      // Clear all settings
      graphOptions.clear();
      setChartInUi();
    }
  }

  @Override
  public void init() {
  }

  @Override
  public void shutdown() {
  }

  @Override
  public String toString() {
    return "*** Visualization client ***\n" + databaseAccess;
  }

  @Override
  public void command(String line) {
  }
}
