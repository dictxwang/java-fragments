package collection;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class WithDelayQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DelayQueue<MyDelayed> queue = new DelayQueue<MyDelayed>();
		queue.offer(new MyDelayed("abc"));
		queue.offer(new MyDelayed("123"));
		queue.offer(new MyDelayed("123-abc"));
		
//		Iterator<MyDelayed> values= queue.iterator();
//		while (values.hasNext()) {
//			System.out.println(values.next().getValue());
//		}
		
		MyDelayed myDelayed = null;
		try {
			while ((myDelayed = queue.take()) == null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(myDelayed.getValue());
	}	
}

class MyDelayed implements Delayed {
	private String value = "";
	private long createTime = 0;

	public MyDelayed(String value) {
		this.value = value;
		this.createTime = new Date().getTime();
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public int compareTo(Delayed paramT) {
		return 0;
	}
	
	@Override
	public long getDelay(TimeUnit paramTimeUnit) {
		long timediff = new Date().getTime() - this.createTime;
		if (timediff < 2000) {
			return 1;
		} else {
			return -1;
		}
	}
}