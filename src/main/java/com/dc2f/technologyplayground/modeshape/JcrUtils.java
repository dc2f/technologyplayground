package com.dc2f.technologyplayground.modeshape;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import org.apache.commons.lang.StringUtils;

public class JcrUtils {
	public static void debugTree(Node tree) {
		debugTree(tree, 1);
	}
	public static void debugTree(Node tree, int depth) {
//		System.out.println(" "tree.getName()
		try {
			NodeType nodeType = tree.getPrimaryNodeType();
			System.out.println(StringUtils.repeat(" ", depth*2) + " " + tree.getName() + " (" + nodeType.getName() + ")");
			NodeIterator nodes;
			nodes = tree.getNodes();
			while (nodes.hasNext()) {
				Node node = nodes.nextNode();
				debugTree(node, depth+1);
			}
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}
}
