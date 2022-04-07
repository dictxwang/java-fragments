package concurrent;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * 使用Exchanger在两个线程间进行信息传递
 * 
 * @author wangqiang
 *
 */
public class ExchangerBasic {

	public static void main(String[] args) {
		
		final Exchanger<String> exchanger = new Exchanger<>();
		final Random rand = new Random();
		
		new Thread(() -> {
			try {
				String value = "tContent-1";
				TimeUnit.SECONDS.sleep(rand.nextInt(5));
				String exchanged = exchanger.exchange(value);
				System.out.println("task 1 get exchanged: " + exchanged);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				String value = "tContent-2";
				TimeUnit.SECONDS.sleep(rand.nextInt(5));
				String exchanged = exchanger.exchange(value);
				System.out.println("task 2 get exchanged: " + exchanged);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}).start();
	}
}
