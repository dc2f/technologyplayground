package com.dc2f.technologyplayground.modeshape;

import javax.jcr.Repository;

/**
 * singleton repository provider responsible for managing a single repository.
 * 
 * calls to {@link #getRepository()} have to be balanced with {@link #releaseRepository()}.
 */
public class RepositoryProvider {
	
	private static RepositoryProvider instance;
	
	private RepositoryProvider() {
	}

	public static RepositoryProvider getInstance() {
		if (instance == null) {
			instance = new RepositoryProvider();
		}
		return instance;
	}
	
	public Repository getRepository() {
		return null;
	}
	
	public void releaseRepository() {
		
	}
}
