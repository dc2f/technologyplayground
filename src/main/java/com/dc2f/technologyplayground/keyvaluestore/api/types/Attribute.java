package com.dc2f.technologyplayground.keyvaluestore.api.types;

import com.dc2f.technologyplayground.keyvaluestore.api.AttributeKey;

public interface Attribute {
	AttributeKey getKey();
	Object getValue();
}
