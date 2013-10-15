package com.dc2f.technologyplayground.keyvaluestore.api;

import com.dc2f.technologyplayground.keyvaluestore.api.types.Attribute;
import com.dc2f.technologyplayground.keyvaluestore.api.types.List;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;

public interface TypedKeyValueStore {
	public Attribute getAttribute(Key key);
	public List getList(Key key);
	public Node getNode(Key key);
	
}
