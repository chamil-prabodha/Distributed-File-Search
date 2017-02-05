//package Util;
//
//
//import app.Daemon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by Chamil Prabodha on 04/02/2017.
 */
public class Configurations {
    public static String NODEIP;
    public static int NODEPORT;
    public static String USERNAME;
    public static String BOOTSTRAPIP;
    public static int BOOTSTRAPPORT;
    public static String FILEREPO;
    public static int MAXHOPS;
    public static int TTL;

    static{

        try {
            String path = Daemon.class.getProtectionDomain().getCodeSource().getLocation().toURI().getSchemeSpecificPart()+"config.properties";

            FileInputStream inputStream = new FileInputStream(path);

            Properties properties = new Properties();
            properties.load(inputStream);

            NODEIP = properties.getProperty("NODEIP");
            NODEPORT = Integer.parseInt(properties.getProperty("NODEPORT"));
            USERNAME = properties.getProperty("USERNAME");
            BOOTSTRAPIP = properties.getProperty("BOOTSTRAPIP");
            BOOTSTRAPPORT = Integer.parseInt(properties.getProperty("BOOTSTRAPPORT"));
            FILEREPO = properties.getProperty("FILEREPO");
            MAXHOPS = Integer.parseInt(properties.getProperty("MAXHOPS"));
            TTL = Integer.parseInt(properties.getProperty("TTL"));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
