package algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MergeSortBasic {

	public static void main(String[] args) {

		List<Integer> lst = new LinkedList<>();
		lst.add(10);
		lst.add(0);
		lst.add(300);
		lst.add(2);
		lst.add(-1);
		lst.add(300);
		lst.add(20);
		
		List<Integer> result = mergeSort(lst);
		for (int val : result) {
			System.out.println(val);
		}
	}


	private static List<Integer> mergeSort(List<Integer> lst) {
		
		if (Objects.isNull(lst) || lst.size() <= 1) {
			return lst;
		}
		
		int middle = lst.size() / 2;
		List<Integer> left = mergeSort(lst.subList(0, middle));
		List<Integer> right = mergeSort(lst.subList(middle, lst.size()));
		
		int leftIndex = 0, rightIndex = 0;
		List<Integer> result = new LinkedList<>();
		while (leftIndex < left.size() && rightIndex < right.size()) {
			if (left.get(leftIndex) <= right.get(rightIndex)) {
				result.add(left.get(leftIndex));
				leftIndex += 1;
			} else if (right.get(rightIndex) < left.get(leftIndex)) {
				result.add(right.get(rightIndex));
				rightIndex += 1;
			}
		}
		
		for (int i = leftIndex; i < left.size(); i++) {
			result.add(left.get(i));
		}
		for (int i = rightIndex; i < right.size(); i++) {
			result.add(right.get(i));
		}
		return result;
	}

}
