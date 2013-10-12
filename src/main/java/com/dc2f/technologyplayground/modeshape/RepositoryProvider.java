package com.dc2f.technologyplayground.modeshape;

import java.net.URL;

import javax.jcr.Repository;

import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.ModeShapeEngine.State;
import org.modeshape.jcr.RepositoryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * singleton repository provider responsible for managing a single repository.
 * 
 * calls to {@link #getRepository()} have to be balanced with {@link #releaseRepository()}.
 */
public class RepositoryProvider {
	
	private static final String MODESHAPE_SETTINGS_FILE = "modeshape-settings.json";

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryProvider.class);
	
	private static RepositoryProvider instance;
	private ModeShapeEngine engine;
	private Repository repository;
	
	private int openCount = 0;
	
	private RepositoryProvider() {
	}

	public static RepositoryProvider getInstance() {
		if (instance == null) {
			instance = new RepositoryProvider();
		}
		return instance;
	}
	
	public Repository getRepository() {
		openCount++;
		if (engine == null) {
			initEngine();
		}
		if (engine.getState().equals(State.NOT_RUNNING)) {
			engine.start();
		}
		
		if (repository == null) {
			initRepository(engine);
		}
		return repository;
	}
	
	public void releaseRepository() {
		openCount--;
		if (openCount == 0) {
			engine.shutdown();
		}
	}
	
	private synchronized ModeShapeEngine initEngine() {
		if (engine == null) {
			engine = new ModeShapeEngine();
			engine.start();
		}
		return engine;
	}
	
	private synchronized Repository initRepository(final ModeShapeEngine engine) {
		if (repository == null) { 
			try {
				URL url = RepositoryProvider.class.getClassLoader().getResource(
						MODESHAPE_SETTINGS_FILE);
				RepositoryConfiguration config = RepositoryConfiguration.read(url);
				// Verify the configuration for the repository ...
				Problems problems = config.validate();
				if (problems.hasErrors()) {
					System.err.println("Problems starting the engine.");
					System.err.println(problems);
					System.exit(-1);
				}
				// Deploy the repository ...
				repository = engine.deploy(config);
			} catch (Throwable e) {
				LOGGER.error("Error in the repository configuration file {}.", new Object[]{MODESHAPE_SETTINGS_FILE, e});
				throw new RuntimeException(e);
			}
		}
		return repository;
	}
}
