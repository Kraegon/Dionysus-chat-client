package console;

import input.Keyboard;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import client.Client;
/**
 * Frame to house the client. Minimal Swing components used.
 * @author Julian G. West
 */
public class Frame extends JFrame{
	private static final long serialVersionUID = -4131350952546988036L;
	private static Frame INSTANCE;
	private static GigaPanel gigaPanel;
	
	public Frame(){
		super("Dionysus - v" + Client.getVersionNo());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new Keyboard());
		setBackground(Color.BLACK);
		gigaPanel = new GigaPanel();
		setContentPane(gigaPanel);
		//setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
		//setUndecorated(true);
		setSize(800,800);
		setResizable(false);
		try {setIconImage(ImageIO.read(new File("Images/DioIcon.jpg")));
		} catch (IOException e) {System.out.println("Icon error");}
		setVisible(true);
	}
	/**
	 * Call upon a higher power to descend the Giga Panel from the heavens.
	 * @return - The holy multifunctional Giga Panel.
	 */
	public GigaPanel descendGigaPanel(){
		return gigaPanel;
	}
	/**
	 * Singleton method.
	 * @return - The one and only frame.
	 */
	public static Frame getInstance(){
		if(INSTANCE == null)
			INSTANCE = new Frame();
		return INSTANCE;
	}
}
