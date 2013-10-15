package com.dc2f.technologyplayground.keyvaluestore.api;

import com.dc2f.technologyplayground.keyvaluestore.api.types.Attribute;
import com.dc2f.technologyplayground.keyvaluestore.api.types.List;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;

public interface TypedKeyValueStore {
	public Attribute getAttribute(String absolutePath);
	public Attribute getAttribute(Node basenode, String relativePath);
	public List getList(String absolutePath);
	public List getList(Node basenode, String relativePath);
	public Node getNode(String path);
	public Node getNode(Node basenode, String relativePath);
	
}
