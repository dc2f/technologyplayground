package com.dc2f.technologyplayground.modeshape;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkspaceSizeTest {
	static Logger logger = LoggerFactory.getLogger(WorkspaceSizeTest.class);
	public static void main(String[] args) {
//		new WorkspaceSizeTest().doit();
		new WorkspaceSizeTest().workspaceCounts();;
	}
	
	private void workspaceCounts() {
		GenericRepositoryFactory repFactory = GenericRepositoryFactory.getInstance();
		FileSystemSynchronizer fs = new FileSystemSynchronizer();
		Repository rep = repFactory.createJackrabbitRepository("/Users/herbert/dev/dc2f/jackrabbit_test");
		try {
			logger.info("login");
			Session session = repFactory.loginWritable(rep, "default");
//			for (int i = 0 ; i < 1000 ; i++) {
//				logger.info("create workspace " + i);
//				session.getWorkspace().createWorkspace("worksace"+i, "default");
//				session.save();
//			}
			logger.info("getVersionmanager");
			VersionManager versionManager = session.getWorkspace().getVersionManager();
			logger.info("getNode");
			Node dataNode = session.getNode("/data");
			logger.info("checkpoint");
			versionManager.checkpoint(dataNode.getPath());
			logger.info("addNode");
			dataNode.addNode("newnode", "nt:folder");
			logger.info("save");
			session.save();
			logger.info("checkpoint");
			versionManager.checkpoint(dataNode.getPath());
			logger.info("save");
			session.save();
			logger.info("logout");
			
			session.logout();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void doit() {
		GenericRepositoryFactory repFactory = GenericRepositoryFactory.getInstance();
		FileSystemSynchronizer fs = new FileSystemSynchronizer();
		Repository rep = repFactory.createMemoryRepository();
		
		try {
			Session session = repFactory.loginWritable(rep, "default");
			Node rootNode = session.getRootNode();
			Node dataNode = rootNode.addNode("data", "nt:folder");
			dataNode.addMixin("mix:versionable");
			
			String baseDirectory = "/Volumes/mydata/data/dev/ceylon-lang.org";
			
			for (int i = 0 ; i < 5 ; i++) {
				Node subNode = dataNode.addNode("sub" + i, "nt:folder");
				logger.info("storing into " + subNode.getName());
				fs.load(new File(baseDirectory), subNode);
				session.save();
			}
			
			logger.info("Press enter when you're done :-)");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			session.logout();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
