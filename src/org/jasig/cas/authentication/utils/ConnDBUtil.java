package org.jasig.cas.authentication.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnDBUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnDBUtil.class);
	
	private static Connection conn = null;
	
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Map map = ConfigUtil.parsePropertiesData("/config.properties");
        String driver = String.valueOf(map.get("driver"));
        String url = String.valueOf(map.get("url"));
        String user = String.valueOf(map.get("username"));
        String pwd = String.valueOf(map.get("password"));
        
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, pwd);
        
        return conn;
	}
	
	public static void close() throws SQLException{
		if(conn!=null){
			conn.close();
		}
	}

}
