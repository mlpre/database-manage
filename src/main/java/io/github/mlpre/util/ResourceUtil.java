package io.github.mlpre.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceUtil {

    public static URL getResource(String name) {
        return ResourceUtil.class.getClassLoader().getResource(name);
    }

    public static InputStream getInputStream(String name) {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(name);
    }

}
