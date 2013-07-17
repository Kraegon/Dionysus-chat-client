package console;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;

import client.Client;
import client.MessageSender;
import data.DrawableString;
/**
 * Magnificent multipurpose holy Giga Panel.
 * Serves input and output.
 * @author Julian G. West
 */
public class GigaPanel extends JPanel{

	private static final long serialVersionUID = 8885260330488571533L;
	private Font font = new Font("Consolas", Font.BOLD, 16);
	private String toSend;
	private boolean flicker;
	private boolean hideInput = false;
	//private int pointer;
	
	public GigaPanel(){
		//pointer = 0;
		toSend = "";
		Timer t = new Timer(1000/60, new ActionListener() {
			int i=0;
			@Override
			public void actionPerformed(ActionEvent e) {repaint(); i++; if(i == 60){flicker();i=0;}}
		});
		t.start();
	}
	/**
	 * Three segmented paint method.
	 */
	public void paintComponent(Graphics gTemp){
		super.paintComponent(gTemp);
		Graphics2D g = (Graphics2D) gTemp;
		//Background
		g.setPaint(Color.BLACK);
		g.fillRect(0, 0, Frame.getInstance().getWidth(), Frame.getInstance().getHeight());
		//Draw received parts
		g.setFont(font);
		g.translate(0, -25);
		g.setPaint(new Color(0x00FF00));
		LinkedList<DrawableString> dialogue = Client.getDialogueRepository().getDialogue();
		int lastDrawn=0;
		for(int i = dialogue.size()-1; i >= 0; i--){
			if(!dialogue.get(i).isDrawn()){
				lastDrawn = i;
				break;
			}
		}
		for(int i = dialogue.size()-1; i >= 0; i--){
			dialogue.get(i).draw(g, new Point2D.Double(5,(Frame.getInstance().getHeight() - ((i+1 - lastDrawn) * g.getFontMetrics().getHeight()+2))-20));
			if(!dialogue.get(i).isDrawn())
				break;
		}
		//Draw input parts
		String toDisplay = "";
		if(hideInput){
			for(int i = 0; i < toSend.length(); i++){
				toDisplay += '*';
			}
		} else {
			toDisplay = toSend;
		}
		if(flicker){
			//if(pointer >= toSend.length())
				toDisplay = toDisplay + "|";
			//else{
			//	String sub = toDisplay.substring(0,pointer);
			//	sub += "|";
			//	toDisplay = sub + toDisplay.substring(pointer);
			//}
		}
		g.drawString(toDisplay, 5, Frame.getInstance().getHeight() - g.getFontMetrics().getHeight());
	}

	/**
	 * Flicker the '|' pointer.
	 */
	public void flicker(){
		flicker = !flicker;
	}
	/**
	 * Add a character to the input line at the pointer's location.
	 * @param character
	 */
	public void addCharacter(char character){
		//if(toSend.length() == 0 || pointer == toSend.length())
			toSend += character;
		//else{
		//	String sub = toSend.substring(0, pointer);
		//	sub += character;
		//	toSend = sub + toSend.substring(pointer);
		//}
		//pointer++;
	}
	/**
	 * Remove the character on the input line form the pointer's location.
	 */
	public void removeCharacter(){
		if(toSend.length() > 0){
			toSend = toSend.substring(0, toSend.length()-1);
		//	pointer--;
		}
	}
	/**
	 * Move the flashing pointer in a direction.
	 * @param orientiation - -1 to move left and 1 to move right.
	 */
	/*public void movePointer(int orientation){
		System.out.println(pointer + " : " + toSend.length());
		if(toSend.length() != 0 && pointer+1 > toSend.length()){
			pointer += orientation;
		} else {
			System.out.println("I cant let you do that Starfox.");
		}
	}*/
	/**
	 * Decide whether or not you want to display the typed text.
	 * @param hideInput - True to replace written text with stars.
	 */
	public void hideInput(boolean hideInput){
		this.hideInput = hideInput;
	}
	/**
	 * Sends whatever text is on the input line.
	 * Special rules apply to the first two lines sent.
	 */
	public void sendLine(){
		if(!Client.isConnected()){
			Client.setIP(toSend);
			Client.unlock();
		}else if(Client.getName() == null && toSend.length() > 0){
			if(!Client.setName(toSend));
		} else if(toSend.length() > 0){
			MessageSender.sendMessage(toSend);
		}
		hideInput(false);
		toSend = "";
	}
}