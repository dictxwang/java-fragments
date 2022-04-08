package reflect;

import java.util.List;

public class ReflectParentBean extends ReflectGrandBean {

	private static String common = "this is static message.";
	private String name;
	private int size;
	
	public ReflectParentBean() {}
	
	public ReflectParentBean(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void say() {
		System.out.println("Hi, this is " + this.name);
	}
	
	private String send(String prefix, List<Integer> sizeList, String ...suffixs) {
		return String.format("[SEND] name:%s,size:%d", this.name, this.size);
	}
	
	public static String getCommon() {
		return common;
	}
}
