package concurrent;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author wangqiang
 *
 * Phase可以实现分阶段的同步控制
 */
public class PhaserBasic {

	public static void main(String[] args) throws Exception {
//		multiArrives();
		onceArrive();
	}
	
	private static void onceArrive() {
		System.out.println("Main task started.");
		// 不需要事先指定parties，根据中途调用register动态注册即可
		final Phaser phaser = new Phaser();
		final Random rand = new Random();
		for (int i = 1; i <= 3; i++) {
			phaser.register();
			final int num = i;
			new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(rand.nextInt(5));
					System.out.println("Child task-" + num + " finished.");
					// 通知子任务已经完成
					phaser.arrive();

					// 如果是调用的arriveAndAwaitAdvance()，之后的代码不会立即执行
					// 如果是调用的arrive()，之后的代码会立即执行
					// 只需要进行一个阶段的同步，子线程调用arrive即可
					System.out.println("Child task-" + num + " after await");
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}).start();
		}
		// 将主线程也注册
		phaser.register();
		// 主线程等待其他线程到达屏障点，主线程需要调用arriveAndAwaitAdvance()，否则达不到子任务同步完成的效果
		phaser.arriveAndAwaitAdvance();
		System.out.println("Main task finished");
	}
	
	private static void multiArrives() {
		System.out.println("Main task started.");
		final Phaser phaser = new Phaser() {
			// 重写onAdvance()方法，实现多阶段的处理
			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				switch (phase) {
				case 0: System.out.println("Main task arrive phaser 0: prepared."); return false;
				case 1: System.out.println("Main task arrive phaser 1: doing."); return false;
				case 2: System.out.println("Main task arrive phaser 2: finished."); return true;
				default: return true;
				}
		    }
		};
		
		final Random rand = new Random();
		for (int i = 1; i <= 3; i++) {
			final int num = i;
			Thread t = new Thread(() -> {
				try {
					int p = 0;
					TimeUnit.SECONDS.sleep(rand.nextInt(5));
					System.out.println("Child task-" + num + " arrive " + p);
					// 到达屏障点，并等待其他线程到达后升级到下一阶段
					p = phaser.arriveAndAwaitAdvance();
					
					TimeUnit.SECONDS.sleep(rand.nextInt(5));
					System.out.println("Child task-" + num + " arrive " + p);
					p = phaser.arriveAndAwaitAdvance();

					TimeUnit.SECONDS.sleep(rand.nextInt(5));
					System.out.println("Child task-" + num + " finished.");
					phaser.arriveAndAwaitAdvance();

				} catch (Exception exp) {
					exp.printStackTrace();
				}
			});
			phaser.register();
			t.start();
		}
	}
}
