package collection;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class WithLinkedBlockingDeque {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<String>(3);
		deque.offer("abc");
		deque.offer("123");
		deque.offer("abc-123");

		Iterator<String> values = deque.iterator();
		while (values.hasNext()) {
			System.out.println(values.next());
		}
	}

}
