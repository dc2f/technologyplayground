package com.dc2f.technologyplayground.modeshape;

import java.io.File;

import javax.jcr.Repository;
import javax.jcr.Session;

import org.junit.Test;
import org.modeshape.jcr.api.JcrTools;

public class FileSystemUtilsTest {

	@Test
	public void loadTest() throws Exception {
		RepositoryProvider provider = RepositoryProvider.getInstance();
		
		Repository repository = provider.getRepository();
		Session session = repository.login();
		FileSystemUtils.getInstance().load(new File("testdata/fs-load-test"), session.getRootNode());		
		session.save();
		
		JcrTools tools = new JcrTools(true);
		tools.printNode(session.getRootNode());
		provider.releaseRepository();
	}
}
