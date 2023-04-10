package algorithm;

import java.util.Objects;

public class BubbleSortBasic {

	public static void main(String[] args) {

		int[] lst = {0, -6, 90, 10, 10, 3, 2, 3, -1, 20};
		bubbleSort(lst);
		for (int val : lst) {
			System.out.println(val);
		}
	}


	private static void bubbleSort(int[] lst) {
		
		if (Objects.isNull(lst) || lst.length <= 1) {
			return;
		}
		for (int i = 0; i <= lst.length - 1; i++) {
			for (int j = lst.length - 1; j > i; j--) {
				if (lst[j] < lst[j - 1]) {
					int swap = lst[j];
					lst[j] = lst[j - 1];
					lst[j -1] = swap;
				}
			}
		}
	}

}
