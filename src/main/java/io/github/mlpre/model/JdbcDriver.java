package io.github.mlpre.model;

import java.util.Locale;

public class JdbcDriver {

    private final String groupId;

    private final String artifactId;

    private final String version;

    private final String className;

    public JdbcDriver(String groupId, String artifactId, String version, String className) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.className = className;
    }

    public String getUrl() {
        String publicUrl = null;
        if (Locale.CHINA.equals(Locale.getDefault())) {
            publicUrl = "https://maven.aliyun.com/repository/public/";
        } else {
            publicUrl = "https://repo1.maven.org/maven2/";
        }
        return publicUrl + groupId.replace(".", "/")
                + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }

    public String getFileName() {
        String url = getUrl();
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getClassName() {
        return className;
    }
}
