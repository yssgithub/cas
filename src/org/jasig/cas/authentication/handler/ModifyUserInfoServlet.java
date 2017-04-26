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
 * Title: ModifyUserInfoServlet
 * Description: 修改用户信息
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:58:13
 */
public class ModifyUserInfoServlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(ModifyUserInfoServlet.class);
	
	private static final long serialVersionUID = 10007L;
       
    public ModifyUserInfoServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String account = request.getParameter("account");
		String oldPwd = request.getParameter("oldPwd");
		String password = request.getParameter("password");
		String digitalID = request.getParameter("digitalID");
		String email = request.getParameter("email");
		String areaCode = request.getParameter("areaCode");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setContentType("text/json; charset=UTF-8");
		String content = "";
		
		//验证用户合法性
		boolean accountExists = UserUtils.checkUsernameWithPWDExists(account, oldPwd);
		if(!accountExists){
			logger.error("参数异常[account/oldPwd]:"+account+"/"+oldPwd);
			content = "{\"success\":false,\"errorDesc\":\"用户名或密码传递参数异常\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		//没有要修改的地方
		if((name==null || "".equals(name)) && (password==null || "".equals(password)) && (digitalID==null || "".equals(digitalID))
				&& (email==null || "".equals(email)) && (areaCode==null || "".equals(areaCode))){
			logger.error("没有要修改的参数");
			content = "{\"success\":false,\"errorDesc\":\"没有要修改的参数\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		
		//更新字段
		int count = UserUtils.modifyUserInfo(name, account, oldPwd, password, digitalID, email, areaCode);
		if(count==1){
			content = "{\"success\":true,\"desc\":\"用户信息修改成功\"}";
			logger.info("用户信息修改成功");
		}else{
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
			logger.info("用户信息修改失败：无权限执行该操作");
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));  
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
