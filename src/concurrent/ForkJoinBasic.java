package concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 通过Fork/Join框架统计单词的数量
 * 类似Map-Reduce实现
 * 
 * @author wangqiang
 *
 */
public class ForkJoinBasic {

	public static void main(String[] args) throws Exception {
		
		int count = 10_000_000;
		List<String> words = generateWords(count);
		
		// 采用Fork/Join方式
		long st = System.currentTimeMillis();
		WordsCountTask task = new WordsCountTask(words, 0, count - 1);
		ForkJoinPool fjp = new ForkJoinPool();
		ForkJoinTask<Map<String, Integer>> fjt = fjp.submit(task);
		Map<String, Integer> result = fjt.get();
		long cost = System.currentTimeMillis() - st;
		int totalCount = 0;
		for (Map.Entry<String, Integer> entry : result.entrySet()) {
			System.out.printf("%s -> %d\n", entry.getKey(), entry.getValue());
			totalCount += entry.getValue();
		}
		
		fjp.shutdown();
		System.out.printf("Fork/Join total count: %d, cost %dms\n", totalCount, cost);
		
		// 采用单线程计算方式
		st = System.currentTimeMillis();
		Map<String, Integer> resultNormal = new HashMap<String, Integer>();
		for (String c : words) {
			int val = 1;
			if (resultNormal.containsKey(c)) {
				val += resultNormal.get(c);
			}
			resultNormal.put(c, val);
		}
		cost = System.currentTimeMillis() - st;
		totalCount = 0;
		for (Map.Entry<String, Integer> entry : resultNormal.entrySet()) {
			System.out.printf("%s -> %d\n", entry.getKey(), entry.getValue());
			totalCount += entry.getValue();
		}
		System.out.printf("Normal total count: %d, cost %dms\n", totalCount, cost);
	}
	
	private static List<String> generateWords(int count) {
		List<String> result = new ArrayList<>();
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			char c = (char)(97 + rand.nextInt(15));
			result.add(String.valueOf(c));
		}
		return result;
	}

	private static class WordsCountTask extends RecursiveTask<Map<String, Integer>> {
		
		// 子任务切分阈值设置比较重要
		private static final int THRESHOLD = 10000;
		private List<String> words;
		private int from;
		private int end;
		
		public WordsCountTask(List<String> words, int from, int end) {
			this.words = words;
			this.from = from;
			this.end = end;
		}
 
		@Override
		protected Map<String, Integer> compute() {
			if (this.end - this.from <= THRESHOLD) {
				Map<String, Integer> result = new HashMap<String, Integer>();
				for (int i = this.from; i <= this.end; i++) {
					result.compute(this.words.get(i), (k, v) -> {
						if (v == null) {
							v = 1;
						} else {
							v += 1;
						}
						return v;
					});
				}
				return result;
			} else {
				int middle = this.from + (this.end - this.from) / 2;
				WordsCountTask left = new WordsCountTask(this.words, this.from, middle);
				WordsCountTask right = new WordsCountTask(this.words, middle + 1, this.end);
				left.fork();
				right.fork();
				Map<String, Integer> leftResult = left.join();
				Map<String, Integer> rightResult = right.join();
				for (Map.Entry<String, Integer> rightEntry : rightResult.entrySet()) {
					String key = rightEntry.getKey();
					if (leftResult.containsKey(rightEntry.getKey())) {
						leftResult.put(key, leftResult.get(key) + rightEntry.getValue());
					} else {
						leftResult.put(key, rightEntry.getValue());
					}
				}
				return leftResult;
			}
		}
		
	}
}
