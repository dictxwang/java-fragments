package bytebuddy;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 模拟待拦截的业务类
 * @author wangqiang
 */
public class ByteBuddyService {
	
	private static Random random = new Random();

	public void handleX() throws Exception {
		TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
		System.out.println("handleX execute");
	}

	public String handleX(String name) throws Exception {
		TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
		System.out.println("handleX execute, name " + name);
		return "ThisIsMethod:handleX";
	}

	// final方法不能被拦截
	public final void handleY() throws Exception {
		TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
		System.out.println("handleY execute");
	}
	
	// 静态方法不会被拦截
	public static void handleS() throws Exception {
		TimeUnit.MILLISECONDS.sleep(random.nextInt(3000));
		System.out.println("handleS execute");
	}
}
