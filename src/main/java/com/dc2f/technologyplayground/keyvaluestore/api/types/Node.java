package com.dc2f.technologyplayground.keyvaluestore.api.types;

import java.io.Serializable;

import com.dc2f.technologyplayground.keyvaluestore.api.AttributeKey;

public interface Node extends Serializable {

	void addAttribute(Attribute attribute);
	
	Attribute getAttribute(AttributeKey key);

}
