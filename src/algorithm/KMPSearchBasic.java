package algorithm;

import java.util.LinkedList;
import java.util.List;

public class KMPSearchBasic {

	public static void main(String[] args) {
		
		String pattern = "ABCDABCD";
		int[] next = generateNextTable(pattern);
		for (int i : next) {
			System.out.printf("%d ", i);
		}
		System.out.println();
		
		String source = "BBC ABCDAB ABCDABCDABDE";
		List<Integer> result = kmpSearch(source, pattern);
		for (int index : result) {
			System.out.println(index);
		}
	}

	/**
	 * 生成next表
	 * 这里next表相当于PMT部分匹配表（Partial Match Table）整体后移一位
	 * @param pattern
	 * @return
	 */
	private static int[] generateNextTable(String pattern) {
		
		int[] next = new int[pattern.length()];
		for (int i = 0; i < pattern.length(); i++) {
			next[i] = -1;
		}
		int i = 0, j = -1;
		while (true) {
			if (i >= pattern.length() - 1) {
				break;
			}
			if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
				// 当前字符相等，向后继续匹配
				i += 1;
				j += 1;
				next[i] = j;
			} else {
				// 当前字符不相等，从头开始匹配
				j = -1;
			}
		}
		return next;
	}
	
	private static List<Integer> kmpSearch(String source, String pattern) {
		
		// 先生成next表
		int[] next = generateNextTable(pattern);
		int i = 0, j = 0;
		List<Integer> result = new LinkedList<>();
		
		while (true) {
			if (i >= source.length()) {
				break;
			}
			if (j == -1 || source.charAt(i) == pattern.charAt(j)) {
				i += 1;
				j += 1;
			} else {
				j = next[j];
			}
			if (j == pattern.length()) {
				result.add(i - j);
				// 开启新一轮的匹配
				j = 0;
			}
		}
		return result;
	}
}
