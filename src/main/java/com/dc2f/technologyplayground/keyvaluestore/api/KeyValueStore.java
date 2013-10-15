package com.dc2f.technologyplayground.keyvaluestore.api;

import java.io.Serializable;

public interface KeyValueStore {
	public Object get(Key key);
	public Key put(Serializable node);
}
