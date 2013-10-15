package com.dc2f.technologyplayground.keyvaluestore.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class DigestHelper {

	public static String checkSum(byte[] bytes) {
		return SHAsum(bytes);
	}
	
	public static String SHAsum(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return byteArray2Hex(md.digest(bytes));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
	}

	private static String byteArray2Hex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
}
