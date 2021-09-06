package mysql;

public interface Configure {

	final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	final String URL_LOCAL_HOST = "jdbc:mysql://localhost:3306/study";
	final String USER_LOCAL = "web";
	final String PASSWORD_LOCAL = "error";
	
	final String URL_REMOTE_HOST = "jdbc:mysql://192.168.3.103:3306/learning";
	final String URL_BALANCE_HOST = "jdbc:mysql:loadbalance://192.168.3.102:3306,192.168.3.103:3306/learning";
	final String URL_REPLICATION_HOST = "jdbc:mysql:replication://192.168.3.103:3306,192.168.3.102:3306/learning";
	
	final String USER_REMOTE = "qiangwang";
	final String PASSWORD_REMOTE = "error";
}
