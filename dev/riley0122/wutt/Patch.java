package dev.riley0122.wutt;

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
	
	public Patch(byte[] bytes) {
		this.bytes = bytes;
		
		this.checkHeader();
	}
	
	private void checkHeader() {
		byte[] bytes = {
				this.readByte(),
				this.readByte(),
				this.readByte(),
				this.readByte()
		};
		
		this.magic = bytes.toString();
		
		if (!this.magic.equals("BPS1")) {
			Main.log("Invalid BPS file: expected BPS1, got " + magic, Main.LogLevel.FATAL);
		}
	}
	
	private byte readByte() {
		byte b = (byte) (this.bytes[offset++] & 0xFF);
		Main.log("Read byte: " + b, Main.LogLevel.DEBUG);
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
		
		return data;
	} 
}
