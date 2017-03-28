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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

public class MySecondAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

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
