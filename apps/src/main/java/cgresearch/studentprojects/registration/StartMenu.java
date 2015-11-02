package cgresearch.studentprojects.registration;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cgresearch.graphics.fileio.AsciiPointFormat;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;

public class StartMenu extends JFrame implements ActionListener{

	private JTextField iteration;
	private JTextField firstPointCloud;
	private JTextField secondPointCloud;
	private JFileChooser chooser;
	
	public StartMenu(){
		
		super("StartMenu");
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(4,2));
		jp.setPreferredSize(new Dimension(300,130));
		
		jp.add(new JLabel("Load first PointCloud"));
		
		JButton openCloud1 = new JButton("Open");
		openCloud1.addActionListener(this);
		openCloud1.setActionCommand("open1");
		jp.add(openCloud1);
//		chooser = new JFileChooser("Open");
//		chooser.showOpenDialog(null);
//		jp.add(chooser);
		jp.add(new JLabel("Load second PointCloud"));
		
		JButton openCloud2 = new JButton("Open");
		openCloud2.addActionListener(this);
		openCloud2.setActionCommand("open2");
		jp.add(openCloud2);
		
		jp.add(new JLabel("Iteration"));
		iteration = new JTextField("200");
		jp.add(iteration);
		
		JButton start = new JButton("Start");
		start.addActionListener(this);
		start.setActionCommand("start");
		jp.add(start);
		
		
		
		getContentPane().add(jp);
		pack();
		
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("open1")){
			OpenFile open = new OpenFile();
			open.Open();
			AsciiPointFormat format = new AsciiPointFormat().setPosition(0, 1, 2).setNormal(3, 4, 5).setSeparator("\\s+");
			
			
		}else if(e.getActionCommand().equals("open2")){
			
		}else if(e.getActionCommand().equals("start")){
			
		}
		
	}

}
