package concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile实际上只能保证可见性，并不能保证操作（例如++操作）的原子性
 * AtomicInteger能保证操作的原子性
 * 因此如果要在并发场景用于计数，需要选择原子对象；而volatile可以用于状态变迁的跟踪判断
 * 
 * @author wangqiang
 *
 */
public class VolatileAtomicBasic {
	
	private static volatile int value = 0;
	private static AtomicInteger atomicValue = new AtomicInteger(0); 

	public static void main(String[] args) throws Exception {
//		useVolatileCount();
		useAtomicIntegerCount();
	}
	
	private static void useAtomicIntegerCount() throws Exception {

		final CountDownLatch cd = new CountDownLatch(50);
		for (int i = 1; i <= 50; i++) {
			new Thread(() -> {
				try {
					int t = 0;
					// 每个线程对value进行1000次加一操作
					while (t++ < 10000) {
						atomicValue.incrementAndGet();
					}
					cd.countDown();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}).start();
		}
		
		cd.await();
		// 预期是500,000 实际值等于预期值
		System.out.println("final atomicValue: " + atomicValue.get());
	}
	
	private static void useVolatileCount() throws Exception {
		
		final CountDownLatch cd = new CountDownLatch(50);
		for (int i = 1; i <= 50; i++) {
			new Thread(() -> {
				try {
					int t = 0;
					// 每个线程对value进行1000次加一操作
					while (t++ < 10000) {
						value++;
						// value += 1 同理
					}
					cd.countDown();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}).start();
		}
		
		cd.await();
		// 预期是500,000 实际值低于预期值
		System.out.println("final value: " + value);
	}

}
