package io.github.mlpre.util;

import io.github.mlpre.model.CustomDriver;
import io.github.mlpre.model.JdbcDriver;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;

public class PluginUtil {

    public static void loadJdbc(JdbcDriver jdbcDriver) {
        try {
            String filePath = System.getProperty("user.home") + File.separator + ".database-manage" + File.separator + jdbcDriver.getFileName();
            if (!new File(filePath).exists()) {
                DownloadUtil.download(jdbcDriver.getUrl(), filePath);
            }
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new File(filePath).toURI().toURL()});
            Driver driver = (Driver) Class.forName(jdbcDriver.getClassName(), true, urlClassLoader).getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new CustomDriver(driver));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
