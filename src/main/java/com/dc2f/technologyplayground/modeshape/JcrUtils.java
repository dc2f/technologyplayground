package com.dc2f.technologyplayground.modeshape;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;

public class JcrUtils {
	public static void debugTree(Node tree) {
		debugTree(tree, 1);
	}
	public static void debugTree(Node tree, int depth) {
//		System.out.println(" "tree.getName()
		StringUtils.repeat(" ", depth*2);
		NodeIterator nodes;
		try {
			nodes = tree.getNodes();
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
		while (nodes.hasNext()) {
			Node node = nodes.nextNode();
			debugTree(node, depth+1);
		}
	}
}
