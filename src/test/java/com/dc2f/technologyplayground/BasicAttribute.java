package com.dc2f.technologyplayground;

import com.dc2f.technologyplayground.keyvaluestore.api.AttributeKey;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Attribute;

public class BasicAttribute implements Attribute {
	private final Object value;
	private final AttributeKey key;
	
	public BasicAttribute(AttributeKey key, String value) {
		this.value = value;
		this.key = key;
	}
	@Override
	public AttributeKey getKey() {
		return key;
	}
	@Override
	public Object getValue() {
		return value;
	}
}
