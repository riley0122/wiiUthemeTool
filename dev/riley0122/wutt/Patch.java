package dev.riley0122.wutt;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Patch {
	private byte[] bytes;
	private int offset = 0;
	
	private String magic;
	private long sourceSize;
	private long targetSize;
	private long metadataSize;
	private String metadata;
	
	private PatchAction[] actions;
	
	private long sourceChecksum;
	private long targetChecksum;
	private long pathChecksum;
	
	private long outputOffset = 0;
	private long sourceRelativeOffset = 0;
	private long targetRelativeOffset = 0;
	
	public Patch(byte[] bytes) {
		this.bytes = bytes;
		
		this.checkHeader();
		this.sourceSize = this.decodeNumber();
		this.targetSize = this.decodeNumber();
		this.metadataSize = this.decodeNumber();
		Main.log("Source size: " + this.sourceSize + "; Target size: " + this.targetSize + "; Metadata size: " + this.metadataSize, Main.LogLevel.INFO);
		
		this.metadata = this.readMetadata();
		this.actions = this.readActions();
	}
	
	private void checkHeader() {
		char[] bytes = {
				(char) this.readByte(),
				(char) this.readByte(),
				(char) this.readByte(),
				(char) this.readByte()
		};
		
		this.magic = String.valueOf(bytes);
		
		if (!this.magic.equals("BPS1")) {
			Main.log("Invalid BPS file: expected BPS1, got " + this.magic, Main.LogLevel.FATAL);
		}
	}
	
	private byte readByte() {
		byte b = (byte) (this.bytes[offset++] & 0xFF);
		Main.log("Read byte: " + b + " - " + (char) b, Main.LogLevel.BYTE);
		return b;
	}
	
	private long decodeNumber() {
		long data = 0;
		long shift = 1;
		
		while (true) {
			int x = this.readByte();
			data += (x & 0x7f) * shift;
			if((x & 0x80) != 0) break;
			shift <<= 7;
			data += shift;
		}
		
		Main.log("Read number: " + data, Main.LogLevel.DEBUG);
		
		return data;
	} 
	
	private String readMetadata() {
		if (this.metadataSize == 0) {
			return "";
		}
		
		ArrayList<Character> data = new ArrayList<Character>();
		
		for (int i = 0; i < this.metadataSize; i++) {
			data.add((char) this.readByte());
		}
				
		String metadata = data.stream().map(String::valueOf).collect(Collectors.joining());
		Main.log("Read metadata: " + metadata, Main.LogLevel.DEBUG);
		return metadata;
	}
	
	private PatchAction[] readActions() {
		ArrayList<PatchAction> actions = new ArrayList<PatchAction>();
		
		// - 12, because that's the size of the footer according to spec.
		while (offset < this.bytes.length - 12) {
			long data = this.decodeNumber();
			PatchAction action = new PatchAction(data);
			Main.log("Found action: " + action.getName(), Main.LogLevel.DEBUG);
			actions.add(action);
		}
		
		Main.log("Found " + actions.size() + " actions.", Main.LogLevel.INFO);
		return actions.toArray(new PatchAction[0]);
	}
}
