package org.jasig.cas.authentication.handler;

import org.apache.commons.codec.digest.DigestUtils;

public class MySecondPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(String usernameAndPassword) {
		if(usernameAndPassword == null || "".equals(usernameAndPassword) || !usernameAndPassword.contains(":")){
			return null;
		}
		String username = usernameAndPassword.split(":")[0];
		String password = usernameAndPassword.split(":")[1];
		 // 取得MD5加密后的字符串  
        String md5pwd = DigestUtils.md5Hex(password);
		String userNameMD5 = DigestUtils.md5Hex(username);
		md5pwd = DigestUtils.md5Hex(userNameMD5.substring(7, 8) + md5pwd);
		
		System.out.println("开始MySecond认证方式MySecondAuthenticationHandler......");  
        System.out.println("userName:" + username);  
        System.out.println("password:" + password);  
        System.out.println("md5pwd:" + md5pwd);  
	        
		return md5pwd;
	}

}
