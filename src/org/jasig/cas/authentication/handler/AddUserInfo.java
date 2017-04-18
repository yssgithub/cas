package org.jasig.cas.authentication.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class AddUserInfo
 * 单个用户注册
 */
public class AddUserInfo extends HttpServlet {
	private Logger logger = LoggerFactory.getLogger(AddUserInfo.class);
	
	private static final long serialVersionUID = 10001L;
       
    public AddUserInfo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("~~~ 开始 ： 新增单个用户信息 ~~~");
		String name = request.getParameter("name"); 
		String account = request.getParameter("account"); 
		String password = request.getParameter("password"); 
		String digitalID = request.getParameter("digitalID"); 
		String email = request.getParameter("email"); 
		String areaCode = request.getParameter("areaCode"); 
		String regTime = request.getParameter("regTime"); 
		
		String content = "";
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/json;charset=UTF-8");
		response.setContentType("text/json; charset=UTF-8");  
		
		System.out.println(name+","+account+","+password+","+email);
		if(name==null || account==null || password==null || email==null || "".equals(name)||"".equals(account)||"".equals(password)||"".equals(email)){
			logger.error("参数异常[name,account,password,email]");
			content = "{\"success\":false,\"errorDesc\":\"参数异常\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		if(regTime==null || "".equals(regTime)){
			regTime = sdf.format(new Date());
		}
		
		int count = UserUtils.addUserInfo(name, account, password, digitalID, email, areaCode, regTime);
		if(count==1){
			content = "{\"success\":true}";
			logger.info("新增用户信息成功");
		}else{
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
			logger.info("新增用户信息失败");
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));
		
		logger.info("~~~ 结束 ： 新增单个用户信息 ~~~");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
