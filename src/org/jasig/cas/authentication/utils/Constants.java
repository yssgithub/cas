package org.jasig.cas.authentication.utils;

import java.util.Map;

/**
 * @author youss
 * @version 1.00.00
 * @ClassName:Constants
 * @Description:
 * @date 2017/3/10
 */
public class Constants {

    /**
     * 配置文件转化成map
     */
    public static Map<String, String> CONFIG_MAP = null;
    private static String CONFIG_PATH = "/config.properties";
    
    static {
        CONFIG_MAP = ConfigUtil.parsePropertiesData(CONFIG_PATH);
    }

}
