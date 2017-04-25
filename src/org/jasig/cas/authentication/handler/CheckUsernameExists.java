package org.jasig.cas.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Title: CheckUsernameExists
 * Description: 注册时验证用户名唯一
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:57:17
 */
public class CheckUsernameExists extends HttpServlet {
	private Logger logger = LoggerFactory.getLogger(CheckUsernameExists.class);
	
	private static final long serialVersionUID = 10004L;
       
    public CheckUsernameExists() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		flag = UserUtils.checkUsernameExists(username);
		content = "{\"success\":true, \"isexist\":false, \"errorDesc\":\"数据库中找不到指定的用户\"}";
		if(flag){
			content = "{\"success\":true, \"isexist\":true,\"errorDesc\":\"数据库中已存在该用户\"}";
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
}
