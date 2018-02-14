package bukkaa.mediahouse.ShakerGameWS.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static bukkaa.mediahouse.ShakerGameWS.model.Constants.CONFIG_FILE_PATH;

public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private Properties config;

    public Config() {
        config = new Properties();

        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            config.load(input);
        } catch (IOException e) {
            log.error("Error in loading config file", e);
        }

        log.info("Configuration loaded: {}", config);
    }

    public String getProperty(String key) {
        String value = config.getProperty(key);
        log.debug("Got property <{}> = {}", key, value);
        return value;
    }

    public void setProperty(String key, String value) {
        log.debug("Setting property <{}> = {}", key, value);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            config.setProperty(key, value);

            log.debug("Storing the changes...");
            config.store(output, null);
            log.debug("Changes saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return config.toString();
    }

    static {
        File f = new File(CONFIG_FILE_PATH);
        CONFIG_FILE_PATH = f.getAbsolutePath();
    }
}