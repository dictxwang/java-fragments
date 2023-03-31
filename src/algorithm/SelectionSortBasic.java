package algorithm;

import java.util.Objects;

public class SelectionSortBasic {

	public static void main(String[] args) {

		int[] lst = {10, 0, 2, -1, 300, 20};
		selectionSort(lst);
		for (int val : lst) {
			System.out.println(val);
		}
	}
	
	private static void selectionSort(int[] lst) {
		
		if (Objects.isNull(lst) || lst.length <= 1) {
			return;
		}
		for (int left = 0; left < lst.length - 1; left++) {
			int swapIndex = left;
			for (int right = left + 1; right < lst.length; right++) {
				if (lst[right] < lst[left]) {
					swapIndex = right;
				}
			}
			if (swapIndex != left) {
				int swap = lst[swapIndex];
				lst[swapIndex] = lst[left];
				lst[left] = swap;
			}
		}
	}
}
