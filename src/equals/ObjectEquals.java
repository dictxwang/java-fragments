package equals;

public class ObjectEquals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String stringOne = "abc";
		String stringTwo = "abc";
		
		System.out.println(stringOne == stringTwo);
		System.out.println(stringOne.equals(stringTwo));
		
		String stringThree = new String("abc");
		String stringFour = new String("abc");
		System.out.println(stringThree == stringFour);
		System.out.println(stringThree.equals(stringFour));
		
		String stringFive = new String("abc");
		String stringSix = "abc";
		System.out.println(stringFive == stringSix);
		System.out.println(stringFive.equals(stringSix));
		System.out.println(stringFive.intern() == stringSix.intern());
	}

}
