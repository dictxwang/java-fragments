package security;

import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.Policy;
import java.security.PrivilegedAction;

/**
 * Java安全策略的一些示例
 * @author wangqiang
 */
public class SecurityBasic {

	public static void main(String[] args) throws Exception {
		simple();
		withEmptySecurityManager();
		useDoPrivileged();
	}
	
	public static void useDoPrivileged() throws Exception {

		// 通过设置jvm参数 -Djava.security.manager ，开启java的安全检查
		// 通过设置jvm参数 -Djava.security.policy="xx/policy.all"配置策略文件位置
		/** 
		 * 未开启安全检查时，这里获取的系统变量将为空
		String security = System.getProperty("java.security.manager");
		System.out.println(security);
		*/
		if (System.getSecurityManager() == null) {
			Policy policy = Policy.getPolicy();
			/**
			 *  这里获取的是编译路径下的文件，如果将策略文件放置在resources路径，
			 *  需要将resources设置为source目录或者在打包时进行复制
			 *  JDK自带的策略文件在JRE的 lib/security 路径下
			 *  放开所有权限的策略配置：
			 *  
			 	grant { 
				    permission java.security.AllPermission;
				};
			 */
		    URL policyURL = SecurityBasic.class.getResource("/policys/policy.all");
		    System.setProperty("java.security.policy", policyURL.toString());
		    System.setProperty("policy.allowSystemProperty", "true");
		    policy.refresh();
			System.setSecurityManager(new SecurityManager());
		}
		Class<Hello> clazz = Hello.class;
		Hello hello = clazz.newInstance();
		Method method = clazz.getDeclaredMethod("say");
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			@Override
			public Void run() {
				try {
					if (!method.isAccessible()) {
						method.setAccessible(true);
					}
					method.invoke(hello);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
				return null;
			}
		});
	}
	
	private static void withEmptySecurityManager() throws Exception {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		Class<Hello> clazz = Hello.class;
		Hello hello = clazz.newInstance();
		Method method = clazz.getDeclaredMethod("say");
		// 这里会抛出异常：因为设置了SecurityManager，但是安全策略为空，相当于未授权
		method.setAccessible(true);
		method.invoke(hello);
	}
		
	private static void simple() throws Exception {

		Class<Hello> clazz = Hello.class;
		Hello hello = clazz.newInstance();
		Method method = clazz.getDeclaredMethod("say");
		method.setAccessible(true);
		method.invoke(hello);
	}
}

class Hello {
	private String name;
	
	{
		System.out.println("> this is instance default code");
	}
	
	// 静态块类每次加载时会执行一次
	static {
		System.out.println("> this is static default code");
	}
	
	public String getName() {
		return name;
	}
	
	private void say() {
		System.out.println("hello");
	}
}