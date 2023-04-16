package agentmainsample;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 先执行这个类的main方法，再执行AgentAttachMain实现agent绑定
 * 
 * @author wangqiang
 *
 */
public class SampleOne {

	public void handleSayHello(String name) throws Exception {
		Random random = new Random();
		TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
		System.out.println(String.format("%s say hello", name));
	}
	
	public static void main(String[] args) throws Exception {
		SampleOne sample = new SampleOne();
		// 这里通过无限循环，便于在中途加载agent
		for (;;) {
			sample.handleSayHello(Thread.currentThread().getName());
			Thread.sleep(10000);
		}
	}

}
