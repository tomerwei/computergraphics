package smarthomevis.architecture.config;

public class Configuration {

    private static String resourcesLocation = "smart_home_visualization/resources.ini";
    private static String packageLocation = "cg.smart_home_visualization.src.main.java.smarthomevis.architecture";

    private static String databaseName = "smarthome";
    private static String testDatabaseName = "smarthome_test";
    private static String databaseServerIP = "localhost";
    private static int databasePort = 27017;

    private static String RESTroot = "/smarthome";
    private static int RESTport = 8183;

    private static String dateFormat = "dd/mm/yyyy h:mm:ss a";

    public static String getResourcesLocation() {
        return resourcesLocation;
    }

    public static String getPackageLocation() {
        return packageLocation;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static String getTestDatabaseName() {
        return testDatabaseName;
    }

    public static String getDatabaseServerIP() {
        return databaseServerIP;
    }

    public static int getDatabasePort() {
        return databasePort;
    }

    public static String getRESTroot() {
        return RESTroot;
    }

    public static int getRESTport() {
        return RESTport;
    }

    public static String getDateFormat() {
        return dateFormat;
    }
}
