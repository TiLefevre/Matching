package com.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class initialize {
	
	private static final String sql = "com.mysql.cj.jdbc.Driver";
	private static final String host = "jdbc:mysql://mysql-timothelfvr.alwaysdata.net/timothelfvr_matching";
	private static final String user = "227099_timothelf";
	private static final String password = "iYhi!9Ke4p3TuSH";
	
	
	public static void Charge() throws ClassNotFoundException
	{
		Class.forName(sql);
	}

	public static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(host, user, password);
	}
	
	
	public initialize() {	
	}
}
