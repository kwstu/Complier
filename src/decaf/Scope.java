package decaf;

import java.util.Hashtable;

class Scope extends Hashtable<String, ScopeData> {
	final Scope parent;
	public Scope(Scope parent) {
		this.parent = parent;
	}
	boolean isDeclared(String varName) {
		if(super.contains(varName)) {
			return true;
		}
		return parent == null ? false : parent.isDeclared(varName);
	}
}