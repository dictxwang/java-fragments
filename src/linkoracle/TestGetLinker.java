package linkoracle;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TestGetLinker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		readAndPrint();
		//insertAndPrompt();
	}

	private static void insertAndPrompt() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getLinker();
			//String sql = "insert into my_test (myid, name, bigtxt) values(?, ?, ?)";
			//String myid = getMyId();
			String myid = "775e6f38-80b6-4502-a759-84116969d680";
			//stmt = conn.prepareStatement(sql);
			//stmt.setString(1, myid);
			//stmt.setString(2, "wangqiang");
			//stmt.setClob(3, oracle.sql.CLOB.empty_lob());
			//stmt.executeUpdate();
			
			String sql_02 = "select bigtxt from my_test where myid = ? for update";
			stmt = conn.prepareStatement(sql_02, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, myid);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				rs.beforeFirst();
				if (rs.next()) {
					String sql_03 = "update my_test set bigtxt = ? where myid = ?";
					Clob c = rs.getClob(1);
					c.setString(1, "是一个好人～");
					PreparedStatement pstmt = conn.prepareStatement(sql_03);
					pstmt.setClob(1, c);
					pstmt.setString(2, myid);
					pstmt.executeUpdate();
					pstmt.close();
				}
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception exp) {
				System.out.println(exp.getMessage());
			}
		}
	}

	private static void readAndPrint() {
		try {
			Connection conn = getLinker();
			String myid = "775e6f38-80b6-4502-a759-84116969d680";
			String sql = "select myid, name, bigtxt from my_test where myid = ?";
			PreparedStatement stmt = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, myid);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				rs.beforeFirst();
				while (rs.next()) {
					//System.out.println(rs.getString("name") + " - " + rs.getString("bigtxt"));
					//System.out.println(rs.getString("bigtxt"));
					Clob c = rs.getClob(3);
					Reader r = c.getCharacterStream();
					BufferedReader br = new BufferedReader(r);
					StringBuffer result = new StringBuffer(rs.getString(1));
					result.append("\n");
					result.append(rs.getString(2));
					result.append("\n");
					String linestr = null;
					while ((linestr = br.readLine()) != null) {
						result.append(linestr);
					}
					br.close();
					r.close();
					System.out.println(result);
				}
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
		}
	}

	private static Connection getLinker()
	throws SQLException, ClassNotFoundException {
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String uid = "wangqiang";
		String pwd = "123123";
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection(url, uid, pwd);
		return conn;
	}

	private static String getMyId() {
		return UUID.randomUUID().toString();
	}
}
