package settings;

import enums.DeviceType;
import enums.OSType;
import enums.PlatformType;
import utils.OS;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.System.getProperty;

/**
 * Mobile settings.
 */
public class Settings {

    private final String projectRoot = getProperty("user.dir");
    private final String testResultsRoot = projectRoot + File.separator + "build" + File.separator + "test-results";

    private Properties properties;

    public OSType hostOS;
    public PlatformType platform;
    public Double platformVersion;
    public DeviceType deviceType;
    public String deviceName;
    public String deviceId;

    public AppSettings app;
    public WebSettings web;
    public AndroidSettings android;

    public final boolean debug = OS.isDebuggerAttached();

    public final String logsPath = testResultsRoot + File.separator + "log";
    public final String screenshotsPath = testResultsRoot + File.separator + "screenshots";

    /**
     * Init settings.
     *
     * @throws Exception when fail to determine some settings.
     */
    public Settings() throws Exception {

        System.out.println("Init settings...");
        String config = getProperty("config");
        if (config != null) {
            this.properties = this.readProperties(config);
        } else {
            throw new Exception("Config file not specified.");
        }

        this.hostOS = OS.getOSType();
        this.platform = this.getPlatformType();
        this.platformVersion = Double.parseDouble(this.properties.getProperty("platformVersion", null));
        this.deviceName = this.properties.getProperty("deviceName", "Unknown Device");
        this.deviceId = this.properties.getProperty("deviceId", null);
        this.deviceType = this.getDeviceType();

        this.app = new AppSettings(properties);
        this.web = new WebSettings(properties);
        this.android = new AndroidSettings(properties);

        System.out.println("[Host] Host OS: " + this.hostOS);
        System.out.println("[Mobile Device] Mobile OS: " + this.platform);
        System.out.println("[Mobile Device] Mobile OS Version: " + this.platformVersion);
        System.out.println("[Mobile Device] Mobile Device Name: " + this.deviceName);
        System.out.println("[Mobile Device] Mobile Device Type: " + this.deviceType);
        System.out.println("[Mobile Device] Mobile Device Id: " + this.deviceId);
        System.out.println("[Android] AVD Image: " + this.android.avdName);
        System.out.println("[Android] AVD Start Options: " + this.android.avdOptions);
        System.out.println("[App] Test App Name: " + this.app.appName);
        System.out.println("[App] Test App Id: " + this.app.appId);
        System.out.println("[App] Test App Path: " + this.app.appPath);
        System.out.println("[Web] Browser: " + this.web.browser);
        System.out.println("[Web] Base URL: " + this.web.baseURL);
        System.out.println("[Logs] Log files location: " + this.logsPath);
        System.out.println("[Logs] Screenshots location: " + this.screenshotsPath);
        System.out.println("[Other] Debug mode: " + this.debug);
    }

    private Properties readProperties(String configFile) throws Exception {
        String path = "config/" + configFile + ".properties";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            throw new Exception("Failed to read " + path);
        }
    }

    private DeviceType getDeviceType() throws Exception {
        String deviceType = this.properties.getProperty("deviceType", "emulator").toLowerCase();
        if (deviceType.contains("android")) {
            return DeviceType.ANDROID_DEVICE;
        } else if (deviceType.contains("ios")) {
            return DeviceType.IOS_DEVICE;
        } else if (deviceType.contains("emu")) {
            return DeviceType.EMULATOR;
        } else if (deviceType.contains("sim")) {
            return DeviceType.SIMULATOR;
        } else {
            throw new Exception("Unknown DeviceType.");
        }
    }

    private PlatformType getPlatformType() throws Exception {
        String platformType = this.properties.getProperty("platform", "android").toLowerCase();
        if (platformType.contains("android")) {
            return PlatformType.ANDROID;
        } else if (platformType.contains("ios")) {
            return PlatformType.IOS;
        } else {
            throw new Exception("Unknown PlatformType.");
        }
    }
}
