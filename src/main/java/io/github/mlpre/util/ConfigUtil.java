package io.github.mlpre.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConfigUtil {

    public static final String DATABASE_CONFIG_PATH = System.getProperty("user.home") + File.separator + ".database-manage";

    public static final String DATABASE_CONFIG_FILE = DATABASE_CONFIG_PATH + File.separator + "database-manage.properties";

    public static Properties properties = new Properties();

    public static void initConfig() throws Exception {
        File file = new File(DATABASE_CONFIG_PATH);
        boolean isExist;
        if (!file.exists()) {
            isExist = file.mkdir();
        } else {
            isExist = true;
        }
        boolean configIsExist;
        if (isExist) {
            File config = new File(DATABASE_CONFIG_FILE);
            if (!config.exists()) {
                configIsExist = config.createNewFile();
            } else {
                configIsExist = true;
            }
        } else {
            throw new Exception("Create directory failure!");
        }
        if (!configIsExist) {
            throw new Exception("Create config failure!");
        }
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        properties.load(new FileReader(DATABASE_CONFIG_FILE));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                properties.store(new FileOutputStream(DATABASE_CONFIG_FILE), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

}
