package jmx;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;

import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 * JMX的全称为Java Management Extensions
 * 常用于管理线程，内存，日志Level，服务重启，系统环境等。
 * JMX服务端示例
 * @author wangqiang
 * 可以支持多种方式访问（jconsole、rmi和在线方式）
 */
public class JmxBasicServer {

	public static void main(String[] args) {

		HelloMBean mbean = new Hello();
		mbean.setName("WangQiang");
		startPrintHelloThread(mbean);
		startMBeanServer(mbean);
	}
	
	private static void startPrintHelloThread(final HelloMBean mbean) {
		
		Runnable job = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.SECONDS.sleep(5);
						// 通过jconsole修改name属性后，将打印新的值
						mbean.printHello();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(job).start();
	}

	private static void startMBeanServer(HelloMBean mbean) {
		/**
		 * 使用ManagementFactory.getPlatformMBeanServer() 和
		 * MBeanServerFactory.createMBeanServer() 都可以创建MBeanServer
		 * 并且前者内部也是首先调用后者创建server，再对其属性赋值
		 * 使用前者创建的MBeanServer，可以通过jconsole工具查看到
		 */
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		// MBeanServer server = MBeanServerFactory.createMBeanServer();
		try {
			ObjectName helloName = new ObjectName("lab.jmx:type=Hello");
			server.registerMBean(mbean, helloName);
			
			// 注册在线查看的适配器server
			// 访问地址 http://127.0.0.1:8888
			ObjectName adapterName = new ObjectName("lab.jmx:name=htmladapter,port=8888");
			HtmlAdaptorServer adaptorServer= new HtmlAdaptorServer(8888);
			server.registerMBean(adaptorServer, adapterName);
			adaptorServer.start();
			System.out.println("html adapter server start...");
			
			// 注册rmi
			LocateRegistry.createRegistry(8889);
			/**
			 *  {wangqiang}这一段内容不重要，只要是合法的域名或者ip地址即可
			 *  url的格式： service:jmx: <protocol>://[[[ <host>]: <port>]/ <path>]
			 *  client的url: service:jmx:rmi:///jndi/rmi://localhost:8889/jmxrmi
			 */
			String url = "service:jmx:rmi://wangqiang/jndi/rmi://localhost:8889/jmxrmi";
			JMXServiceURL jmxUrl = new JMXServiceURL(url);
			Map<String, Object> auth = makeAuthenticator();
			JMXConnectorServer jmxServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxUrl, auth, server);
			jmxServer.start();
			System.out.println("jmx connector server start...");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	// 构造认证工具
	private static Map<String, Object> makeAuthenticator() {
		Map<String, Object> prop = new HashMap<>();
		// JMXConnectorServer.AUTHENTICATOR="jmx.remote.authenticator"
		prop.put(JMXConnectorServer.AUTHENTICATOR, new JMXAuthenticator() {
			@Override
			public Subject authenticate(Object credentials) {
				if (credentials instanceof String) {
					if ("xyz123".equals(credentials)) {
						return new Subject();
					}
				}
				throw new SecurityException("not authicated");
			}
		});
		return prop;
	}
}
