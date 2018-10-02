package org.kkonoplev.bali.suiteexec.mdl;

public class ExecutorsInfo {
	
	private int total;
	private int busy;
	
	public ExecutorsInfo(int total, int busy) {
		super();
		this.total = total;
		this.busy = busy;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBusy() {
		return busy;
	}
	public void setBusy(int busy) {
		this.busy = busy;
	}
	
	

}
