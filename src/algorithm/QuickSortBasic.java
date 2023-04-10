package algorithm;

import java.util.Objects;

public class QuickSortBasic {

	public static void main(String[] args) {

		int[] lst = {0, -6, 90, 10, 10, 3, 2, 3, -1, 20};
		quickSort(lst);
		for (int val : lst) {
			System.out.println(val);
		}
	}


	private static void quickSort(int[] lst) {
		doQuickSort(lst, 0, lst.length - 1);
	}


	private static void doQuickSort(int[] lst, int fromIndex, int endIndex) {
		
		if (Objects.isNull(lst) || lst.length <= 1) {
			return;
		} else if (fromIndex < 0 || fromIndex >= endIndex) {
			return;
		}
		
		int middleIndex = (endIndex - fromIndex + 1) / 2 + fromIndex;
		int middleValue = lst[middleIndex];
		for (int i = fromIndex; i <= endIndex; i++) {
			if (lst[i] < middleValue && i > middleIndex) {
				// 需要把小于中间值的元素向左移动
				// 相当于首先把中间值到当前值前一位整体右移，然后用当前值填充原中间值位置
				int swap = lst[i];
				for (int j = i; j > middleIndex; j--) {
					lst[j] = lst[j - 1];
				}
				lst[middleIndex] = swap;
				middleIndex += 1;
			} else if (lst[i] > middleValue && i < middleIndex) {
				// 需要把大于中间值的元素向右移动
				// 相当于是当前值后一位到中间值整体左移，然后用当前值填充原中间值位置
				int swap = lst[i];
				for (int j = i; j < middleIndex; j++) {
					lst[j] = lst[j + 1];
				}
				lst[middleIndex] = swap;
				middleIndex -= 1;
				// 因为整体左移一位，需要将下标回拨一位
				i -= 1;
			}
		}
		// 递归分别对左子序列和右子序列排序
		doQuickSort(lst, fromIndex, middleIndex - 1);
		doQuickSort(lst, middleIndex + 1, endIndex);
	}

}
