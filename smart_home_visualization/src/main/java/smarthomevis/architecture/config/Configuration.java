package smarthomevis.architecture.config;

import cgresearch.graphics.scenegraph.CgNode;

import java.text.SimpleDateFormat;

public class Configuration {

    private static final String resourcesLocation = "smart_home_visualization/resources.ini";
    private static final String packageLocation = "cg.smart_home_visualization.src.main.java.smarthomevis.architecture";

    private static final String databaseName = "smarthome";
    private static final String testDatabaseName = "smarthome_test";
    private static final String databaseServerIP = "localhost";
    private static final int databasePort = 27017;

    private static final String RESTroot = "/smarthome";
    private static final int RESTport = 8183;

    private static final String dateFormat = "dd-MM-yyyy HH:mm:ss:S";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

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

    public static SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
