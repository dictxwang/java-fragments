package collection;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class WithIdentityHashMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		IdentityHashMap<String, String> map = new IdentityHashMap<String, String>();
		String keyOne = "abc";
		String keyTwo = new String("abc");
		
		map.put(keyOne, "value for key one");
		map.put(keyTwo, "value for key two");
		
		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			System.out.printf("key:%s\tvalue:%s\n", entry.getKey(), entry.getValue());
		}
		
		System.out.println("=====just split====");
		
		HashMap<String, String> mapnormal = new HashMap<String, String>();
		mapnormal.put(keyOne, "value for key one");
		mapnormal.put(keyTwo, "value for key two");
		Iterator<Map.Entry<String, String>> entriesNormal = mapnormal.entrySet().iterator();
		while (entriesNormal.hasNext()) {
			Map.Entry<String, String> entry = entriesNormal.next();
			System.out.printf("key:%s\tvalue:%s\n", entry.getKey(), entry.getValue());
		}
	}

}
