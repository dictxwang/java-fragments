package cload;

public class ClazzLoadMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(FinalStatic.a);
		
		Singleton instance = Singleton.getInstance();
		System.out.printf("a=%d,b=%d\n", instance.a, instance.b);
	}
}
class FinalStatic {

	public static int b;
	public static final int a = 4 + b;
	
	static {
		System.out.println("静态块被执行");
	}
}

class Singleton {
	public static Singleton instance = new Singleton();
	public static int a;
	public static int b = 0;
	
	private Singleton() {
		a++;
		b++;
	}
	
	public static Singleton getInstance() {
		return instance;
	}
}
