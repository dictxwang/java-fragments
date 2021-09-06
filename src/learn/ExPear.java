package learn;

public class ExPear extends Pear {

	private String exname = "xxyy";
	public static String sexname = "S xxyy";
	public ExPear() {
		super("", "");
	}
	
	private String exPrint() {
		return "this is private method";
	}
	
	public static void exStaticMethod() {}
}
