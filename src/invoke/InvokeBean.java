package invoke;

public class InvokeBean {

	private static final String common = "this is Bean";
	public String name;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static void hello() {
		System.out.println("Hello, common message: " + common);
	}
	
	public void say(String content) {
		System.out.println("Say: " + content);
	}
}
