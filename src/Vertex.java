
/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 */
public class Vertex implements Comparable<Vertex> {
	
	private int id;

	@Override
	public int compareTo(Vertex that) {
		return this.id - that.getId();
	}
	
	public int getId() {
		return id;
	}

}
