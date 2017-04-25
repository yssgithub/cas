package org.jasig.cas.authentication.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.entry.UserInfo;
import org.jasig.cas.authentication.utils.JsonUtil;
import org.jasig.cas.authentication.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Title: AddUserInfos
 * Description: 单个用户注册
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午6:56:37
 */
public class AddUserInfos extends HttpServlet {
	private Logger logger = LoggerFactory.getLogger(AddUserInfos.class);
	
	private static final long serialVersionUID = 10002L;
       
    public AddUserInfos() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * {"users":[{ "name":"用户名称","account":"用户账号","password":"密码","digitalID":"数字电子证书编号",
					"email":"电子邮箱","areaCode":"归属地","regTime":"注册时间"},...]}
		 */
		String users = request.getParameter("users"); 
		logger.info("users:"+users);
		
		
		String content = "";
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/json;charset=UTF-8");
		response.setContentType("text/json; charset=UTF-8");  
		
		int count=0;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		//UserInfo[] userInfos = UserUtils.getJsonTOArray(users, UserInfo.class);
		List userInfos = JsonUtil.getObjectArray4Json(users, UserInfo.class);
		if(userInfos==null || userInfos.size()<2){
			logger.error("参数异常:未提交任何参数或接口调用错误(该调用添加一个用户的接口却调用了添加多个用户的接口)");
			content = "{\"success\":false,\"errorDesc\":\"参数异常:未提交任何参数或接口调用错误\"}";
			response.getOutputStream().write(content.getBytes("UTF-8"));
			return;
		}
		
		for (Object obj : userInfos) {
			UserInfo userInfo = (UserInfo)obj;
			if(userInfo.getRegTime()==null || "".equals(userInfo.getRegTime())){
				userInfo.setRegTime(sdf.format(new Date()));
			}
			count += UserUtils.addUserInfoObj(userInfo);
		}
		if(count==userInfos.size()){
			content = "{\"success\":true, \"desc\":\"批量新增用户成功\"}";
		}else if(count>0 && count<userInfos.size()){
			content = "{\"success\":false,\"errorDesc\":\"部分添加失败\"}";
		}else{
			content = "{\"success\":false,\"errorDesc\":\"无权限执行该操作\"}";
		}
		response.getOutputStream().write(content.getBytes("UTF-8"));
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
