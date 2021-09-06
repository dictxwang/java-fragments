package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StatementTest implements Configure {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Class.forName(DRIVER_CLASS);
			Connection connection = DriverManager.getConnection(URL_LOCAL_HOST, USER_LOCAL, PASSWORD_LOCAL);
			Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
			String sql = "select * from myisamtest";
			connection.setAutoCommit(false);
			
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.afterLast();
			int lineCount = resultSet.getRow();
			System.out.println(lineCount);
			
			resultSet.beforeFirst();
			boolean absoluteOne = resultSet.absolute(2);
			System.out.println(absoluteOne);
			String nameOne = resultSet.getString("name");
			System.out.println(nameOne);
			boolean absoluteTwo = resultSet.absolute(1);
			System.out.println(absoluteTwo);
			String nameTwo = resultSet.getString("name");
			System.out.println(nameTwo);
			
			// concurrent 设置不为 CONUR_UPTABLE时，不能进行更改和插入操作
//			resultSet.updateString("name", "new name");
//			resultSet.updateRow();

//			resultSet.moveToInsertRow();
//			resultSet.updateString("name", Utils.getUUID());
//			resultSet.insertRow();
			
			connection.commit();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
