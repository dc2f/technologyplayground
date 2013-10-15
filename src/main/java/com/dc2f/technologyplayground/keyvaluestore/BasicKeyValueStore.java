package com.dc2f.technologyplayground.keyvaluestore;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.dc2f.technologyplayground.keyvaluestore.api.AttributeKey;
import com.dc2f.technologyplayground.keyvaluestore.api.Key;
import com.dc2f.technologyplayground.keyvaluestore.api.KeyValueStore;
import com.dc2f.technologyplayground.keyvaluestore.api.TypedKeyValueStore;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Attribute;
import com.dc2f.technologyplayground.keyvaluestore.api.types.List;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;
import com.dc2f.technologyplayground.keyvaluestore.utils.DigestHelper;

public class BasicKeyValueStore implements KeyValueStore, TypedKeyValueStore {
	
	HashMap<Key, Serializable> values = new HashMap<Key, Serializable>();
	
	@Override
	public Key put(Serializable element) {
		Key key = getKeyForObject(element);
		values.put(key, element);
		return key;
	}
	
	@Override
	public Serializable get(Key key) {
		return values.get(key);
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
	
	protected Key getKeyForObject(Serializable element) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(4 * 1024);
		try {
			ObjectOutputStream os = new ObjectOutputStream(bytes);
			os.writeObject(element);
			os.close();
			bytes.close();
			return new BasicKey(DigestHelper.checkSum(bytes.toByteArray()));
		} catch (IOException e) {
			new RuntimeException(e);
		}
		return null;
	}
	

	public AttributeKey getKeyForAttributeName(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}
}
