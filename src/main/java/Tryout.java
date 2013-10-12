import java.awt.BorderLayout;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.modeshape.common.collection.Problems;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.jcr.api.JcrTools;

import com.dc2f.technologyplayground.modeshape.RepositoryProvider;

public class Tryout {
	public static void main(String[] args) {
		Repository repository = RepositoryProvider.getInstance().getRepository();
		Session session = login(repository);
		
		Node root = writeData(session);
		
		debugTree(root);

		readContent(session);
		RepositoryProvider.getInstance().releaseRepository();
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

}
