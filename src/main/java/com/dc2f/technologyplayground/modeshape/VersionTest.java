package com.dc2f.technologyplayground.modeshape;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionTest {
	Logger logger = LoggerFactory.getLogger(VersionTest.class);
	private GenericRepositoryFactory repositoryProvider;
	private FileSystemSynchronizer fileSystemUtils;

	private VersionTest() {
		repositoryProvider = GenericRepositoryFactory.getInstance();
		fileSystemUtils = new FileSystemSynchronizer();
	}
	
	public void go() {
		// Load repository
		Repository rep = repositoryProvider.createMemoryRepository();
		Path testDataFolder = createTestData();
		try {
			// create a session
//			Session session = rep.login("default");
			Session session = rep.login(new SimpleCredentials("username", "password".toCharArray()), "default");

			String fullVersioning = rep.getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED);
			String simpleVersioning = rep.getDescriptor(Repository.OPTION_SIMPLE_VERSIONING_SUPPORTED);
			logger.info("Got repository. supports full versioning: " + fullVersioning);
			logger.info("Got repository. supports simple versioning: " + simpleVersioning);

			// create a test node which is versionable.
			Node rootNode = session.getRootNode();
			Node testDataNode = rootNode.addNode("testDataNode", "nt:folder");
			testDataNode.addMixin("mix:versionable");
			session.save();
			JcrUtils.debugTree(testDataNode);
			

			VersionManager versionManager = session.getWorkspace().getVersionManager();
			versionManager.checkout(testDataNode.getPath());
			versionManager.checkin(testDataNode.getPath());
			versionManager.checkout(testDataNode.getPath());
			
			
			// load the first version of our test data ..
			fileSystemUtils.load(testDataFolder.toFile(), testDataNode);
			session.save();
			
			// create a new version for our root Node.
			//errr.. no idea?
			Version prevVersion = versionManager.checkin(testDataNode.getPath());
			versionManager.checkout(testDataNode.getPath());

			// make some modifications to our test data
			modifyDataTry1(testDataFolder);
			
			// load the data again..
			fileSystemUtils.load(testDataFolder.toFile(), testDataNode);
			session.save();
			versionManager.checkin(testDataNode.getPath());
			versionManager.checkout(testDataNode.getPath());
			
			debugVersionHistory(testDataNode, versionManager);
			JcrUtils.debugTree(testDataNode);
			
			// now test to diff our two versions ..
			session.getWorkspace().createWorkspace("second workspace", session.getWorkspace().getName());
			session.save();
			
			Version v11 = versionManager.getVersionHistory(testDataNode.getPath()).getVersion("1.1");
			versionManager.restore(v11, true);
			
			logger.info("Restored original workspace.");
			
			Session cmpSession = rep.login("second workspace");
			Node cmpTestDataNode = cmpSession.getNode(testDataNode.getPath());
			VersionHistory cmpVersionHistory = cmpSession.getWorkspace().getVersionManager().getVersionHistory(cmpTestDataNode.getPath());
			Version cmpVersion = cmpVersionHistory.getVersion("1.0");
			logger.info("we want to compare with version: " + cmpVersion.getIdentifier());
			debugVersionHistory(cmpTestDataNode, cmpSession.getWorkspace().getVersionManager());
//			cmpSession.getWorkspace().getVersionManager().restore(cmpVersion, true);
			System.out.println("===========================================");
			JcrUtils.debugTree(cmpTestDataNode);
			System.out.println("===========================================");
			compareNodes(cmpTestDataNode, testDataNode);
			
			session.logout();
			cmpSession.logout();
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		} finally {
//			repositoryProvider.releaseRepository();
			try {
				FileUtils.deleteDirectory(testDataFolder.toFile());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void debugVersionHistory(Node testDataNode,
			VersionManager versionManager)
			throws UnsupportedRepositoryOperationException, RepositoryException {
		VersionHistory versionHistory = versionManager.getVersionHistory(testDataNode.getPath());
		VersionIterator vit = versionHistory.getAllVersions();
//			versionHistory.
		logger.info("version history id: " + versionHistory.getIdentifier() + " / " + versionHistory.getVersionableIdentifier());
		while (vit.hasNext()) {
			Version v = (Version) vit.next();
			logger.info("some version: " + v.getName() + " --- " + v.getIdentifier());
		}
	}
	
	private void compareNodes(Node testDataNode, Node cmpTestDataNode) throws RepositoryException {
		logger.info("Comparing two nodes.." + testDataNode.getPath());
		NodeIterator childNodes = testDataNode.getNodes();
		while (childNodes.hasNext()) {
			Node node = (Node) childNodes.next();
			if (!cmpTestDataNode.hasNode(node.getName())) {
				logger.info("Not available in second: " + node.getPath());
			} else {
				compareNodes(node, cmpTestDataNode.getNode(node.getName()));
			}
		}
	}

	private void modifyDataTry1(Path testDataFolder) {
//		testDataFolder.
		File second = new File(testDataFolder.toFile(), "testxx.txt");
		try {
			Files.write(second.toPath(), "Just testing.".getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Path createTestData() {
		try {
			File testBaseDataFolder = new File("testdata");
			if (!testBaseDataFolder.exists()) {
				throw new RuntimeException("call us from the same directory as testdata.");
			}
			Path testDataFolder = Files.createTempDirectory("versiontest");
			Files.copy(testBaseDataFolder.toPath(), new File(testDataFolder.toFile(), "x").toPath());
			FileUtils.copyDirectory(testBaseDataFolder, testDataFolder.toFile());
			return testDataFolder;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		VersionTest versionTest = new VersionTest();
		versionTest.go();
	}
}

