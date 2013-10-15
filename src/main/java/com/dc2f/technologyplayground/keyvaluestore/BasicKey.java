package com.dc2f.technologyplayground.keyvaluestore;

import com.dc2f.technologyplayground.keyvaluestore.api.Key;

public class BasicKey implements Key {

	final String checkSum;
	
	public BasicKey(String checkSum) {
		this.checkSum = checkSum;
	}
	
	@Override
	public String toString() {
		return checkSum;
	}

}
