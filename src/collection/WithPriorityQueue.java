package collection;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class WithPriorityQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PriorityQueue<Item> queue = new PriorityQueue<Item>(10, new ItemComparator());
		
		queue.offer(new Item(1, "abc"));
		queue.offer(new Item(5, "cdr"));
		queue.offer(new Item(3, "qqe"));
		queue.offer(new Item(4, "abcd"));
		queue.offer(new Item(6, "cdrss"));
		
//		Iterator<Item> items = queue.iterator();
//		while (items.hasNext()) {
//			Item item = items.next();
//			System.out.println(item.toString());
//		}
		
		Item item = null;
		while ((item = queue.poll()) != null) {
			System.out.println(item.toString());
		}
	}

}

class ItemComparator implements Comparator<Item> {
	
	public int compare(Item paramT1, Item paramT2) {
//		return paramT1.id - paramT2.id;
		
		String v1 = paramT1.name;
		String v2 = paramT2.name;
		
		int len1 = v1.length();
		int len2 = v2.length();
		int iteratorCount = len1 < len2 ? len1 : len2;
		
		char c1,c2;
		for (int i = 0; i < iteratorCount; i++) {
			c1 = v1.charAt(i);
			c2 = v2.charAt(i);
			if (c1 != c2) {
				return c1 - c2;
			}
		}
		return len1 - len2;
	}
}

class Item {
	String name = "";
	int id = 0;
	
	public Item(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Item [name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
