package concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchBasic {

	public static void main(String[] args) throws Exception {
		
		System.out.println("Main task started.");
		final CountDownLatch cd = new CountDownLatch(3);
		final Random rand = new Random();
		for (int i = 1; i <= 3; i++) {
			final int num = i;
			new Thread(()->{
				try {
					TimeUnit.SECONDS.sleep(rand.nextInt(5));
					System.out.println("Child task-" + num + " finished.");
					cd.countDown();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}) {
			}.start();
		}
		
		cd.await();
		System.out.println("Main task finished.");
	}
}
