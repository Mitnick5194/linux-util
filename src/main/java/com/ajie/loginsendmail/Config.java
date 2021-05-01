package com.ajie.loginsendmail;

import java.io.*;
import java.util.Properties;

/**
 * 配置
 */
public class Config {
    private static Config config;

    private Properties properties;

    public Config() {
        loadConfig();
    }

    public Properties getProperties() {
        return properties;
    }

    private void loadConfig() {
        InputStream is = null;
        try {
            String configPath = System.getProperty("config_path");
            if(null != configPath){
                is = loadConfigFromPath(configPath);
                return;
            }
            is = this.getClass().getClassLoader().getResourceAsStream("source.properties");
            Properties p = new Properties();
            p.load(is);
            properties = p;
        } catch (Exception e) {
            throw new IllegalArgumentException("加载配置文件失败", e);
        }finally {
            if(null != null){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private InputStream loadConfigFromPath(String path) throws Exception {
        InputStream is = new FileInputStream(new File(path));
        Properties p = new Properties();
        p.load(is);
        properties = p;
        return is;
    }

    public static Config getConfig() {
        if (null != config) {
            return config;
        }
        Config c = new Config();
        config = c;
        return c;
    }
}
