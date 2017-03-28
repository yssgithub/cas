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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户信息操作类
 * @author Administrator
 *
 */
public class UserUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class);
	
	/*
	 * 修改用户密码
	 */
	public static int modifyPwd(String username, String oldPwd, String newPwd){
		//如果提交的参数已经加密过，则这步省去，下面引用oldmd5pwd改成引用oldPwd
		String oldmd5pwd = MyPasswordEncoder.getPassword(username, oldPwd);
		
		int count = 0;//更新成功行数
        // 连接数据库  
        Connection conn = null;  
        PreparedStatement ps = null;  
        ResultSet rs = null;  
        try {  
            conn = ConnDBUtil.getConnection(); 
            String sql = "select count(1) count from t_sys_user_info where user_account='" + username + "' and password='" + oldmd5pwd + "'";  
            ps = conn.prepareStatement(sql);  
            rs = ps.executeQuery();  
            //有且存在一个匹配用户
            if(rs!=null && rs.next() && rs.getInt("count")==1){
            	String newmd5pwd = MyPasswordEncoder.getPassword(username, newPwd);
           		
            	sql = "update t_sys_user_info set password='"+newmd5pwd+"' where user_account='"+ username +"'";
            	rs.close();
            	ps.close();
            	ps = conn.prepareStatement(sql);
            	count = ps.executeUpdate();
            }
        } catch (Exception e) {  
        	LOGGER.error("操作数据库异常："+e.getMessage());
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
		return count;
	}
	
	/*
	 * 验证用户名唯一性
	 * 
	 */
	public static boolean checkUsernameExists(String username) {
		// 不存在
		boolean flag = false;
		// 连接数据库  
        Connection conn = null;  
        PreparedStatement ps = null;  
        ResultSet rs = null;  
        try {  
            conn = ConnDBUtil.getConnection(); 
            
            String sql = "select count(1) as count  from t_sys_user_info where  user_account='" + username + "'";  
            ps = conn.prepareStatement(sql);  
            rs = ps.executeQuery();  
            if(rs!=null && rs.next() && rs.getInt("count")>0){
            	flag = true;//存在
            }
        } catch (Exception e) {  
        	LOGGER.error("操作数据库异常："+e.getMessage());
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
		return flag;
	}
	
	/*
	 * 新增单个用户
	 * 
	 */
	public static int addUserInfo(String name, String account, String password, String digitalID, String email, String areaCode, String regTime) {
		int count = 0;
		// 连接数据库  
        Connection conn = null;  
        PreparedStatement ps = null;  
        try {  
            conn = ConnDBUtil.getConnection(); 
            
            String sql = "insert into `t_sys_user_info` (`user_name`,`user_account`,`password`,`digitalid`,`email`,`reg_time`,`area_code`) "+
            			 " values('"+name+"','"+account+"','"+password+"',"+digitalID+",'"+email+"',STR_TO_DATE('"+regTime+"','%Y-%m-%d %H:%i:%s'),"+areaCode+")";  
            ps = conn.prepareStatement(sql);  
            count = ps.executeUpdate();  
        } catch (Exception e) {  
        	LOGGER.error("操作数据库异常："+e.getMessage());
        } finally {  
            try {  
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
		return count;
	}
	/*
	 * 新增单个用户
	 * 
	 */
	public static int addUserInfoObj(UserInfo userInfo) {
		int count = 0;
		// 连接数据库  
		Connection conn = null;  
		PreparedStatement ps = null;  
		try {  
			conn = ConnDBUtil.getConnection(); 
			
			String sql = "insert into `t_sys_user_info` (`user_name`,`user_account`,`password`,`digitalid`,`email`,`reg_time`,`area_code`) "+
					" values('"+userInfo.getName()+"','"+userInfo.getAccount()+"','"+userInfo.getPassword()+"',"+userInfo.getDigitalID()+",'"
					+userInfo.getEmail()+"',STR_TO_DATE('"+userInfo.getRegTime()+"','%Y-%m-%d %H:%i:%s'),"+userInfo.getAreaCode()+")";  
			ps = conn.prepareStatement(sql);  
			count = ps.executeUpdate();  
		} catch (Exception e) {  
			LOGGER.error("操作数据库异常："+e.getMessage());
		} finally {  
			try {  
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
		return count;
	}
	
	
	public static int addUserInfos(String jsonUserInfos){
		UserInfo[] userInfos = getJsonTOArray(jsonUserInfos, UserInfo.class);
		
		//批量插入数据库
		
		
		return 0;
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
		// 连接数据库  
        Connection conn = null;  
        PreparedStatement ps = null;  
        ResultSet rs = null;  
        try {  
            conn = ConnDBUtil.getConnection(); 
            
            String sql = "select email from t_sys_user_info where user_account='" + username + "'";  
            ps = conn.prepareStatement(sql);  
            rs = ps.executeQuery();  
            if(rs!=null && rs.next() && !"".equals(rs.getString("email"))){
            	flag = true;//存在
            }
        } catch (Exception e) {  
        	LOGGER.error("操作数据库异常："+e.getMessage());
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
		return flag;
	}
}
