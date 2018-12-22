package cistiakj;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A class that represents a pair of values.
 * @param <L>
 * @param <R>
 */
public class Pair<L extends Comparable<L>,R> implements Comparable<Pair<L,R>>{
	private L left;
	private R right;
	
	public Pair(L val1, R val2){
		left = val1;
		right = val2;
	}
	
	
	public L getLeft() {
		return left;
	}
	
	public R getRight() {
		return right;
	}
	
	public void addLeft(L val) {
		left = val;
	}
	
	public void addRight(R val) {
		right = val;
	}
	
	//compare left sides only
	public boolean equals(Pair<L,R> that) {
		return that.getLeft().equals(left);
	}


	@Override
	public int compareTo(Pair<L,R> that) {
		return that.getLeft().compareTo(left);
	}
	
	
}
