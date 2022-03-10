package equals;

public class ObjectEquals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String stringOne = "abc";
		String stringTwo = "abc";
		
		System.out.println(stringOne == stringTwo);  // true
		System.out.println(stringOne.equals(stringTwo));  // true
		
		String stringThree = new String("abc");
		String stringFour = new String("abc");
		System.out.println(stringThree == stringFour);  // false
		System.out.println(stringThree.equals(stringFour));  // true
		
		String stringFive = new String("abc");
		String stringSix = "abc";
		System.out.println(stringFive == stringSix);  // false
		System.out.println(stringFive.equals(stringSix));  // true
		System.out.println(stringFive.intern() == stringSix.intern());  // true
	}

}
