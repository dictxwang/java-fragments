package learn;

public class InitTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(A.value);
	}

	public static class B {
		static int value = 100;
		static {
			System.out.println("Class B is initialized");
		}
	}
	
	public static class A extends B {
		static {
			System.out.println("Class A is initialized");
		}
	}
}
