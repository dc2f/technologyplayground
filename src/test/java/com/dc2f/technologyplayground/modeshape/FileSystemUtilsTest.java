package com.dc2f.technologyplayground.modeshape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Test;

public class FileSystemUtilsTest {

	/**
	 * Asserts that a file with the given string content exists at the sub-path in the given node.
	 * 
	 * @param node The node in which to dereference the path
	 * @param path The path relative to the node
	 * @param content The expected content
	 */
	void assertFile(Node node, String path, String content) throws RepositoryException {
		assertTrue("subnode " + path + " exists", node.hasNode(path));
		Node file = node.getNode(path);
		String type = file.getPrimaryNodeType().getName();
		assertEquals("nt:file", type);
		assertEquals(content, file.getNode("jcr:content").getProperty("jcr:data").getString());
	}
	
	/**
	 * Loads some test data from the current working directory and checks if the files were inserted correctly.
	 */
	@Test
	public void loadTest() throws RepositoryException {
		GenericRepositoryFactory provider = GenericRepositoryFactory.getInstance();
		
		Repository repository = provider.createMemoryRepository();
		Session session = repository.login("default");
		Node root = session.getRootNode();
		new FileSystemSynchronizer().load(new File("testdata/fs-load-test"), root);		
		session.save();
	
		assertFile(root, "a/f1.txt", "File 1");
		assertFile(root, "a/f2.txt", "File 2");
		assertFile(root, "b/f3.txt", "File 3");
		assertFile(root, "b/c/f4.txt", "File 4");
		
		session.logout();
	}
}
