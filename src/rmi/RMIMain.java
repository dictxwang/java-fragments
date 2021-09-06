package rmi;

import java.util.concurrent.TimeUnit;

/**
 * rmi应用实例
 * @author wangqiang
 * https://last-las.github.io/2020/12/10/%E6%B7%B1%E5%85%A5%E5%AD%A6%E4%B9%A0rmi%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86/
 * rmi是java JNDI的一种，JNDI全称Java Naming and Directory Interface
 */
public class RMIMain {

	public static void main(String[] args) throws Exception {
		RMIServerThread server = new RMIServerThread();
		server.start();
		
		// 需要等待一段时间，确保服务注册成功（服务端的stub和skeleton创建成功）
		TimeUnit.MILLISECONDS.sleep(1000);

		for (int i = 1; i < 10; i++) {
			RMIClientThread client = new RMIClientThread("c00" + 1);
			client.start();
		}
	}

}
