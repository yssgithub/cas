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
 * Title: ModifyPasswordServlet
 * Description: 修改用户密码
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:57:50
 */
public class ModifyPasswordServlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(ModifyPasswordServlet.class);

	private static final long serialVersionUID = 10006L;
       
    public ModifyPasswordServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("account");
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("password");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setContentType("text/json; charset=UTF-8");
		String content = "";
		
		if(username==null || oldPwd==null || newPwd==null || "".equals(username) || "".equals(oldPwd) || "".equals(newPwd)){
			logger.error("参数异常[username]");
			content = "{\"success\":false,\"errorDesc\":\"传递参数异常\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		
		int count = UserUtils.modifyPwd(username, oldPwd, newPwd);
		
		content = "{\"success\":true,\"desc\":\"密码修改成功\"}";
		if(count==0){
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));  
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}


