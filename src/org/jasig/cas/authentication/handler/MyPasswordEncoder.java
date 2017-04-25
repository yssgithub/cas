package org.jasig.cas.authentication.handler;

import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * Title: MyPasswordEncoder
 * Description: 密码加密工具类 
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:59:50
 */
public class MyPasswordEncoder implements PasswordEncoder {
	
	@Override
	public String encode(String content) {
		System.out.println("password[luchg]:"+content);
		return md5(md5(content) + "luchg");
	}
	
	public String md5(String content){
		if(content == null){
			return null;
		}
		StringBuffer sbReturn = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update((content).getBytes("utf-8"));
			
			for (byte b : md.digest()) {
				sbReturn.append(Integer.toString((b & 0xff) + 0x100, 16)
						.substring(1));
			}
			return sbReturn.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 密码加密
	 * 
	 * @param username   用户名
	 * @param password   密码
	 * @return  加密后密码
	 */
	public static String getPassword(String username, String password)
	{
		String pwd = DigestUtils.md5Hex(password);
		String userNameMD5 = DigestUtils.md5Hex(username);
		return DigestUtils.md5Hex(userNameMD5.substring(7, 15) + pwd);
	}
	
	public static void main(String[] args) {
		
//		System.out.println("password[1]:"+new MyPasswordEncoder().encode("1"));
//		System.out.println("password[2]:"+new MyPasswordEncoder().encode("2"));
//		System.out.println("password[ffcs]:"+new MyPasswordEncoder().encode("ffcs"));
		System.out.println(MyPasswordEncoder.getPassword("admin", "1"));
		System.out.println(MyPasswordEncoder.getPassword("ffcs", "1"));
	}

}
