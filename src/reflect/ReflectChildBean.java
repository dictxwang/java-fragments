package reflect;

public class ReflectChildBean extends ReflectParentBean {

	private String address;
	
	public ReflectChildBean(String name, int size, String address) {
		super(name, size);
		this.address = address;
	}
	
	public void message() {
		System.out.println("Child at " + this.address);
	}
}
