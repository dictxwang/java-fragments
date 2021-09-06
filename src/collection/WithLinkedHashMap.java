package collection;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class WithLinkedHashMap {

	public static void main(String[] args) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(10, 0.75f, true);
		map.put("a", "a001");
		map.put("b", "b001");
		map.put("c", "c001");
		map.put("d", "d001");
		map.put("e", "e001");
		
//		String valueE = map.get("e");
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.printf("key:%s\tvalue:%s\n", key, map.get(key));
		}
	}
}
