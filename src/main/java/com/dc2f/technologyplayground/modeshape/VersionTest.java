package com.dc2f.technologyplayground.modeshape;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.FileUtils;
import org.modeshape.jcr.api.JcrTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionTest {
	Logger logger = LoggerFactory.getLogger(VersionTest.class);
	private RepositoryProvider repositoryProvider;
	private FileSystemUtils fileSystemUtils;

	private VersionTest() {
		repositoryProvider = RepositoryProvider.getInstance();
		fileSystemUtils = FileSystemUtils.getInstance();
	}
	
	public void go() {
		// Load repository
		Repository rep = repositoryProvider.getRepository();
		String fullVersioning = rep.getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED);
		logger.info("Got repository. supports full versioning: " + fullVersioning);
		Path testDataFolder = createTestData();
		try {
			// create a session
			Session session = rep.login("default");
			
			// create a test node which is versionable.
			Node rootNode = session.getRootNode();
			Node testDataNode = rootNode.addNode("testDataNode", "nt:folder");
			testDataNode.addMixin("mix:versionable");
			session.save();
			debugTree(rootNode);
			
			
			
			// load the first version of our test data ..
//			fileSystemUtils.load(testDataFolder.toFile(), testDataNode);
			session.save();
			
			// create a new version for our root Node.
			//errr.. no idea?

			// make some modifications to our test data
			modifyDataTry1(testDataFolder);
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				FileUtils.deleteDirectory(testDataFolder.toFile());
			} catch (IOException e) {
				throw new RuntimeException(e);
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
	private void debugTree(Node root) {
		JcrTools tools = new JcrTools();
		try {
			tools.printSubgraph(root);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

