package com.dc2f.technologyplayground.keyvaluestore;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dc2f.technologyplayground.keyvaluestore.api.Key;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;
import com.dc2f.technologyplayground.keyvaluestore.persistent.PersistentKeyValueStore;

public class TestPersistenKeyValueStore {
	@Test
	public void testPersistingAndRestoring() {
		String storename = "teststore";
		PersistentKeyValueStore store = new PersistentKeyValueStore(storename);
		Node node = new BasicNode();
		node.addAttribute(new BasicAttribute(store.getKeyForAttributeName("content"), "MySmallContent"));
		Key key = store.put(node);
		assertNotNull(key);
		store.close();
		
		PersistentKeyValueStore store2 = new PersistentKeyValueStore(storename);
		Node node2 = store2.getNode(key);
		assertNotNull(node2);
		assertEquals(node, node2);
		assertSame(node.hashCode(), node2.hashCode());
		assertEquals("MySmallContent", node2.getAttribute(store.getKeyForAttributeName("content")));
	}

}
