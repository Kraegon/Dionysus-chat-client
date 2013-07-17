package data;

import java.awt.FontMetrics;
import java.util.LinkedList;

import console.Frame;

import client.Client;
/**
 * Last 50 messages received will be contained here.
 * @author Julian G. West
 *
 */
public class DialogueRepository {
	LinkedList<DrawableString> dialogue;
	FontMetrics fontMetrics;
	
	public DialogueRepository(){
		dialogue = new LinkedList<DrawableString>();
	}
	/**
	 * Set the font metrics to find cutpoints in a drawable string.
	 * @param fontMetrics - The font used in the console.
	 */
	public void setFontMetrics(FontMetrics fontMetrics){
		this.fontMetrics = fontMetrics;
	}
	/**
	 * Get the metrics of the used font.
	 * @return - current font metrics.
	 */
	public FontMetrics getFontMetrics(){
		return fontMetrics;
	}
	/**
	 * @return The list of the latest 50 messages.
	 */
	public LinkedList<DrawableString> getDialogue(){
		return dialogue;
	}
	/**
	 * Add a message to the list.
	 * @param message - to be printed by the console
	 */
	public synchronized void commitMessage(String message){
		if(message.substring(message.indexOf(">>") + 2).isEmpty()) return;
		if(!replace(message, findCutPoint(message))){
			dialogue.addFirst(new DrawableString(Client.now() + " " + message));
		}
		System.out.println(Thread.currentThread().getStackTrace());
	}
	/**
	 * Add a message to the list
	 * @param message - to be printed by the console
	 * @param isInstant - True if this message to be printed immediately
	 */
	public synchronized void commitMessage(String message, boolean isInstant){
		dialogue.addFirst(new DrawableString(message,isInstant));
	}
	
	/**
	 * Cuts down the list to 50 entries.
	 * @return True if successful.
	 */
	public boolean cutList(){
		boolean success = false;
		while(dialogue.size() > 50)
			dialogue.pollLast();
		if(dialogue.size() == 50){
			success = true;
		}
		return success;
	}
	/**
	 * Replace a long string with two smaller ones, from the last occurrence of an empty space.
	 * @param toReplace - Drawable string that will be removed and replaced.
	 * @param cutPoint - index at which to cut the string. -1 for no cuts, 0 for not words to cut at.
	 */
	public boolean replace(String toReplace, int cutPoint){
		if(cutPoint == -1){
			return false;
		}
		commitMessage(toReplace.substring(0, cutPoint));
		if(toReplace.substring(cutPoint).trim().length() > 0){
			commitMessage(toReplace.substring(cutPoint).trim());
		}
		return true;
	}
	/**
	 * Find the point to split a string and fit it on the screen.
	 * @param string - String to subjugate to splitting.
	 * @return - The index where the split takes place.
	 */
	public int findCutPoint(String string){
		int cutPoint = 0;
		for(int i = 0; i < string.length(); i++){
			if(fontMetrics.stringWidth(string.substring(0, i)) >= Frame.getInstance().getWidth()-20){
				cutPoint = string.substring(0, i).lastIndexOf(" ");
				if(cutPoint == 0)
					cutPoint = -1;
				break;
			}else if(string.contains("\n")){ 
					cutPoint = string.lastIndexOf("\n"); 
			}else{
				cutPoint = -1;
			}
		}
		return cutPoint;
	}
}
