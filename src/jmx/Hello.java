package jmx;

/**
 * MBean接口的实现
 * @author wangqiang
 */
public class Hello implements HelloMBean {

	private String name = "";
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void printHello(String thename) {
		System.out.println("Hello, " + thename);
	}
	
	@Override
	public void printHello() {
		System.out.println("Hello, " + name);
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
