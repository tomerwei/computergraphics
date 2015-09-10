package homeautomation.webservice.resources;

public abstract class PageFactoryAbstract {
  /**
   * Return the html code for the page top.
   * 
   * @param title
   *          Title of the created HTML page.
   * 
   * @return HTML code.
   */
  protected String getHtmlPageTop(String title) {
    return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
        + "<html><head>"
        + "<title>"
        + title
        + "</title>"
        + getCssDef()
        + "<meta http-equiv=\"Content-Type\" content=\"text/html; "
        + "charset=iso-8859-1\"></head><body>";
  }

  /**
   * Create CSS definition.
   * 
   * @return CSS code.
   */
  protected String getCssDef() {
    return "<style type=\"text/css\">\n"
        + "p.text{ font-style:normal; font-weight:normal; font-size: 10pt; color:black; "
        + "font-family:Helvetica,Arial,sans-serif; text-align: center;}\n"
        + "p.text-bold{ font-style:normal; font-weight:bold; font-size: 10pt; color:black; "
        + "font-family:Helvetica,Arial,sans-serif; text-align: center;}\n"
        + "p.text-italics-center{font-style:italic; "
        + "font-weight:normal; font-size: 10pt; color:black; "
        + "font-family:Helvetica,Arial,sans-serif; text-align: center;}"
        + "p.text-italics-left{font-style:italic; "
        + "font-weight:normal; font-size: 10pt; color:black; "
        + "font-family:Helvetica,Arial,sans-serif; text-align: left;}"
        + "p.text-bold-italics-left {font-style:italic; "
        + "font-weight:bold; font-size: 10pt; color:black; "
        + "font-family:Helvetica,Arial,sans-serif; text-align: left;}"
        + "img.frame { border:1px solid black; align:center }\n" + "</style>\n";

  }

  /**
   * Return the HTML code for the page bottom.
   * 
   * @return HTML code for the bottom of the page.
   */
  protected String getHtmlPageBottom() {
    return "</body>\n</html>\n";
  }

  /**
   * Getter.
   * 
   * @param numberOfColumns
   *          Number of columns in the central table.
   * @param tableWidth
   *          Width of the central table.
   * @return HTML code for the banner.
   */
  protected String getHtmlCodeHorizontalLineTable(int numberOfColumns,
      int tableWidth) {
    return "<tr><td colspan='" + numberOfColumns
        + "'><img src='images/horizontal_line.png' width='" + tableWidth
        + "' height='3' alt='Line'></td></tr>\n";
  }

  /**
   * Getter.
   * 
   * @param width
   *          Width of the central table.
   * @return HTML code for the horizontal line.
   */
  protected String getHtmlCodeHorizontalLine(int width) {
    return "<img src='images/horizontal_line.png' width='" + width
        + "' height='3' alt='Line'><br>\n";
  }

  /**
   * Getter.
   * 
   * @param width
   *          Width of the central table.
   * @return HTML code for the banner.
   */
  protected String getHtmlCodeBanner(int width) {
    return "<img class='frame' src='images/banner.jpg' width='" + width
        + " alt='Banner'><br>\n";
  }

  /**
   * Getter.
   * 
   * @param width
   *          Width of the central table.
   * @return HTML code for the navigation bar.
   */
  protected String getNavigationBar(int width) {
    String htmlCode = "";
    htmlCode +=
        "<center><table cellpadding='5' cellspacing='5' border='0' width='650'>"
            + "<tr>"
            + "<td><p class='text'><a href='/raspiserver/sensors.html'>Sensoren</a></p></td>"
            + "<td><p class='text'><a href='/raspiserver/actors.html'>Aktoren</a></p></td>"
            + "</tr>" + "</table></center>";
    htmlCode += getHtmlCodeHorizontalLine(width);
    return htmlCode;
  }

  /**
   * Get the content of the page in HTML.
   * 
   * @return HTML code for the whole page.
   */
  public abstract String getHtmlCode();
}
