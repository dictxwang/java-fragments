package pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pattern p = Pattern.compile("^[0-9]+.*[a-z]$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher("123sdf34XzAS");
		System.out.println(m.matches());

		Pattern p2 = Pattern.compile("(?!z)abc", Pattern.CASE_INSENSITIVE);
		Matcher m2 = p2.matcher("abc");
		System.out.println(m2.matches());
		while(m2.find()) {
			System.out.println(m2.start());
		}
		
		Pattern p3 = Pattern.compile("\\w+");
		Matcher m3 = p3.matcher("y345_asfw45_zx");
		System.out.println(m3.matches());
	}

}
