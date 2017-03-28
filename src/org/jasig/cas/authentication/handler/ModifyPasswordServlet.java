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
 * Servlet implementation class ModifyPasswordServlet
 * 修改用户密码
 */
public class ModifyPasswordServlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(ModifyPasswordServlet.class);

	private static final long serialVersionUID = 10005L;
       
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
		
		if(username==null || oldPwd==null || newPwd==null || "".equals(username) || "".equals(oldPwd) || "".equals(oldPwd)){
			logger.error("参数异常[username]");
			content = "{\"success\":false,\"errorDesc\":\"传递参数异常\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		
		int count = UserUtils.modifyPwd(username, oldPwd, newPwd);
		
		content = "{\"success\":true}";
		if(count==0){
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));  
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}


