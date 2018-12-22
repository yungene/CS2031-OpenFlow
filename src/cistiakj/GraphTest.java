package cistiakj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A JUnit test to test the methods of Graph class, especially Dijkstra algortihm.
 */
public class GraphTest {

	@Test
	public void testContructor() {
		Graph<Integer> g = new Graph<>();
		assertNotEquals(null, g);
	}
	
	@Test
	public void testAddVertexAndTestContains() {
		Graph<Integer> g = new Graph<>();
		g.addVertex(3);
		assertTrue(g.contains(3));
		g.addEdge(1, 2, 2);
		assertTrue(g.contains(1));
		assertTrue(g.contains(2));	
		assertFalse(g.contains(5));
	}
	
	@Test
	public void testContainsEdge() {
		Graph<Integer> g = new Graph<>();
		g.addEdge(1, 2, 2);
		assertTrue(g.containsEdge(1,2));
	}
	
	@Test
	public void testRemove() {
		Graph<Integer> g = new Graph<>();
		g.addEdge(1, 2, 2);
		assertTrue(g.contains(1));
		assertTrue(g.contains(2));	
		g.removeEdge(1, 2);
		assertFalse(g.containsEdge(1,2));
	}
	
	@Test
	public void testAddUndirectedEdge() {
		Graph<Integer> g = new Graph<>();
		g.addUndirectedEdge(1, 2, 2);
		assertTrue(g.containsEdge(1,2));
		assertTrue(g.containsEdge(2,1));	
	}
	
	@Test
	public void testDijkstra() {
		Graph<Integer> g = new Graph<>();
		g.addUndirectedEdge(1, 2, 2);
		g.addUndirectedEdge(2, 3, 7);
		g.addUndirectedEdge(3, 4, 3);
		g.addUndirectedEdge(2, 5, 2);
		g.addUndirectedEdge(5, 6, 2);
		g.addUndirectedEdge(5, 7, 1);
		g.addUndirectedEdge(1, 7, 6);
		g.addUndirectedEdge(7, 8, 4);
		g.addUndirectedEdge(6, 8, 2);
		g.addUndirectedEdge(3, 6, 3);
		g.addUndirectedEdge(4, 8, 10);
		
		HashMap<Integer,Integer> actualParents = g.dijkstraParents(1);
		HashMap<Integer,Integer> expectedParents = new HashMap<>();
		expectedParents.put(1, 1);
		expectedParents.put(2,1);
		expectedParents.put(5, 2);
		expectedParents.put(3, 2);
		expectedParents.put(7, 5);
		expectedParents.put(6,5);
		expectedParents.put(4,3);
		expectedParents.put(8,6);
		
		for(Integer u: expectedParents.keySet()) {
			assertEquals(String.format("node: %d", u.intValue()),expectedParents.get(u), actualParents.get(u));
		}
		
	}

}
