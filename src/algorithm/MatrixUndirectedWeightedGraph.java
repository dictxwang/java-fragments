package algorithm;

/**
 * 使用邻接矩阵实现无向带权图
 * 
 * @author wangqiang103
 *
 */
public class MatrixUndirectedWeightedGraph {

	private int size;
	private char[] vertexs;
	private int[][] matrix;
	
	
	public MatrixUndirectedWeightedGraph(char[] vertexs) {
		this.size = vertexs.length;
		this.vertexs = vertexs;
		this.matrix = new int[this.size][this.size];
	}
	
	
	public void setEdge(char f, char s, int weight) {
		int findex = this.getPosition(f);
		int sindex = this.getPosition(s);
		if (findex >= 0 && sindex >= 0) {
			this.matrix[findex][sindex] = weight;
			this.matrix[sindex][findex] = weight;
		}
	}
	
	
	private int getPosition(char ch) {
		for (int i = 0; i < this.size; i++) {
			if (this.vertexs[i] == ch) {
				return i;
			}
		}
		return -1;
	}
	
	
	private void print() {
		for (int i = 0; i < this.size; i++) {
			for (int v : this.matrix[i]) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		char[] vertexs = {'A', 'B', 'C', 'D', 'E', 'F'};
		MatrixUndirectedWeightedGraph mudwg = new MatrixUndirectedWeightedGraph(vertexs);
		mudwg.setEdge('A', 'B', 15);
		mudwg.setEdge('A', 'F', 3);
		mudwg.setEdge('B', 'C', 2);
		mudwg.setEdge('C', 'D', 5);
		mudwg.setEdge('D', 'E', 4);
		mudwg.setEdge('F', 'B', 10);
		mudwg.print();
	}

}
