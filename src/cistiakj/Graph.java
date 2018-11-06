package cistiakj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 * represents a simple weighted directed graph
 */
public class Graph<V extends Comparable<V>> {

	Map<V, ArrayList<Pair<V,Integer>>> adj;	//adjacency list
	Map<Pair<V,V>, Integer> weight;	//weight function for edge (u,v)
	
	public Graph() {
		adj = new HashMap<>();
		weight = new HashMap<>();
	}
	
	public void addVertex(V u) {
		if(!adj.containsKey(u)) {
			adj.put(u, new ArrayList<>());
		}
	}
	
	public void addEdge(V u, V v, int w) {
		addVertex(u);
		adj.get(u).add(new Pair<V,Integer>(v,w));
	}
	
	public void addUndirectedEdge(V u, V v,int w) {
		addEdge(u,v,w);
		addEdge(v,u,w);
	}
	
	public void removeEdge(V u, V v) {
		if(adj.containsKey(u)) {
			ArrayList<Pair<V,Integer>> arr = adj.get(u);
			int size = arr.size();
			for(int i = 0; i < size; i++) {
				Pair<V, Integer> p = arr.get(i);
				if(v.compareTo(p.getLeft()) == 0) {
					arr.remove(i);
					i--;
					size--;
				}
			}
		}
	}
	
	
	
}
