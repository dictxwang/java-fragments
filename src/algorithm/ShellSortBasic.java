package algorithm;

import java.util.Objects;

/**
 * 希尔排序是插入排序的变种，关键在于步长的变化
 * 
 * @author wangqiang103
 *
 */
public class ShellSortBasic {
	
	public static void main(String[] args) {

		int[] lst = {0, -6, 90, 10, 10, 3, 2, 3, -1, 20};
		shellSort(lst);
		for (int val : lst) {
			System.out.println(val);
		}
	}


	private static void shellSort(int[] lst) {
		
		if (Objects.isNull(lst) || lst.length <= 1) {
			return;
		}
		
		int gap = lst.length / 2;
		
		while (gap > 0) {
			for (int right = gap; right < lst.length; right += gap) {
				int swapIndex = right;
				int swapValue = lst[right];
				for (int left = right - gap; left >= 0; left -= gap) {
					if (lst[left] > swapValue) {
						lst[left + gap] = lst[left];
						swapIndex = left;
					}
				}
				
				lst[swapIndex] = swapValue;
			}
			
			gap = gap / 2;
		}
	}
}
