package org.jasig.cas.authentication.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.entry.UserInfo;
import org.jasig.cas.authentication.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Title: GetUserInfoServlet
 * Description: 获取用户信息
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:57:33
 */
public class GetUserInfoServlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(ModifyUserInfoServlet.class);
	
	private static final long serialVersionUID = 10005L;
       
    public GetUserInfoServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String account = request.getParameter("account");
		logger.info("获取用户:"+account+"的信息  开始");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setContentType("text/json; charset=UTF-8");
		String content = "";
		
		boolean accountExists = UserUtils.checkUsernameExists(account);
		if(!accountExists){
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			logger.info("获取用户:"+account+"的信息  --> 用户不存在");
			return;
		}
		
		UserInfo userInfo = UserUtils.getUserInfo(account);
		
		if(userInfo==null){
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
			logger.info("获取用户:"+account+"的信息  --> 无法从数据库中搜索相关信息");
		}else{
			//如果为null，则设置为""
			String areaCode = userInfo.getAreaCode();
			String digitalID = userInfo.getDigitalID();
			String email = userInfo.getEmail();
			String password = userInfo.getPassword();
			String regTime = userInfo.getRegTime();
			int userID = userInfo.getId();
			content = "{\"success\": true,"
					+ " \"user\":{"
						+ "\"account\":\""+account+"\""
						+ ",\"areaCode\":\""+(areaCode==null?"":areaCode)+"\""
						+ ",\"digitalID\":\""+(digitalID==null?"":digitalID)+"\""
						+ ",\"email\":\""+email+"\""
						+ ",\"password\":\""+password+"\""
						+ ",\"regTime\":\""+regTime+"\""
						+ ",\"userID\":"+userID
						+"}"
					+ "}"; 
			logger.info("content:"+content);
			logger.info("获取用户:"+account+"的信息  --> {userID:"+userID+",account:"+account
						+",areaCode:"+areaCode+",digitalID:"+digitalID+",email:"+email
						+",password:"+password+",regTime:"+regTime+"}");
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
