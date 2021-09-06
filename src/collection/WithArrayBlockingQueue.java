package collection;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WithArrayBlockingQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);
		queue.add("abc");
		queue.add("123");
		
		try {
			queue.offer("abc123", 3, TimeUnit.SECONDS);
			
			Iterator<String> values = queue.iterator();
			while (values.hasNext()) {
				System.out.println(values.next());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
