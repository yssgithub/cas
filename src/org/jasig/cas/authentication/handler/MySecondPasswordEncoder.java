package org.jasig.cas.authentication.handler;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySecondPasswordEncoder implements PasswordEncoder {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public String encode(String usernameAndPassword) {
		if(usernameAndPassword == null || "".equals(usernameAndPassword) || !usernameAndPassword.contains(":")){
			log.error("登录账号或密码有问题");
			return null;
		}
		String username = usernameAndPassword.split(":")[0];
		String password = usernameAndPassword.split(":")[1];
		 // 取得MD5加密后的字符串  
        String md5pwd = DigestUtils.md5Hex(password);
		String userNameMD5 = DigestUtils.md5Hex(username);
		md5pwd = DigestUtils.md5Hex(userNameMD5.substring(7, 15) + md5pwd);
		
        log.info("开始MySecond认证方式MySecondAuthenticationHandler......");
	    log.info("userName:" + username);    
	    log.info("password:" + password);    
	    log.info("md5pwd:" + md5pwd);    
        
		return md5pwd;
	}

}
