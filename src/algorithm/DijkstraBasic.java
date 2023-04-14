package algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 迪杰斯特拉算法实现（基于带权无向图）
 * 
 * @author wangqiang103
 *
 */
public class DijkstraBasic {

	
	public static Map<String, PathWeight> dijkstra(MatrixUndirectedWeightedGraph graph, char from) {
		
		Map<String, PathWeight> result = new HashMap<>();
		char current = from;
		Set<String> visited = new HashSet<>();
		
		while (true) {
			
			int row = graph.getPosition(current);
			
			if (current == from) {
				// 如果起始节点，直接将所有邻接节点追加到结果即可
				for (int col = 0; col < graph.getSize(); col++) {
					int w = graph.getMatrix()[row][col];
					char to = graph.getVertexs()[col];
					if (w > 0) {
						PathWeight pw = new PathWeight(current, to, w);
						result.put(makeKey(current, to), pw);
					}
				}
			} else {
				// 获取前缀路径
				String prefixKey = makeKey(from, current);
				PathWeight prefixPW = result.get(prefixKey);

				// 需要和已有路径进行对比，保留其中的最短路径
				for (int col = 0; col < graph.getSize(); col ++) {
					int weight = graph.getMatrix()[row][col];
					char to = graph.getVertexs()[col];
					if (to == from || weight == 0) {
						continue;
					}
					String key = makeKey(from, to);
					PathWeight pwExists = result.getOrDefault(key, null);
					if (pwExists == null || (prefixPW.getWeight() + weight) < pwExists.getWeight()) {
						PathWeight pwNew = prefixPW.copyAndExtend(to, weight);
						result.put(key, pwNew);
					}
				}
			}
			visited.add(String.valueOf(current));
			
			// 从已经访问的路径中，找到路径最短切还未遍历的节点，继续遍历
			char minTo = '\0';
			int minWeigth = 0;
			for (Map.Entry<String, PathWeight> entry : result.entrySet()) {
				String to = entry.getKey().substring(1);
				if (!visited.contains(to)
						&& (minWeigth == 0 || entry.getValue().getWeight() < minWeigth)) {
					minTo = to.charAt(0);
					minWeigth = entry.getValue().getWeight();
				}
			}
			if (minTo == '\0') {
				// 已没有未访问的节点
				break;
			} else {
				current = minTo;
			}
		}
		
		return result;
	}
	
	
	static String makeKey(char from, char to) {
		return String.valueOf(from) + String.valueOf(to);
	}
	
	
	static class PathWeight {
		private List<String> path;
		private int weight;
		
		public PathWeight() {
			this.path = new LinkedList<>();
			this.weight = 0;
		}
		
		public PathWeight(char from, char to, int weight) {
			this.path = new LinkedList<>();
			this.path.add(String.valueOf(from));
			this.path.add(String.valueOf(to));
			this.weight = weight;
		}
		
		/**
		 * 复制并扩展路径权重
		 * 
		 * @param current 当前节点
		 * @param weight 权重
		 * @return
		 */
		public PathWeight copyAndExtend(char current, int weight) {
			PathWeight npw = new PathWeight();
			for (String n : this.path) {
				npw.path.add(n);
			}
			npw.path.add(String.valueOf(current));
			npw.weight = this.weight + weight;
			return npw;
		}

		public List<String> getPath() {
			return path;
		}

		public int getWeight() {
			return weight;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("weight:" + this.weight);
			sb.append(",path:");
			for (int i = 0; i < this.path.size(); i++) {
				sb.append(this.path.get(i));
				if (i < this.path.size() - 1) {
					sb.append("=>");
				}
			}
			return sb.toString();
		}
	}
	
	
	static void printPathWeight(Map<String, PathWeight> pathWeights) {
		System.out.println("============ PathAndWeight ============");
		for (Map.Entry<String, PathWeight> entry : pathWeights.entrySet()) {
			String fromTo = entry.getKey();
			String pathWeight = entry.getValue().toString();
			System.out.printf("%s %s\n", fromTo, pathWeight);
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
		
		Map<String, PathWeight> pathWeights = dijkstra(mudwg, 'A');
		printPathWeight(pathWeights);
	}

}
