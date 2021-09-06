package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class ReplicatioinTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		WriteThread writeThreadOne = new WriteThread("wt1");
		WriteThread writeThreadTwo = new WriteThread("wt2");
		
		ReadThread readThreadOne = new ReadThread("rt1");
		ReadThread readThreadTwo = new ReadThread("rt2");
		
//		writeThreadOne.start();
//		writeThreadTwo.start();
		
		readThreadOne.start();
		readThreadTwo.start();
	}

}

class ReadThread extends Thread implements Configure {
	private String name = null;
	private int count = 0;
	
	public ReadThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {

		try {
			Class.forName(DRIVER_CLASS);
			Connection connection = null;
			Statement statement = null;
			while (count++ < 10) {
				connection = DriverManager.getConnection(URL_REPLICATION_HOST, USER_REMOTE, PASSWORD_REMOTE);
				statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
				
				String sql = "select * from tab1 order by id desc limit 0, 1";
				ResultSet resultSet = statement.executeQuery(sql);
				if (resultSet == null) {
					continue;
				}
				resultSet.beforeFirst();
				while (resultSet.next()) {
					System.out.printf("[%s:%d] - %s - %s\n", name, count, resultSet.getString("name"), sql);
				}
				resultSet.moveToInsertRow();
				resultSet.updateString("name", "xxxxxxx");
				resultSet.insertRow();
				resultSet.close();
				
				try {
					statement.close();
					connection.close();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
				Thread.sleep(new Random().nextInt(600));
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
	}
}

class WriteThread extends Thread implements Configure {
	private String name = null;
	private int count = 0;
	
	public WriteThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		
		System.out.println("start");
		Connection connection = null;
		Statement statement = null;
		
		try {
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(URL_REPLICATION_HOST, USER_REMOTE, PASSWORD_REMOTE);
			statement = connection.createStatement();
			
			while (count++ < 50) {
				String sql = "insert into tab1 set name = '"+ Utils.getUUID() +"'";
				statement.execute(sql);
				
				System.out.println("["+ this.name +":"+ count +"] - " + sql);
				Thread.sleep(new Random().nextInt(600));
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}
}