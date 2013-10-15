package com.dc2f.technologyplayground.keyvaluestore.persistent;

import java.io.Serializable;

import com.dc2f.technologyplayground.keyvaluestore.api.AttributeKey;
import com.dc2f.technologyplayground.keyvaluestore.api.Key;
import com.dc2f.technologyplayground.keyvaluestore.api.KeyValueStore;
import com.dc2f.technologyplayground.keyvaluestore.api.TypedKeyValueStore;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Attribute;
import com.dc2f.technologyplayground.keyvaluestore.api.types.List;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;

public class PersistentKeyValueStore implements KeyValueStore, TypedKeyValueStore {
	
	
	

	public PersistentKeyValueStore(String storename) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Key put(Serializable element) {
		return null;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object get(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * closes the store and forces it to persist all changes.
	 */
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Attribute getAttribute(Key key) {
		Object object = get(key);
		if (object instanceof Attribute) {
			return (Attribute) object;
		}
		return null;
	}

	@Override
	public List getList(Key key) {
		Object object = get(key);
		if (object instanceof List) {
			return (List) object;
		}
		return null;
	}

	@Override
	public Node getNode(Key key) {
		Object object = get(key);
		if (object instanceof Node) {
			return (Node) object;
		}
		return null;
	}

	public AttributeKey getKeyForAttributeName(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}



}
