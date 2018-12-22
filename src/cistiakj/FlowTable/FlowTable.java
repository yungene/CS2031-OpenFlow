package cistiakj.FlowTable;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 * @param <Entry>
 */
public class FlowTable<Entry> {

	// maps (dest,src) -> table entry
	// dest and src are ints, e.g. can be port numbers if on localhost or ip adresses
	Map<Integer, Map<Integer, Entry>> flowTable;

	public FlowTable() {
		flowTable = new HashMap<>();
	}

	public Entry getEntry(int dest, int src) {
		if (flowTable.containsKey(dest)) {
			if (flowTable.get(dest).containsKey(src)) {
				return flowTable.get(dest).get(src);
			}
		}
		return null;
	}

	public void addEntry(int dest, int src, Entry e) {
		if (!flowTable.containsKey(dest)) {
			flowTable.put(dest, new HashMap<>());
		}
		flowTable.get(dest).put(src, e);
	}

}
