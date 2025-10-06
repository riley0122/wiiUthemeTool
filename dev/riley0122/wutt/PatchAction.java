package dev.riley0122.wutt;

public class PatchAction {
	private int action;
	public long length;
	
	public long offsetData;
	public byte[] data;
	
	public PatchAction(long actionByte) {
		this.action = (int)(actionByte & 3);
		this.length = (actionByte >> 2) + 1;
	}
	
	public PatchAction(long actionByte, byte[] data) {
		this.action = (int)(actionByte & 3);
		this.length = (actionByte >> 2) + 1;
		
		if (this.action != 1) {
	        throw new IllegalArgumentException("Data constructor only for TargetRead (action 1)");
	    }
		this.data = data;
	}
	
	public PatchAction(long actionByte, long offsetData) {
		this.action = (int)(actionByte & 3);
		this.length = (actionByte >> 2) + 1;
		
		this.offsetData = offsetData;
	}
	
	public String getName() {
		switch (this.action) {
		case 0:
			return "SourceRead";
		case 1: 
			return "TargetRead";
		case 2:
			return "SourceCopy";
		case 3:
			return "TargetCopy";
		}
		return "Invalid";
	}
}
