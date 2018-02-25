package org.kkonoplev.bali.classifyreport;

public class AnchorA extends BaseA {
	
	public AnchorA(String newVar, String str) {
		super(str);
		this.newVar = newVar;
	}
	
	public String newVar = "";
	
	public String getNewVar() {
		return newVar;
	}

	public void setNewVar(String newVar) {
		this.newVar = newVar;
	}

	
}
