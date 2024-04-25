package wjh.projects.common.util;

import wjh.projects.common.constants.PropertiesEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties properties;

    static {
        try {
            InputStream in = PropertiesUtil.class.getResourceAsStream("/application-common.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(PropertiesEnum propertiesEnum) {
        return properties.getProperty(propertiesEnum.getKey());
    }
}
