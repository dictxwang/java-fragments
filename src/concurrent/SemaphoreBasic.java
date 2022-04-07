package concurrent;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreBasic {

	public static void main(String[] args) {
		
		// 默认是非公平模式
		final Semaphore semaphore = new Semaphore(2);
		final Random rand = new Random();
		for (int i = 1; i <= 20; i++) {
			final int num = i;
			new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(1);
					// 尝试获取许可，可以设置最长等待时间；默认是不等待
					if (semaphore.tryAcquire(100, TimeUnit.SECONDS)) {
						System.out.println("Child task-" + num + " get permit");
						TimeUnit.SECONDS.sleep(rand.nextInt(5));
						System.out.println("Child task-" + num + " finished and release permit");
						// 释放锁。需要注意的是，如果未获取到许可，不能执行release，否则会造成permit总量增加
						semaphore.release();
					}
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}).start();
		}
	}
}
