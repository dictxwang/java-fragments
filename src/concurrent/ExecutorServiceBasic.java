package concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ExecutorServiceBasic {

	public static void main(String[] args) {

		ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(1),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.CallerRunsPolicy());  // 拒绝策略：由提交线程自主执行任务
		List<Future<Integer>> futureList = new ArrayList<>();
		
		for (int i = 1; i <= 5; i++) {
			Future<Integer> future = executorService.submit(() -> {
				TimeUnit.SECONDS.sleep(2);
				return null;
			});
			futureList.add(future);
		}
		
		for (Future<Integer> future : futureList) {
			try {
				System.out.println(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		executorService.shutdown();
	}
	
}
