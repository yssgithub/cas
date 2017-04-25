package org.jasig.cas.authentication.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jasig.cas.authentication.entry.UserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author youss
 * @since 2017-03-23
 * @company ffcs
 */
/**
 * 
 * Title: JsonUtil
 * Description: Json <---> Object 转换工具类
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午7:08:14
 */
public class JsonUtil {

	/* 
	 * json -> java
	 */
	public static UserInfo getObject4Json(String jsonUserInfo, Class clazz){
		JSONObject jsonObj = JSONObject.fromObject(jsonUserInfo);
		UserInfo userInfo = (UserInfo) JSONObject.toBean(jsonObj, clazz);
		return userInfo;
	}
	
	/*
	 * jsons -> java[]
	 */
	public static List getObjects4Json(String jsonString, Class clazz){
		JSONArray array = JSONArray.fromObject(jsonString); 
		List clazzs = (List) JSONArray.toCollection(array, clazz);
		return clazzs;
	}
	/*
	 * jsons -> java[]
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();

	}
	/*
	 * jsons -> java[]
	 */
	public static List getObjectArray4Json(String jsonString, Class clazz) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, clazz);
			list.add(pojoValue);
		}
		return list;
	}

	/*
	 * java -> json
	 */
	public static String getJson4Object(Object obj){
		JSONObject jsonObj = JSONObject.fromObject(obj);
		return jsonObj.toString();
	}
	
	/*
	 * javas -> jsons
	 */
	public static String getJson4Object(Object[] obj){
		JSONArray jsonArray = JSONArray.fromObject(obj);
		return jsonArray.toString();
	}

	/*
	 * jsons -> java String[]
	 */
	public static String[] getStringArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}
		return stringArray;
	}
	
	/*
	 * map -> json 
	 */
    private static JSONObject createJSONObject(Map<String, Object> map){  
        JSONObject jsonObject = new JSONObject();
        for (String key : map.keySet()) {
			jsonObject.put(key, map.get(key));
		}
        return jsonObject;   
    }   

	public static void main(String[] args) {
		
	}

}