package com.superum.db.lesson;

public class TimeConverter {

	public static byte hour(short time) {
		return (byte) (time >> 6);
	}
	
	public static byte minute(short time) {
		return (byte) (time & 63);
	}
	
	public static short time(byte hour, byte minute) {
		return (short) ((hour << 6) + minute);
	}

}
