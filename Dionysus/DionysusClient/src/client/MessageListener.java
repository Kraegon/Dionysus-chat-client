package client;

import java.io.DataInputStream;
import java.io.IOException;

import console.Frame;
/**
 * Listener thread that waits for input from the socket.
 * Also parses the incoming messages for commands and commands the displaying.
 * @author Julian G. West
 *
 */
public class MessageListener extends Thread implements Runnable{
	static DataInputStream in;
	/**
	 * Only viable constructor. Stream highly important.
	 * @param in - Inputstream from the socket.
	 */
	public MessageListener(DataInputStream in){
		MessageListener.in = in;
	}
	/**
	 * Listen for messages and immediately handle the command.
	 */
	public void run(){
		while(true){
			String message = receiveMessage();
			if(message != null){
				if(message.substring(0, message.indexOf(">>")).contains(Client.getName()) || message.contains("SYS>>")){
					Client.getDialogueRepository().commitMessage(message);
				} else {
					Sound.playSeq(Sound.MESSAGE);
					Client.getDialogueRepository().commitMessage(message);
				}
				if(message.contains("SYS>> ") && message.contains("has called to arms.")){
					Sound.playSeq(Sound.BUZZER);
				}
				message = null;
			}
		}
	}
	/**
	 * Assist method to receive messages via a personal protocol.
	 * @return Returns the string received from the server.
	 */
	private static String receiveMessage(){
		String transmission;
		try{
			char initialiser = in.readChar();
			if(initialiser == '*'){
				transmission = "";
				while(true){
					char temp = in.readChar();
					if(temp == 'þ')
						return transmission;
					transmission += temp;
				}
			} else if (initialiser == 'µ'){
				Client.getDialogueRepository().commitMessage("SYS>> Thank you for using the Dionysus chat client");
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					System.out.println("ERR>> Not so fast!");
				}
				System.exit(0);
			} else if (initialiser == 'õ'){
				Sound.toggleIsMute();
				if(Sound.isMute())
					return("SYS>> Muted");
				else
					return ("SYS>> Unmuted");
			} else if (initialiser == '©'){
				Client.printLogo();
			} else if (initialiser == '^'){
				Frame.getInstance().descendGigaPanel().hideInput(true);
			}
		} catch (IOException e){
			Client.getDialogueRepository().commitMessage("ERR>> Connection to host lost.");
			try {
				sleep(2000);
			} catch (InterruptedException e1) {
				System.out.println("ERR>> Not so fast!");
			}
			System.exit(0);
		} 
		return null;
	}
	
	/**
	 * Method waiting for a server response to user specified name.
	 * 
	 * @param name - user specified name
	 * @return - true if name is valid, false if not.
	 */
	public static boolean isNameTaken(String name) {
			MessageSender.sendName(name);
			try {
				return in.readBoolean();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
	}
}


