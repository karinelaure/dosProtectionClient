package com.dos.protection.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @auther Karine Camhy
 * @since 8/2/2020
 */
public class AppResources {
    private static final Logger logger = LoggerFactory.getLogger(AppResources.class);

    private static Properties properties = new Properties();
    private static final String fileName = "application.properties";

    public AppResources() {
        try {
            final InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
            try {
                properties.load(input);
                properties.forEach((k,v)-> logger.debug(k+"="+v));

            } finally {
                input.close();
            }
        } catch (IOException ex) {
            logger.error("Couldn't load propeties. ",ex);

        }
    }

    public static String getProperties(String key){

        return (String) properties.get(key);
    }

    public static Integer getMaxWait(){
        return Integer.valueOf(getProperties("client.thread.max.wait"));
    }

    public static int getMMinWait(){
        return Integer.valueOf(getProperties("client.thread.min.wait"));
    }

    public static String getUrl(){
        return getProperties("client.url");

    }

    public static String getParamQuery(){
        return getProperties("client.request.param");
    }

}
