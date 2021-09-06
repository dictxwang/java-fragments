package encoding;

import java.net.URLEncoder;

public class UrlEncodingMain {

	public static void main(String[] args) {
		
		String param = "北京";
		try {
			String encodeParam = URLEncoder.encode(param, "GBK");
			System.out.println(encodeParam);
			System.out.println(URLEncoder.encode(encodeParam, "UTF-8"));
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
