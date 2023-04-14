package algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 无向图的邻接表实现
 * 
 * @author wangqiang103
 *
 */
public class ListUndirectedGraph {

	private int size;
	private Vertex[] vertexLists;
	
	class Vertex {
		char ch;
		Vertex next;
		
		Vertex(char ch) {
			this.ch = ch;
		}
		
		void add(char ch) {
			Vertex node = this;
			while (node.next != null) {
				node = node.next;
			}
			node.next = new Vertex(ch);
		}
	}
	
	
	public ListUndirectedGraph(char[] vertexs, char[][] edges) {
		
		this.size = vertexs.length;
		this.vertexLists = new Vertex[this.size];
		
		// 设置邻接表头节点
		for (int i = 0; i < this.size; i++) {
			this.vertexLists[i] = new Vertex(vertexs[i]);
		}
		
		// 存储边信息
		for (char[] pair : edges) {
			int p1 = this.getPosition(pair[0]);
			this.vertexLists[p1].add(pair[1]);
			int p2 = this.getPosition(pair[1]);
			this.vertexLists[p2].add(pair[0]);
		}
	}

	
	private int getPosition(char ch) {
		for (int i = 0; i < this.size; i++) {
			if (this.vertexLists[i].ch == ch) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void print() {
		for (int i = 0; i < this.size; i++) {
			Vertex temp = this.vertexLists[i];
			while (temp != null) {
				System.out.print(temp.ch + " ");
				temp = temp.next;
			}
			System.out.println();
		}
	}
	
	
	public void dfs(char from, List<String> path) {
		
		if (path.contains(String.valueOf(from))) {
			return;
		}
		
		int position = this.getPosition(from);
		path.add(String.valueOf(from));

		Vertex node = this.vertexLists[position];
		while (node.next != null) {
			this.dfs(node.next.ch, path);
			node = node.next;
		}
	}
	
	
	public void bfs(char from, List<String> path) {
		
		if (!path.contains(String.valueOf(from))) {
			path.add(String.valueOf(from));
		}
		
		int position = this.getPosition(from);
		Set<String> children = new HashSet<>();
		
		Vertex node = this.vertexLists[position];
		while (node.next != null) {
			node = node.next;
			if (!path.contains(String.valueOf(node.ch))) {
				path.add(String.valueOf(node.ch));
				children.add(String.valueOf(node.ch));
			}
		}
		
		for (String child : children) {
			this.bfs(child.charAt(0), path);
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
		
		ListUndirectedGraph ludg = new ListUndirectedGraph(vertexs, edges);
		ludg.print();
		
		System.out.println("==============================");

		List<String> dfsPath = new LinkedList<>();
		ludg.dfs('A', dfsPath);
		for (String p : dfsPath) {
			System.out.printf("%s ", p);
		}

		System.out.println("\n==============================");

		List<String> bfsPath = new LinkedList<>();
		ludg.bfs('A', bfsPath);
		for (String p : bfsPath) {
			System.out.printf("%s ", p);
		}
	}

}
