package cglib;

public class ServiceHandler {

	public void service(String input) {
		System.out.println("input is " + input);
	}
	
	public void cal(int a, int b) throws Exception {
		try {
			System.out.println(a / b);
		} catch (Exception exp) {
			throw exp;
		}
	}
}
