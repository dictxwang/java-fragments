package algorithm;

/**
 * 最大堆的实现
 * 数组表示法：
 * 	当前节点下标*2+1是其左子节点下标，当前节点下标*2+2是其右子节点下标
 * @author wangqiang103
 */
public class MaximumHeapBasic {
	
	private int[] data;
	private int count;
	
	public MaximumHeapBasic(int[] data) {
		this.data = data;
		this.count = data.length;
		
		for (int i = this.count - 1; i >= 0; i--) {
			this.siftDown(i);
		}
	}
	
	public void print() {
		for (int v : this.data) {
			System.out.printf("%d ", v);
		}
	}
	
	private void siftDown(int currentIndex) {
		
		int latest = currentIndex;
		int left = currentIndex * 2 + 1;
		int right = currentIndex * 2 + 2;
		
		if (left < this.count && this.data[left] > this.data[latest]) {
			latest = left;
		}
		if (right < this.count && this.data[right] > this.data[latest]) {
			latest = right;
		}
		
		if (currentIndex != latest) {
			// 将最大值移到堆顶
			int swap = this.data[currentIndex];
			this.data[currentIndex] = this.data[latest];
			this.data[latest] = swap;
			this.siftDown(latest);
		}
	}

	public static void main(String[] args) {
		int data[] = {10, 20, 9, 4, 5, 30, 2, 2, -10, 50, 100, 340};
		MaximumHeapBasic heap = new MaximumHeapBasic(data);
		heap.print(); // 340 100 30 4 50 10 2 2 -10 20 5 9 
	}

}