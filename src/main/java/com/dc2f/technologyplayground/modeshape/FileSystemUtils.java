package com.dc2f.technologyplayground.modeshape;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class FileSystemUtils {
	private FileSystemUtils() {
	}
	
	public static FileSystemUtils getInstance() {
		return new FileSystemUtils();
	}
	
	/**
	 * imports given rootFolder recursively into baseNode of repository.
	 * e.g. rootFolder(/etc) baseNode(/blah) -> /etc/passwd == /blah/passwd
	 * 
	 * If a file exists, it will be overwritten.
	 * 
	 * @param rootFolder root folder from where to import
	 * @param baseNode import relative paths from rootFolder into the repository starting at baseNode
	 */
	public void load(File rootFolder, final Node baseNode) {
		try {
			final Path rootPath = rootFolder.getAbsoluteFile().toPath();
			
			Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
				Node currentFolder = null;

				/**
				 * Get a folder node relative to the base node.
				 * If it doesn't exist it creates it and all its parents if necessary.
				 * 
				 * @param path The path relative to the base node.
				 * @return The folder node
				 */
				private Node getNode(Path path) {
					try {
						Node node = baseNode;
						Path relativePath = rootPath.relativize(path);
						
						for(Path pathPart : relativePath) {
							String name = pathPart.toString();
							if(name.equals("")) {
								continue;
							}
							
							if(!node.hasNode(name)) {
								node.addNode(name, "nt:folder");
							}
							node = node.getNode(name);
						}
						
						return node;
					} catch (RepositoryException e) {
						throw new RuntimeException(e);
					}
				}
				
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					currentFolder = getNode(dir);
					return FileVisitResult.CONTINUE;
				}
	
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						Session session = currentFolder.getSession();
						InputStream in = new BufferedInputStream(new FileInputStream(file.toFile()));
						String fileName = file.getFileName().toString();
						
						// check if the file exists and remove it if it does
						Node fileNode;
						Node contentNode;
						if(currentFolder.hasNode(fileName)) {
//							currentFolder.getNode(fileName).remove();
							fileNode = currentFolder.getNode(fileName);
							contentNode = fileNode.getNode("jcr:content");
						} else {
						
						// create the new file
							fileNode = currentFolder.addNode(fileName, "nt:file");
							contentNode = fileNode.addNode("jcr:content", "nt:resource");
						}
						contentNode.setProperty("jcr:data", session.getValueFactory().createBinary(in));
						
						return FileVisitResult.CONTINUE;
					} catch (RepositoryException e) {
						throw new RuntimeException(e);
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
