package cistiakj;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 *         A class that represents a simple weighted directed graph
 */
public class Graph<V extends Comparable<V>> {

	// u-v => u->(v,w[u-v])
	Map<V, ArrayList<Pair<V, Integer>>> adj; // adjacency list
	Map<Pair<V, V>, Integer> weight; // weight function for edge (u,v)

	public Graph() {
		adj = new HashMap<>();
		weight = new HashMap<>();
	}

	public void addVertex(V u) {
		if (!adj.containsKey(u)) {
			adj.put(u, new ArrayList<>());
		}

	}
	
	public Set<V> V(){
		return adj.keySet();
	}

	public void addEdge(V u, V v, int w) {
		addVertex(u);
		addVertex(v);
		adj.get(u).add(new Pair<V, Integer>(v, w));
	}

	public void addUndirectedEdge(V u, V v, int w) {
		addEdge(u, v, w);
		addEdge(v, u, w);
	}

	public void removeEdge(V u, V v) {
		if (adj.containsKey(u)) {
			ArrayList<Pair<V, Integer>> arr = adj.get(u);
			int size = arr.size();
			for (int i = 0; i < size; i++) {
				Pair<V, Integer> p = arr.get(i);
				if (v.compareTo(p.getLeft()) == 0) {
					arr.remove(i);
					i--;
					size--;
				}
			}
		}
	}

	public boolean contains(V v) {
		return adj.containsKey(v);
	}

	public boolean containsEdge(V u, V v) {
		if (adj.containsKey(u)) {
			ArrayList<Pair<V,Integer>> list = adj.get(u);
			for(int i = 0; i< list.size(); i++) {
				if(list.get(i).getLeft().equals(v)) {
					return true;
				}
			}
		}
		return false;
	}

	
	//TODO: imporve efficiency
	public HashMap<V, V> dijkstraParents(V start) {
		HashMap<V, V> parents = new HashMap<>();
		HashMap<V, Integer> dist = new HashMap<>();
		for (V v : adj.keySet()) {
			parents.put(v, v);
			if(v.equals(start)){
				dist.put(v,0);
			}else {
				dist.put(v, Integer.MAX_VALUE);
			}
		}
		// min queue
		PriorityQueue<V> queue = new PriorityQueue<>(new Comparator<V>() {
			@Override
			public int compare(V arg0, V arg1) {
				return dist.get(arg0) - dist.get(arg1);
			}

		});
		queue.addAll(adj.keySet());

		while (!queue.isEmpty()) {

			V v = queue.poll();

			ArrayList<Pair<V, Integer>> adjVertices = adj.get(v);
			for (Pair<V, Integer> pair : adjVertices) {
				// relax the edge
				V u = pair.getLeft();
				int w = pair.getRight();
				if (queue.contains(u) && dist.get(u) > dist.get(v) + w) {
					queue.remove(u);
					dist.put(u, dist.get(v) + w);
					parents.put(u, v);
					queue.add(u);
				}
			}
		}

		return parents;
	}

}
