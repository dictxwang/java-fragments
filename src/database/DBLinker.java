
package database;

import java.sql.*;
//import sun.jdbc.odbc.JdbcOdbcDriver;

public class DBLinker {

	public static Connection getLinker() {
		Connection conn = null;
		String dbpath = "D:\\lala.mdb";
		try {
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + dbpath;
			
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection(url, "", "");
		} catch (Exception exp) {
			conn = null;
		}
		return conn;
	}
}
