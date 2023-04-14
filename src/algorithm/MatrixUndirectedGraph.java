package algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 无向图的邻接矩阵实现
 * 
 * @author wangqiang103
 *
 */
public class MatrixUndirectedGraph {

	// 图的顶点数
	private int size;
	// 图的定点名
	char[] vertexs;
	// 图的关系矩阵
	int[][] matrix;
	
	
	public MatrixUndirectedGraph(char[] vertexs, char[][] edges) {
		this.size = vertexs.length;
		this.matrix = new int[size][size];
		this.vertexs = vertexs;
		
		// 设置矩阵值
		for (char[] edge : edges) {
			int p1 = this.getPosition(edge[0]);
			int p2 = this.getPosition(edge[1]);
			
			// 无向图，需要同时设置两个方向
			this.matrix[p1][p2] = 1;
			this.matrix[p2][p1] = 1;
		}
	}
	
	
	public void print() {
		for (int[] i : this.matrix) {
			for (int j : i) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
	}
	
	
	private int getPosition(char ch) {
		for (int i = 0; i < this.vertexs.length; i++) {
			if (this.vertexs[i] == ch) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void dfs(char from, List<String> path) {
		
		int row = this.getPosition(from);
		if (!path.contains(String.valueOf(from))) {
			path.add(String.valueOf(from));
			for (int col = 0; col < this.vertexs.length; col++) {
				if (this.matrix[row][col] == 1) {
					char ch = this.vertexs[col];
					this.dfs(ch, path);
				}
			}
		}
	}
	
	
	public void bfs(char from, List<String> path) {
		
		int row = this.getPosition(from);
		if (!path.contains(String.valueOf(from))) {
			path.add(String.valueOf(from));
		}
		
		// 先遍历当前层，并过滤出需要继续遍历下层的点
		Set<String> next = new HashSet<>();
		for (int col = 0; col < this.vertexs.length; col++) {
			if (this.matrix[row][col] == 1) {
				char ch = this.vertexs[col];
				if (!path.contains(String.valueOf(ch))) {
					path.add(String.valueOf(ch));
					next.add(String.valueOf(ch));
				}
			}
		}
		
		for (String ch : next) {
			this.bfs(ch.charAt(0), path);
		}
	}
	
	
	public static void main(String[] args) {
		
		char[] vertexs = {'A', 'B', 'C', 'D', 'E', 'F'};
		char[][] edges = new char[][] {
			{'A', 'B'},
			{'A', 'F'},
			{'B', 'C'},
			{'C', 'D'},
			{'D', 'E'},
		};
		
		MatrixUndirectedGraph mudg = new MatrixUndirectedGraph(vertexs, edges);
		mudg.print();

		System.out.println("==============================");
		
		List<String> dfsPath = new LinkedList<>();
		mudg.dfs('A', dfsPath);
		for (String p : dfsPath) {
			System.out.printf("%s ", p);
		}

		System.out.println("\n==============================");
		
		List<String> bfsPath = new LinkedList<>();
		mudg.bfs('A', bfsPath);
		for (String p : bfsPath) {
			System.out.printf("%s ", p);
		}
	}

}
