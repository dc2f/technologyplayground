package com.dc2f.technologyplayground.modeshape;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleVersioningTest {
	Logger logger = LoggerFactory.getLogger(SimpleVersioningTest.class);
	
//	private RepositoryProvider repositoryProvider;
//	private FileSystemUtils fileSystemUtils;

	public SimpleVersioningTest() {
//		repositoryProvider = RepositoryProvider.getInstance();
//		fileSystemUtils = FileSystemUtils.getInstance();
	}

	public static void main(String[] args) {
		try {
			new SimpleVersioningTest().doit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doit() throws Exception {
//		Session session = repositoryProvider.getRepository().login("default");
//		Node rootNode = session.getRootNode();
//		
//		Node n = rootNode.addNode("childNode", "nt:unstructured");
////		n.addMixin("mix:versionable");
//		versioningBasics(rootNode, session);
//		
//		repositoryProvider.releaseRepository();
		
		GenericRepositoryFactory repProv = GenericRepositoryFactory.getInstance();
		Repository repository = repProv.createMemoryRepository();
		String fullVersioning = repository.getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED);
		logger.info("Got repository. supports full versioning: " + fullVersioning);

//		TransientRepository repository = new TransientRepository();
		Session session = repProv.loginWritable(repository, "default");
		Node rootNode = session.getRootNode();
		Node n = rootNode.addNode("childNode", "nt:unstructured");
		versioningBasics(n, session);
		session.logout();
	}

	public void versioningBasics(Node parentNode, Session session)
			throws RepositoryException {
		// create versionable node
		Node n = parentNode.addNode("childNode", "nt:unstructured");
		n.addMixin("mix:versionable");
		n.setProperty("anyProperty", "Blah");
		session.save();
		Version firstVersion = n.checkin();

		// add new version
		Node child = parentNode.getNode("childNode");
		child.checkout();
		child.setProperty("anyProperty", "Blah2");
		session.save();
		child.checkin();

		// print version history
		VersionHistory history = child.getVersionHistory();
		for (VersionIterator it = history.getAllVersions(); it.hasNext();) {
			Version version = (Version) it.next();
			System.out.println(version.getCreated().getTime());
		}

		// restoring old version
		child.checkout();
		child.restore(firstVersion, true);
	}

}
