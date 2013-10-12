package com.dc2f.technologyplayground.modeshape;

import static org.junit.Assert.*;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;

import org.junit.Test;

public class RepositoryProviderTest {
	@Test(timeout=10000)
	public void testInitRepository() throws LoginException, RepositoryException {
		RepositoryProvider provider = RepositoryProvider.getInstance();
		Repository repository = provider.getRepository();
		assertNotNull(repository);
		repository.login();
		provider.releaseRepository();
	}

	@Test(timeout=10000)
	public void testGettingRepositoryTwice() {
		RepositoryProvider provider = RepositoryProvider.getInstance();
		Repository repository = provider.getRepository();
		assertNotNull(repository);
		Repository repository2 = provider.getRepository();
		assertNotNull(repository2);
		assertSame(repository2, repository);
		
		provider.releaseRepository();
		provider.releaseRepository();
	}
	
	@Test(timeout=10000)
	public void testRestartingEngine() throws LoginException, RepositoryException {
		testInitRepository();
		testInitRepository();
	}
}
