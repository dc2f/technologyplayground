package com.dc2f.technologyplayground.keyvaluestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.dc2f.technologyplayground.keyvaluestore.api.Key;
import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;

public class TestBasicKeyValueStore {
	@Test
	public void testPuttingAndGetting() {
		BasicKeyValueStore store = new BasicKeyValueStore();
		Node node = new BasicNode();
		Key key = store.put(node);
		assertNotNull(key);
		Node node2 = store.getNode(key);
		assertNotNull(node2);
		assertEquals(node, node2);
		assertSame(node.hashCode(), node2.hashCode());
	}
	
	@Test
	public void testPuttingAndGettingWithAttribute() {
		BasicKeyValueStore store = new BasicKeyValueStore();
		Node node = new BasicNode();
		node.addAttribute(new BasicAttribute(store.getKeyForAttributeName("content"), "MySmallContent"));
		Key key = store.put(node);
		assertNotNull(key);
		Node node2 = store.getNode(key);
		assertNotNull(node2);
		assertEquals("MySmallContent", node2.getAttribute(store.getKeyForAttributeName("content")));
	}
}
