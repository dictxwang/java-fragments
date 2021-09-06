package agentpresample;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SampleOne {

	public static void main(String[] args) throws Exception {
		System.out.println("current in main method");
		handleFirst();
		new SampleOne().handleSecond("wangqiang");
	}

	// 测试静态方法
	public static void handleFirst() throws Exception {
		Random random = new Random();
		TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
		System.out.println("this is static method: handleFirst");
	}
	
	// 测试成员方法
	public void handleSecond(String caller) throws Exception {
		Random random = new Random();
		TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
		System.out.println(caller + " call instance method: handleSecond");
	}
}
