package algorithm;

import java.util.Objects;

public class QuickSortBasic {

	public static void main(String[] args) {

		int[] lst = {0, -6, 90, 10, 3, 2, -1, 20};
		quickSort(lst, 0, lst.length - 1);
		for (int val : lst) {
			System.out.println(val);
		}
	}
	
	private static void quickSort(int[] lst, int fromIndex, int endIndex) {
		
		if (Objects.isNull(lst) || lst.length <= 1) {
			return;
		} else if (fromIndex < 0 || fromIndex >= endIndex) {
			return;
		}
		
//		System.out.printf("%d=>%d\n", fromIndex, endIndex);
		
		int middleIndex = (endIndex - fromIndex + 1) / 2 + fromIndex;
		int middleValue = lst[middleIndex];
		for (int i = fromIndex; i <= endIndex; i++) {
			if (lst[i] < middleValue && i > middleIndex) {
				// 需要把小于中间值的元素向左移动
				int swap = lst[i];
				for (int j = i; j > middleIndex; j--) {
					lst[j] = lst[j - 1];
				}
				lst[middleIndex] = swap;
				middleIndex += 1;
			} else if (lst[i] > middleValue && i < middleIndex) {
				// 需要把大于中间值的元素向右移动
				int swap = lst[i];
				for (int j = i; j < middleIndex; j++) {
					lst[j] = lst[j + 1];
				}
				lst[middleIndex] = swap;
				middleIndex -= 1;
				// 因为元素整体左移一位，需要将下标回拨一位
				i -= 1;
			}
		}
		// 递归分别对左子序列和右子序列排序
		quickSort(lst, fromIndex, middleIndex - 1);
		quickSort(lst, middleIndex + 1, endIndex);
	}

}
