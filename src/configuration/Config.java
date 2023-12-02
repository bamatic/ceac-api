package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String dbType = "mariadb";
    public static String serverName = "localhost";
    public static int serverPort = 3306;
    public static String dbName = "ceac_project";
    public static String dbUser = "root";
    public static String dbPassword = "";
    public static String useSSL = "false";
    public static String keyPath = "src/resources/ceac_private.der";
    public static String publicKeyPath = "src/resources/ceac_public.pem";
    public static int jwtExpiration = 60*60*1000;
    public static String loggingDirectory = "";

    public static void load() {
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("./resources/config.properties");
            properties.load(fis);
            Config.dbType = properties.getProperty("db.type");
            Config.serverName = properties.getProperty("db.server.name");
            Config.serverPort = Integer.parseInt(properties.getProperty("db.server.port"));
            Config.dbName = properties.getProperty("db.name");
            Config.dbUser = properties.getProperty("db.user");
            Config.dbPassword = properties.getProperty("db.password");
            Config.jwtExpiration = Integer.parseInt(properties.getProperty("jwt.expiration.hours")) * 3600 * 1000;
            Config.keyPath = properties.getProperty("jwt.private.key");
            Config.publicKeyPath = properties.getProperty("jwt.public.key");
        } catch (IOException ex) {
            System.out.println("configuration exception");
            throw new RuntimeException(ex);
        }
    }
    public static String url() {
        return "jdbc:" + Config.dbType + "://" + Config.serverName + ":" + Config.serverPort + "/" + Config.dbName
                + "?useSSL=" + Config.useSSL;
    }
}
