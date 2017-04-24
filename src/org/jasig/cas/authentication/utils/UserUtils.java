package org.jasig.cas.authentication.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jasig.cas.authentication.entry.UserInfo;
import org.jasig.cas.authentication.handler.MyPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户信息操作类
 * @author Administrator
 *
 */
public class UserUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class);
	
	private static JdbcTemplate jdbcTemplate;
	
	public static void setJdbcTemplate(JdbcTemplate template) {
		jdbcTemplate = template;
	}

	/*
	 * 修改用户密码
	 */
	public static int modifyPwd(String username, String oldPwd, String newPwd){
		//如果提交的参数已经加密过，则这步省去，下面引用oldmd5pwd改成引用oldPwd
		String oldmd5pwd = MyPasswordEncoder.getPassword(username, oldPwd);
		
		Integer count = 0;//更新成功行数
        try {  
            String sql = "select count(1) count from t_sys_user_info where user_account='" + username + "' and password='" + oldmd5pwd + "'";  
            count = jdbcTemplate.queryForObject(sql, Integer.class);
            //有且存在一个匹配用户
            if(count == 1){
            	String newmd5pwd = MyPasswordEncoder.getPassword(username, newPwd);
            	sql = "update t_sys_user_info set password='"+newmd5pwd+"' where user_account='"+ username +"'";
            	count = jdbcTemplate.update(sql);
            	LOGGER.info("oldmd5pwd:"+oldmd5pwd+",newmd5pwd:"+newmd5pwd);
            }
        } catch (Exception e) {  
        	LOGGER.error("系统异常："+e.getMessage());
        } 
		return count;
	}
	
	/*
	 * 验证用户名唯一性
	 * 
	 */
	public static boolean checkUsernameExists(String username) {
		// 不存在
		boolean flag = false;
        try {  
        	String sql = "select count(1) as count  from t_sys_user_info where  user_account='" + username + "'";  
            //int count = jdbcTemplate.queryForInt(sql, username);
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        	if(count >0){
            	flag = true;//存在
            	LOGGER.info("用户验证结果："+username+" 用户已存在于数据库");
        	}else{
        		LOGGER.info("用户验证结果："+username+" 用户不存在于数据库");
        	}
        } catch (Exception e) {  
        	LOGGER.error("系统异常："+e.getMessage());
        }  
		return flag;
	}
	
	/*
	 * 新增单个用户
	 * 
	 */
	public static int addUserInfo(String name, String account, String password, String digitalID, String email, String areaCode, String regTime) {
		int count = 0;
		
        try {  
            String sql = "insert into `t_sys_user_info` (`user_name`,`user_account`,`password`,`digitalid`,`email`,`reg_time`,`area_code`) "+
            			 " values('"+name+"','"+account+"','"+password+"',"+((digitalID==null||"".equals(digitalID))?"NULL":digitalID)+",'"+email+"',STR_TO_DATE('"+regTime+"','%Y-%m-%d %H:%i:%s'),"+((areaCode==null||"".equals(areaCode))?"NULL":areaCode)+")";  
            LOGGER.info("adduser:"+sql);
            count = jdbcTemplate.update(sql);
        } catch (Exception e) {  
        	LOGGER.error("操作数据库异常："+e.getMessage());
        } 
		return count;
	}
	/*
	 * 新增单个用户
	 * 
	 */
	public static int addUserInfoObj(UserInfo userInfo) {
		int count = 0;
		try {  
			
			String sql = "insert into `t_sys_user_info` (`user_name`,`user_account`,`password`,`digitalid`,`email`,`reg_time`,`area_code`) "+
					" values('"+userInfo.getName()+"','"+userInfo.getAccount()+"','"+userInfo.getPassword()+"',"+((userInfo.getDigitalID()==null||"".equals(userInfo.getDigitalID()))?"NULL":userInfo.getDigitalID())
					+userInfo.getEmail()+"',STR_TO_DATE('"+userInfo.getRegTime()+"','%Y-%m-%d %H:%i:%s')," + ((userInfo.getAreaCode()==null||"".equals(userInfo.getAreaCode()))?"NULL":userInfo.getAreaCode()) +")";  
			count = jdbcTemplate.update(sql);  
		} catch (Exception e) {  
			LOGGER.error("系统异常："+e.getMessage());
		}   
		return count;
	}
	
	/*
	 * 新增批量用户
	 */
	public static int addUserInfos(String jsonUserInfos){
		UserInfo[] userInfos = getJsonTOArray(jsonUserInfos, UserInfo.class);
		int count = 0;
		//批量插入数据库
		
		try {
			String prefix = "INSERT INTO `t_sys_user_info` (`user_name`,`user_account`,`password`,`digitalid`,`email`,`reg_time`,`area_code`)  values ";
			StringBuffer suffix = new StringBuffer();
			
			for (int i = 0; userInfos!=null && i<userInfos.length; i++) {
			     suffix.append("("
			        + userInfos[i].getName() + ","
			        + userInfos[i].getAccount() + ","
			        + userInfos[i].getPassword() + ","
			        +((userInfos[i].getDigitalID()==null||"".equals(userInfos[i].getDigitalID()))?"NULL":userInfos[i].getDigitalID())
			        + userInfos[i].getEmail() + ","
			        + "STR_TO_DATE('"+userInfos[i].getRegTime()+"','%Y-%m-%d %H:%i:%s'),"
			        + userInfos[i].getAreaCode() + ","
			        +((userInfos[i].getAreaCode()==null||"".equals(userInfos[i].getAreaCode()))?"NULL":userInfos[i].getAreaCode())
			        +") ,"); 
			}
			// 构建完整sql  
			String sql = prefix + suffix.substring(0, suffix.length() - 1);
			count = jdbcTemplate.update(sql);
		} catch (Exception e) {
			LOGGER.error("系统异常："+e.getMessage());
		}
		
		return count;
	}
	
	/*
	 * 把json数组转换成java对象数组
	 */
	@SuppressWarnings("rawtypes")
	public static UserInfo[] getJsonTOArray(String jsonUserInfos, Class clazz){ 
		/*
		 * [{ "name":"用户名称","account":"用户账号","password":"密码","digitalID":"数字电子证书编号",
					"email":"电子邮箱","areaCode":"归属地","regTime":"注册时间"},...]
		 */
        JSONArray array = JSONArray.fromObject(jsonUserInfos);  
        
        List<UserInfo> list = (List<UserInfo>) JSONArray.toCollection(array, clazz);
        
        
        UserInfo[] userInfos = new UserInfo[array.size()];  
        for(int i = 0; i < array.size(); i++){  
            JSONObject jsonObject = array.getJSONObject(i);  
            userInfos[i] = (UserInfo) JSONObject.toBean(jsonObject, clazz);  
        }  
        return userInfos;
	}
	
	/*
	 * 
	 */
	public static boolean checkEmailExist(String username) {
		// 不存在
		boolean flag = false;
        try {  
            
            String sql = "select email from t_sys_user_info where user_account='" + username + "'";  
            String email = jdbcTemplate.queryForObject(sql, String.class); 
            if(email!=null && !"".equals(email)){
            	flag = true;//存在
            	LOGGER.info("邮箱验证结果："+username+" 用户的邮箱已存在于数据库");
            }else{
            	LOGGER.info("邮箱验证结果："+username+" 用户的邮箱不存在于数据库");
            }
        } catch (Exception e) {  
        	LOGGER.error("操作数据库异常："+e.getMessage());
        }  
		return flag;
	}
}
