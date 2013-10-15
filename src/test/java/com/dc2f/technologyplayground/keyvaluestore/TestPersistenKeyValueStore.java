package com.dc2f.technologyplayground.keyvaluestore;

import org.junit.Test;

import com.dc2f.technologyplayground.keyvaluestore.api.types.Node;

public class TestPersistenKeyValueStore {
	@Test
	public void testPersistingAndRestoring() {
		String storename = "teststore";
		PersistentKeyValueStore store = new PersistentKeyValueStore(storename);
		Node node = new BasicNode();
		node.addAttribute("content", "MySmallContent");
		node.setPath("/node");
		store.put(node);
		store.close();
		
		PersistentKeyValueStore store2 = new PersistentKeyValueStore(storename);
		Node node2 = store2.getNode("/node");
		assertNotNull(node2);
		assertEquals(node, node2);
		assertSame(node.hashCode(), node2.hashCode());
		assertEquals("MySmallContent", node2.getAttribute("content"));
	}
}
