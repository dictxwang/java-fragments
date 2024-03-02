package nio;

import java.nio.channels.Selector;

/**
 * Java Selector在Windows平台和Unix平台的实现是不同的：
 * 1、Windows平台上每个Selector会建立一对回环链接（lookback connection）
 * 2、Unix平台上每个Selector会建立一对管道（pipe）
 * 这样做的方式是为了实现当有数据到达时，通过朝lookback或者pipe发送一点信息，唤醒阻塞在select上的线程
 * */
public class SelectorBasic {

	private static final int MAX_SIZE = 5;
	
	public static void main(String[] args) {
		
		Selector[] sels = new Selector[MAX_SIZE];
		
		try {
			for (int i = 0; i < MAX_SIZE; i++) {
				sels[i] = Selector.open();
			}
			Thread.sleep(3600000);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
