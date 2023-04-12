package algorithm;
import java.util.Objects;

public class InsertionSortBasic {

	public static void main(String[] args) {
		
		int[] lst = {10, 0, 2, -1, 300, 20, -10};
		insertionSort(lst);
		for (int val : lst) {
			System.out.println(val);
		}
	}


	private static void insertionSort(int[] lst) {
		
		if (Objects.isNull(lst) || lst.length <= 1) {
			return;
		}
		for (int right = 1; right < lst.length; right++) {
			int swapIndex = right;
			int swapValue = lst[right];
			for (int left = right - 1; left >= 0; left--) {
				if (lst[left] > swapValue) {
					lst[left + 1] = lst[left];
					swapIndex = left;
				}
			}
			lst[swapIndex] = swapValue;
		}
	}
}