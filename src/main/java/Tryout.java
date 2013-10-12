import java.awt.BorderLayout;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.jcr.api.JcrTools;

public class Tryout {
	public static void main(String[] args) {
		ModeShapeEngine engine = initEngine();
		Repository repository = initRepository(engine);
		Session session = login(repository);
		
		Node root = writeData(session);
		
		debugTree(root);

		readContent(session);

		engine.shutdown();
	}

	private static void readContent(Session session) {
		try {
			Node doc = session.getNode("/files/firefox-logo.png");
			Node imageContent = doc.getNode("jcr:content");
			Binary content = imageContent.getProperty("jcr:data").getBinary();
			InputStream is = content.getStream();
			 
			Image image = ImageIO.read(is);
			JFrame frame = new JFrame();
			JLabel label = new JLabel(new ImageIcon(image));
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			is.close();
		} catch (RepositoryException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
	}

	private static void debugTree(Node root) {
		JcrTools tools = new JcrTools();
		try {
			tools.printSubgraph(root);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Session login(Repository repository) {
		
		Session session = null;
		try {
			session = repository.login("default");
		} catch (RepositoryException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
		return session;
	}

	private static Node writeData(Session session) {
		try {
			// Create the '/files' node that is an 'nt:folder' ...
			Node root = session.getRootNode();
			Node filesNode = root.addNode("files", "nt:folder");
			InputStream stream =
			    new BufferedInputStream(Tryout.class.getResourceAsStream("firefox-logo.png"));
			// Create an 'nt:file' node at the supplied path ...
			Node fileNode = filesNode.addNode("firefox-logo.png","nt:file");
	
			// Upload the file to that node ...
			Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
			Binary binary = session.getValueFactory().createBinary(stream);
			contentNode.setProperty("jcr:data", binary);
			session.save();
			return root;
		} catch (RepositoryException e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}
		return null;
	}

	private static Repository initRepository(ModeShapeEngine engine) {
		Repository repository = null;
		String repositoryName = null;
		try {
			URL url = Tryout.class.getClassLoader().getResource(
					"modeshape-settings.json");
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
			repositoryName = config.getName();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		return repository;
	}

	private static ModeShapeEngine initEngine() {
		ModeShapeEngine engine = new ModeShapeEngine();
		engine.start();
		return engine;
	}
}
