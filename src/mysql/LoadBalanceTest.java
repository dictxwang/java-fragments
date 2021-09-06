package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

public class LoadBalanceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InsertThread insertThreadOne = new InsertThread("t1");
		InsertThread insertThreadTwo = new InsertThread("t2");
		
		insertThreadOne.start();
//		insertThreadTwo.start();
	}
}

class InsertThread extends Thread implements Configure {
	
	private String name = "";
	private int count = 0;
	
	public InsertThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		Connection connection = null;
		try {
			Class.forName(DRIVER_CLASS);
			
			while (count++ < 20) {
				Statement statement = null;
				connection = DriverManager.getConnection(URL_BALANCE_HOST, USER_REMOTE, PASSWORD_REMOTE);
				statement = connection.createStatement();
				String sql = "insert into tab1 set name = '"+ Utils.getUUID() +"'";
				statement.execute(sql);
				System.out.println("["+ this.name +":"+ count +"] - " + sql);
				
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
			try {
				connection.close();
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}
}
