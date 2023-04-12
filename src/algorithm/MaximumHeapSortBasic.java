package algorithm;

/**
 * 利用最大堆排序
 * 
 * @author wangqiang103
 *
 */
public class MaximumHeapSortBasic {
	
	private static void heapSort(int[] list) {
		
		// 首先完成最大堆的构建
		for (int i = list.length - 1; i >= 0; i--) {
			siftDown(list, i, list.length);
		}
		
		// 通过遍历并重建最大堆实现排序
		for (int i = list.length - 1; i > 0; i--) {
			// 将当前最大元素往后移，然后重建最大堆
			int swap = list[i];
			list[i] = list[0];
			list[0] = swap;
			siftDown(list, 0, i);
		}
	}
	
	
	private static void siftDown(int[] list, int currentIndex, int endIndex) {
		
		int latest = currentIndex;
		int left = latest * 2 + 1;
		int right = latest * 2 + 2;
		
		if (left < endIndex && list[left] > list[latest]) {
			latest = left;
		}
		if (right < endIndex && list[right] > list[latest]) {
			latest = right;
		}
		
		if (currentIndex != latest) {
			int swap = list[currentIndex];
			list[currentIndex] = list[latest];
			list[latest] = swap;
			siftDown(list, latest, endIndex);
		}
	}

	
	public static void main(String[] args) {
		int data[] = {2, 1, 7, -10, 6, 200, -12, 340, 20, -12, 340};
		heapSort(data);
		
		// -12 -12 -10 1 2 6 7 20 200 340 340 
		for (int v : data) {
			System.out.printf("%d ", v);
		}
	}
}
