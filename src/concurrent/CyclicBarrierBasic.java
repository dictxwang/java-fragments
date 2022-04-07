package concurrent;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierBasic {

	public static void main(String[] args) {
		
		System.out.println("Main task started.");
		
		// CyclicBarrier在初始化时需要指定后续的主线流程，这一点不如CountDownLatch方便
		final CyclicBarrier cb = new CyclicBarrier(3, () -> {
			System.out.println("Main task finished.");
		});
		
		final Random rand = new Random();
		
		for (int i = 1; i <= 3; i++) {
			final int num = i;
			new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(rand.nextInt(5));
					System.out.println("Child task-" + num + " finished.");
					cb.await();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}) {}.start();
		}
	}
}
