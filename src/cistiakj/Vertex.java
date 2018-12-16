package cistiakj;


/**
 * 
 * @author Jevgenijus Cistiakovas 17325426 cistiakj@tcd.ie
 *
 */
public class Vertex implements Comparable<Vertex> {
	
	private int id;
	int dist;
	
	public Vertex(int id, int dist) {
		this.id = id;
		this.dist = dist;
	}

	@Override
	public int compareTo(Vertex that) {
		return this.id - that.getId();
	}
	
	public int getId() {
		return id;
	}

}
