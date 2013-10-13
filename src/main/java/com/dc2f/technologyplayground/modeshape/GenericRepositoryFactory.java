package com.dc2f.technologyplayground.modeshape;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.io.FileUtils;

public class GenericRepositoryFactory {

	private static GenericRepositoryFactory instance;

	private GenericRepositoryFactory() {
	}

	public static GenericRepositoryFactory getInstance() {
		if (instance == null) {
			instance = new GenericRepositoryFactory();
		}
		return instance;
	}
	
	private Map<String, String> getTransientParameters() {
//		Properties props = new Properties();
//		try {
//			props.load(getClass().getResourceAsStream("jackrabbit.properties"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Map<String, String> props = new HashMap<>();
		URL conf = getClass().getClassLoader().getResource("jackrabbit_memory.conf");
		URL modeShapeConf = getClass().getClassLoader().getResource("modeshape-settings.json");
		String path = conf.getPath();
		try {
			final Path tmpdir = Files.createTempDirectory("jackrabbithome");
			props.put("org.apache.jackrabbit.repository.home", tmpdir.toString());
			props.put("org.apache.jackrabbit.repository.conf", path);
			props.put("org.modeshape.jcr.URL", modeShapeConf.toString());
			System.out.println("props: " + props.toString());
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						FileUtils.deleteDirectory(tmpdir.toFile());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			
			return props;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//		props.put("org.apache.jackrabbit.repository.conf", path);
	}
	
	/**
	 * make sure to call {@link #loginReadable(Repository, String)} and {@link #loginWritable(Repository, String)}
	 * instead of repository.login(..)!
	 */
	public Repository createMemoryRepository() {
		Map<?,?> parameters = getTransientParameters();
		for (RepositoryFactory factory : ServiceLoader.load(RepositoryFactory.class)) {
			Repository repository = null;
			try {
				repository = factory.getRepository(parameters);
			} catch (RepositoryException e) {
				// ignore for now.
			}
			if (repository != null) {
				return repository;
			}
		}
		return null;
	}
	
	public Repository createJackrabbitRepository(String path) {
		Map<String, String> parameters = getTransientParameters();
		parameters.put("org.apache.jackrabbit.repository.home", path);
		for (RepositoryFactory factory : ServiceLoader.load(RepositoryFactory.class)) {
			Repository repository = null;
			try {
				repository = factory.getRepository(parameters);
			} catch (RepositoryException e) {
				// ignore for now.
			}
			if (repository != null) {
				return repository;
			}
		}
		return null;
	}
	
	public Session loginWritable(Repository repository, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
//		return repository.login(new SimpleCredentials("username", "password".toCharArray()), workspaceName);
		return repository.login(workspaceName);
	}
	
	public Session loginReadable(Repository repository, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
		return repository.login(workspaceName);
	}
	
	
//	public void shutdown() {
//		
//	}
	
	

}
