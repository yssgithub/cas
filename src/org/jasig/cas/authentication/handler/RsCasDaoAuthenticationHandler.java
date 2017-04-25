/** 
 * Project Name:JhostAddTime 
 * File Name:RsCasDaoAuthenticationHandler.java 
 * Package Name:org.jasig.cas.authentication.handler 
 * Date:2013-4-25下午04:20:35 
 * Copyright (c) 2013, riambsoft All Rights Reserved. 
 * 
 */  
       
package org.jasig.cas.authentication.handler;  
       
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.authentication.utils.ConnDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
       
/**
 * 
 * Title: RsCasDaoAuthenticationHandler
 * Description: 密码加密工具类 ，通过java代码中固定连接数据库及实现认证(不推荐)
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午7:05:01
 */
public final class RsCasDaoAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {  
       
	private final Logger log = LoggerFactory.getLogger(getClass());
    public RsCasDaoAuthenticationHandler() {  
    	
    }  
       
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		
		String username = credential.getUsername();  
        String password = credential.getPassword();  
       
        // 取得MD5加密后的字符串  
//      String md5pwd = DigestUtils.md5Hex(password);
//		String userNameMD5 = DigestUtils.md5Hex(username);
//		md5pwd = DigestUtils.md5Hex(userNameMD5.substring(7, 8) + md5pwd);
        
        String md5pwd = MyPasswordEncoder.getPassword(username, password);
       
        log.info("开始CAS认证方式 RsCasDaoAuthenticationHandler......");  
        log.info("用户登录:userName[" + username+"],password[" + password+"]");  
        log.info("用户登录:md5pwd:[" + md5pwd+"]");  
       
        // 连接数据库  
        Connection conn = null;  
        PreparedStatement ps = null;  
        ResultSet rs = null;  
        //默认表示不通过
        boolean flag = false;
        try{    
            conn = ConnDBUtil.getConnection();  
            //String sql = "select count(1)  from t_sys_user_info where  user_account='" + username + "' and password='" + password + "'";  
            //只查密码，与上面的加密后密码比较
            String sql = "select password  from t_sys_user_info where  user_account='" + username + "'";  
            ps = conn.prepareStatement(sql);  
            rs = ps.executeQuery();  
            //用while防止重复的username(注册的要判重)
            while (rs != null && rs.next()) {  
                String dbPassword = rs.getString(1);  
                log.info("查询数据库:dbPassword:[" + dbPassword+"]");  
                if (md5pwd.equals(dbPassword)) { 
                	//存在相当，则通过
                    flag = true;
                }  
            }  
        } catch (Exception e) {  
        	log.error("操作数据库异常："+e.getMessage());
        } finally {  
            try {  
                if (rs != null) {  
                    rs.close();  
                }  
                if (ps != null) {  
                    ps.close();  
                }  
                if (conn != null) {  
                    conn.close();  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        } 
        if(!flag){
        	log.info("登录失败:[用户登录使用的账号或密码无法得到验证]");  
        	//如果不存在，则登录失败  
        	throw new FailedLoginException("Password does not match value on record.");  
        }
        return createHandlerResult(credential, new SimplePrincipal(username), null);
	}  
       
}