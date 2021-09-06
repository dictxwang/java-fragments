package jmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.JMX;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * JMX客户端示例
 * @author wangqiang
 * 还可以通过jconsole工具或者在线方式访问MBean
 */
public class JmxBasicClient {

	public static void main(String[] args) {
		try {
			MBeanServerConnection mbsc = getConnection();
			List<String> allObjectNames = queryAllObjectName(mbsc);
			allObjectNames.stream().forEach(System.out::println);
			
			HelloMBean mbean = getHelloProxy(mbsc);
			// 重置属性
			mbean.setName("Changed-Name-2");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	// 获取rmi的链接
	private static MBeanServerConnection getConnection() throws Exception {
		String jmxurl = "service:jmx:rmi:///jndi/rmi://localhost:8889/jmxrmi";
		JMXServiceURL serviceURL = new JMXServiceURL(jmxurl);
		
		Map<String, String> credentials = new HashMap<>();
		// JMXConnector.CREDENTIALS="jmx.remote.credentials"
		credentials.put(JMXConnector.CREDENTIALS, "xyz123");
		
		JMXConnector connector = JMXConnectorFactory.connect(serviceURL, credentials);
		MBeanServerConnection mbsc = connector.getMBeanServerConnection();
		return mbsc;
	}

	// 获取MBean的代理
	private static HelloMBean getHelloProxy(MBeanServerConnection mbsc) throws Exception {
		ObjectName objectName = new ObjectName("lab.jmx:type=Hello");
		HelloMBean mbean = JMX.newMBeanProxy(mbsc, objectName, HelloMBean.class);
		return mbean;
	}

	private static List<String> queryAllObjectName(MBeanServerConnection mbsc) throws Exception {
		List<String> result = new ArrayList<>();
		// 设置ObecjtName可以搜索特定的实例: query = new ObjectName("lab.jmx:type=Hello");
		ObjectName query = null;
		Set<ObjectInstance> mbeanset = mbsc.queryMBeans(query, null);
		Iterator<ObjectInstance> mbeaniterator = mbeanset.iterator();
		while (mbeaniterator.hasNext()) {
			ObjectInstance instance = mbeaniterator.next();
			ObjectName objectName = instance.getObjectName();
			MBeanInfo objectInfo = mbsc.getMBeanInfo(objectName);
			result.add(objectName.getCanonicalName());
		}
		return result;
	}
}
