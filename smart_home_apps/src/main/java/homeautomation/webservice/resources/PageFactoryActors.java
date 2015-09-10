package homeautomation.webservice.resources;

import homeautomation.actors.ElecticalOutletSwitch;
import homeautomation.actors.IActor;
import homeautomation.actors.IActor.Commands;
import homeautomation.apps.RasPiServer;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;

/**
 * Factory for an HTML page with the actors.
 * 
 * @author Philipp Jenke
 *
 */
public class PageFactoryActors extends PageFactoryAbstract {

  @Override
  public String getHtmlCode() {
    int width = 650;
    String htmlCode = getHtmlPageTop("Aktoren");
    htmlCode += "<center>";
    htmlCode += getNavigationBar(width);
    htmlCode += getActorsHtmlCode(width);
    htmlCode += getHtmlCodeHorizontalLine(width);
    htmlCode += getHtmlCodeBanner(width);
    htmlCode += "</center>";
    htmlCode += getHtmlPageBottom();
    return htmlCode;
  }

  /**
   * Create HTML code for all actors.
   * 
   * @param width
   *          Width of the central table.
   * @return HTML code for the actors list.
   */
  private String getActorsHtmlCode(int width) {
    RasPiServer raspiServer =
        RaspiServerAccessSingleton.getInstance().getRaspiServer();
    if (raspiServer == null) {
      return "";
    }

    String htmlCode =
        "<table cellpadding='0' cellspacing='0' border='0' width='" + width
            + "'>";
    for (int i = 0; i < raspiServer.getNumberOfActors(); i++) {
      htmlCode += getActorRow(width, raspiServer.getActor(i), i);
    }
    htmlCode += "</table>";
    return htmlCode;
  }

  /**
   * Create HTML code for a single actor.
   * 
   * @param width
   *          Width of the central table.
   * @param actor
   *          Actor for which code is created.
   * @param index
   *          Index of the actor in the actor-list.
   * @return HTML code for the actor row.
   */
  private String getActorRow(int width, IActor actor, int index) {

    String htmlCode = "";
    if (actor instanceof ElecticalOutletSwitch) {

      htmlCode +=
          "<tr>\n" + "<td><p class='text'>" + actor.toString() + "</p></td>\n"
              + "<td>\n" + getHtmlCodeOnOff(actor, index, Commands.ON)
              + "</td>\n" + "<td>"
              + getHtmlCodeOnOff(actor, index, Commands.OFF) + "</td>\n"
              + "<td><p class='text'>" + actor.getLocation() + "</p></td>\n"
              + "</tr>\n";
    } else {
      htmlCode +=
          "<tr>" + "<td><p class='text'>" + actor.toString() + "</p></td>"
              + "<td></td>" + "<td></td>" + "<td><p class='text'>"
              + actor.getLocation() + "</p></td>" + "</tr>";
    }
    return htmlCode;
  }

  /**
   * Getter.
   * 
   * @param actor
   *          Actor for which the code is created.
   * @param index
   *          Index of the actor in the actor list.
   * @param command
   *          Command for which the code is created for.
   * @return HTML code for the on/off command
   */
  private String getHtmlCodeOnOff(IActor actor, int index, Commands command) {
    String formId = "actor_" + index + "_" + command;
    String imageFilename =
        (command == Commands.ON) ? "images/on.png" : "images/off.png";
    return "<form  enctype='application/x-www-form-urlencoded' id='" + formId
        + "' method='post' action='actors/index=" + index + ",command="
        + command.toString() + "'>" + "<input type='image' name='GigID' src='"
        + imageFilename + "' height='30'>"
        // + "<a href='#' onclick='javascript:document.forms['" + formId //
        // /*type='hidden'
        // + "'].submit()'>"
        // + "</a>"
        + "</form>\n";
  }
}
