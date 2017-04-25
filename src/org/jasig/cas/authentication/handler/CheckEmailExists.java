package org.jasig.cas.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * Title: CheckEmailExists
 * Description: 注册时验证用户名唯一
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:56:56
 */
public class CheckEmailExists extends HttpServlet {
	private Logger logger = LoggerFactory.getLogger(CheckEmailExists.class);
	
	private static final long serialVersionUID = 10003L;
       
    public CheckEmailExists() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("~~~ 开始 ： 给定账号验证邮箱是否存在 ~~~");
		boolean flag = false;
		String content = "";
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/json;charset=UTF-8");
		response.setContentType("text/json; charset=UTF-8");  

		String username = request.getParameter("account");
		if(username==null || "".equals(username)){
			logger.error("参数异常[username]");
			content = "{\"success\":false,\"errorDesc\":\"参数异常\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		//要先判断参数中的用户是否存在，如果不存在
		boolean checkUsernameExists = UserUtils.checkUsernameExists(username);
		if(!checkUsernameExists){
			logger.error("参数异常[username]");
			content = "{\"success\":false,\"errorDesc\":\"账号不存在\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		
		//如果存在则再判断是否存在邮箱地址
		flag = UserUtils.checkEmailExist(username);
		content = "{\"success\":true, \"isexist\":true}";
		logger.info("已存在邮箱地址");
		if(!flag){
			content = "{\"success\":false,\"errorDesc\":\"数据库中不存在指定用户的email字段\"}";
			logger.info("不存在邮箱地址");
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));
		
		logger.info("~~~ 结束 ： 给定账号验证邮箱是否存在 ~~~");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
}
