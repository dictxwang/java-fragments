package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionTest implements Configure {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		try {
			Class.forName(DRIVER_CLASS);
			conn = DriverManager.getConnection(URL_LOCAL_HOST, USER_LOCAL, PASSWORD_LOCAL);
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			
			
			String sql01 = "insert into myisamtest set name='"+ Utils.getUUID() +"'";
			String sql02 = "insert into myisamtest set name='"+ Utils.getUUID() +"'";
			
//			String sql01 = "insert into myinnodb set name='abc'";
//			String sql02 = "insert into myinnodb set name='def'";
			
			stmt.execute(sql01);
			stmt.execute(sql02);
			
//			conn.commit();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
