package com.dc2f.technologyplayground.modeshape;

import static org.junit.Assert.*;

import javax.jcr.Repository;

import org.junit.Test;

public class RepositoryProviderTest {
	@Test(timeout=10000)
	public void testInitRepository() {
		RepositoryProvider provider = RepositoryProvider.getInstance();
		Repository repository = provider.getRepository();
		assertNotNull(repository);
		provider.releaseRepository();
	}

}
