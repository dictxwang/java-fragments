package algorithm;

public class BubbleSortBasic {

	public static void main(String[] args) {

		int[] lst = {0, -6, 90, 10, 10, 3, 2, 3, -1, 20};
		bubbleSort(lst);
		for (int val : lst) {
			System.out.println(val);
		}
	}
	
	
	private static void bubbleSort(int[] lst) {
		
		for (int outer = 0; outer < lst.length - 1; outer++) {
			for (int inner = lst.length - 1; inner > outer; inner--) {
				if (lst[inner] < lst[inner - 1]) {
					int swap = lst[inner];
					lst[inner] = lst[inner - 1];
					lst[inner - 1] = swap;
				}
			}
		}
	}

}
