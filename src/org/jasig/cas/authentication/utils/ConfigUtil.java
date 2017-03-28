package org.jasig.cas.authentication.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ConfigUtil
 * @Description: 配置文件解析工具类
 * @Copyright: Copyright (c) 2010 FFCS All Rights Reserved
 * @Company 北京福富软件福州分公司
 * @author 屈盛知
 * @date Aug 5, 2013 4:13:32 PM
 * @version 1.00.00
 * @history:
 */
public class ConfigUtil {
	private final static Logger log = LoggerFactory.getLogger(ConfigUtil.class);

	private static Properties parseProperties(String configPath) {
		InputStream in = ConfigUtil.class.getResourceAsStream(configPath);
		Properties properties = new Properties();
		try {
			if (in == null) {
				log.info("找不到配置文件，路径：" + configPath);
				return null;
			}
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("解析配置文件config.properties失败,错误信息:" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	public static Map<String, String> parsePropertiesData(String configPath) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Properties p = parseProperties(configPath);
			if (p == null) {
				return null;
			}
			Set s = p.keySet();
			for (Iterator iter = s.iterator(); iter.hasNext();) {
				String key = String.valueOf(iter.next());
				String value = p.getProperty(key);
				map.put(key.trim(), value.trim());
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
		Map map = ConfigUtil.parsePropertiesData("/config.properties");
		System.out.println(map.get("db_username"));
		System.out.println(map.get("db_password"));
	}
}

