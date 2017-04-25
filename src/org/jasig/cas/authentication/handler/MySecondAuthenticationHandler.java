package org.jasig.cas.authentication.handler;

import java.security.GeneralSecurityException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * 
 * Title: MySecondAuthenticationHandler
 * Description: 第二种验证接口
 * @Copyright: Copyright (c) 2017 FFCS All Rights Reserved 
 * @Company: 北京福富软件有限公司  
 * @author youss
 * @date 2017年4月25日
 * @time 下午7:02:45
 */
public class MySecondAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());
	// xml配置文件中指定datasource，
	// 并传入sql
	@NotNull
	private String sql;

	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		String username = credential.getUsername();
		String password = credential.getPassword();
		// 自定义PasswordEncoder的实现
		String encryptedPassword = getPasswordEncoder().encode(username+":"+password);
		try {
			String dbPassword = (String) getJdbcTemplate().queryForObject(this.sql, String.class,
					new Object[] { username });
			if (!dbPassword.equals(encryptedPassword))
				throw new FailedLoginException("Password does not match value on record.");
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0) {
				throw new AccountNotFoundException(username + " not found with SQL query");
			}
			throw new FailedLoginException("Multiple records found for " + username);
		} catch (DataAccessException e) {
			throw new PreventedException("SQL exception while executing query for " + username, e);
		}
		return createHandlerResult(credential, new SimplePrincipal(username), null);
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
