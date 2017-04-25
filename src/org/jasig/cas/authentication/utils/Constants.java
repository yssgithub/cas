package org.jasig.cas.authentication.utils;

import java.util.Map;

/**
 * 
 * Title: Constants
 * Description: 配置文件读取工具类
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午7:07:32
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
