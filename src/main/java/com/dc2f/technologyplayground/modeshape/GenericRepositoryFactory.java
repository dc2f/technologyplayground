package com.dc2f.technologyplayground.modeshape;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

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
	
	private Map getTransientParameters() {
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
	
	public Repository createMemoryRepository() {
		Map parameters = getTransientParameters();
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
	
	
//	public void shutdown() {
//		
//	}
	
	

}
