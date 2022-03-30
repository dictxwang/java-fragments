package gc;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 测试G1运行情况，通过导出带main方法的jar包执行
 * @author wangqiang
 * 执行命令示例： 
 * 	java -XX:+UseG1GC -Xmx3m -Xms3m -XX:+PrintGCDetails -jar gc-basic.jar weak 50
 * 		说明：-Xms最小1m，因为g1模式下，region最小是1m
 * 	java -XX:+UseG1GC -Xmx20m -Xms20m -XX:+PrintGCDetails -XX:+UnlockExperimentalVMOptions -XX:G1MaxNewSizePercent=10 -XX:InitiatingHeapOccupancyPercent=20 -Xloggc:gc-log.txt -jar gc-basic.jar weak 50
 * 		说明：
 * 			-XX:+PrintGCDetails 打印详细的gc信息
 * 			-XX:+UnlockExperimentalVMOptions 解锁实验性的参数
 * 			-XX:G1MaxNewSizePercent=10 配置年轻代占比
 * 			-XX:InitiatingHeapOccupancyPercent=20 设置触发标记周期的java堆占用率，默认值是45（调小这个值有助于避免fullgc）
 * 			-Xloggc:gc-log.txt 输出gc日志到文件中
 */

public class GcBasic {

	public static void main(String[] args) throws Exception {
		String mapFlag = "strong";
		if (args.length > 0) {
			mapFlag = args[0];
		}
		// default 5k
		int capacity = 5;
		if (args.length > 1) {
			capacity = Integer.valueOf(args[1]);
		}
		System.out.printf("use '%s' map, and allocate %dk per time\n", mapFlag, capacity);
		Map<String, ByteBuffer> map = null;
		if ("strong".equals(mapFlag)) {
			// 内存不够时不会被回收，会引发oom
			map = new HashMap<>();
		} else if ("weak".equals(mapFlag)) {
			// 每次gc就会被回收，不会引发oom
			// 但是如果参数配置不正确，会造成gc阶段没有足够的空间用于对象复制，从而引发fullGC甚至oom
			map = new WeakHashMap<>();
		}
		int count = 0;
		while (true) {
			long ts = System.currentTimeMillis();
			map.put(UUID.randomUUID().toString(), ByteBuffer.allocate(capacity * 1024));
			int size = map.size();
			System.out.printf("%d, %d, %d\n", ++count, size, ts);
			TimeUnit.SECONDS.sleep(1);
		}
	}

}
