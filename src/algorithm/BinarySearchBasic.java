package algorithm;

import java.util.Objects;

public class BinarySearchBasic {

	public static void main(String[] args) {
		
		int[] lst = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20};
		
		System.out.println(binarySearch(lst, 0, 0, lst.length - 1));
		System.out.println(binarySearch(lst, 1, 0, lst.length - 1));
		System.out.println(binarySearch(lst, 3, 0, lst.length - 1));
		System.out.println(binarySearch(lst, 20, 0, lst.length - 1));
		System.out.println(binarySearch(lst, 6, 0, lst.length - 1));
		System.out.println(binarySearch(lst, -10, 0, lst.length - 1));
		System.out.println(binarySearch(lst, 100, 0, lst.length - 1));
	}

	private static int binarySearch(int[] lst, int pattern, int fromIndex, int endIndex) {
		
		if (Objects.isNull(lst) || endIndex < fromIndex) {
			return -1;
		}
		
		int middleIndex = (endIndex - fromIndex + 1) / 2 + fromIndex;
		if (lst[middleIndex] == pattern) {
			return middleIndex;
		} else if (lst[middleIndex] > pattern) {
			// 继续在左子序列查找
			return binarySearch(lst, pattern, fromIndex, middleIndex - 1);
		} else {
			// 继续在右子序列查找
			return binarySearch(lst, pattern, middleIndex + 1,  endIndex);
		}
	}
}
