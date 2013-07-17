package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import console.Frame;
import data.DialogueRepository;

/**
 * Main client class for Dionysus. Starts the connection and sets the
 * datastreams up for their respective threads.
 * 
 * @author Julian G. West
 * 
 */
public class Client {
	
	//General attributes
	static String versionNo = "0.2.4";
	static String ip;	
	static String yourName;
	static DialogueRepository dialogue;
	//Connection related attributes
	static Socket socket;
	static boolean block = true; 
	static boolean isConnected;
	static Thread listener;
	
	public static void main(String[] args) throws IOException,InterruptedException {
		isConnected = false;
		dialogue = new DialogueRepository();
		Frame.getInstance();
		dialogue.setFontMetrics(Frame.getInstance().descendGigaPanel().getGraphics().getFontMetrics());
		initConnection();
		printLogo();
		Thread.sleep(10);
		new Sound();
		listener = new MessageListener(new DataInputStream(socket.getInputStream()));
		new MessageSender(new DataOutputStream(socket.getOutputStream()));
		Thread.sleep(100);
		Sound.playSeq(Sound.LOGON);
		
		Client.getDialogueRepository().commitMessage("SYS>> Your name please: ");
	}

	/**
	 * Start the connection with specified IP. A default is possible.
	 * 
	 * @throws InterruptedException
	 *             Shouldn't happen.
	 */
	private static void initConnection() throws InterruptedException {
		Client.getDialogueRepository().commitMessage("SYS>> Please enter the IP adress to connect to. Default for the main server.");
		while(block){
			Thread.sleep(1);
		}
		if (ip.toLowerCase().equals("default") || ip.trim().equals(""))
			ip = "localhost"; // Main server IP yet to be established
		try {
			socket = new Socket(ip, 8500);
		} catch (UnknownHostException e) {
			Client.getDialogueRepository().commitMessage("ERR>> Failure to connect to host.");
			Thread.sleep(2000);
			System.exit(0);
		} catch (ConnectException e) {
			Client.getDialogueRepository().commitMessage("ERR>> Failure to connect to host.");
			Thread.sleep(2000);
			System.exit(0);
		} catch (Exception e) {
			Client.getDialogueRepository().commitMessage("ERR>> Something went wrong and I don't know WHAT the fuck it is.");
			Thread.sleep(2000);
			System.exit(0);
		}
		isConnected = true;
	}

	/**
	 * Reads in a name and validates it locally to hardcoded rules, then checks
	 * it on the server. Waits for response boolean.
	 */
	public static Boolean setName(String name){
		boolean isApproved = false;		
		if(name.toLowerCase().contains("adm>") || name.toLowerCase().contains("sys")){
			Client.getDialogueRepository().commitMessage("ERR>> Invalid name. You're not an admin or the system. Try again.");
		}else if(name.toCharArray().length > 23){
			Client.getDialogueRepository().commitMessage("ERR>> Invalid name. Too long. Try again.");
		}else if(MessageListener.isNameTaken(name)){
			Client.getDialogueRepository().commitMessage("ERR>> Name has been taken. Try again.");
		} else {
			isApproved = true;
		}
		if(!isApproved){
			return false;
		}
		yourName = name;
		Frame.getInstance().setTitle(Frame.getInstance().getTitle() +" - " + yourName);
		Client.getDialogueRepository().commitMessage("SYS>> "+ yourName + " approved.");
		listener.start();
		return isApproved;
	}

	/**
	 * Prints the sweet Dionysus logo.
	 */
	public static void printLogo() {
		Client.getDialogueRepository().commitMessage("-------------------------------------------",true);
		Client.getDialogueRepository().commitMessage("    ___ _                                 ",true);
		Client.getDialogueRepository().commitMessage("   /   (_) ___  _ __  _   _ ___ _   _ ___ ",true);
		Client.getDialogueRepository().commitMessage("  / /\\ / |/ _ \\| '_ \\| | | / __| | | / __|",true);
		Client.getDialogueRepository().commitMessage(" / /_//| | (_) | | | | |_| \\__ \\ |_| \\__ \\",true);
		Client.getDialogueRepository().commitMessage("/___,' |_|\\___/|_| |_|\\__, |___/\\__,_|___/",true);
		Client.getDialogueRepository().commitMessage("                      |___/ Chat Client ",true);
		Client.getDialogueRepository().commitMessage("                 V" + versionNo + " by Julian G. West",true);
		Client.getDialogueRepository().commitMessage("-------------------------------------------",true);
	}
	/**
	 * Get the username.
	 * @return - This client's username
	 */
	public static String getName() {
		return yourName;
	}
	/**
	 * Set the ip of the server the client is connecting to.
	 * @param ip - ip address of the server "xxx.xxx.xxx.xxx"
	 */
	public static void setIP(String ip){
		Client.ip = ip;
	}
	/**
	 * Unlock the blockade waiting for the setting of the ip.
	 */
	public static void unlock(){
		block = false;
	}
	/**
	 * Check whether or not the connection has been established
	 * @return - true if the point of connection has been passed
	 */
	public static boolean isConnected(){
		return isConnected;
	}
	/**
	 * Get the version name of the Dionysus system.
	 * 
	 * @return String with the version number format "x.x.x"
	 */
	public static String getVersionNo() {
		return versionNo;
	}
	/**
	 * Get the current time from the client.
	 * @return - The current time in format HH:mm
	 */
	public static String now() {
		SimpleDateFormat format = new SimpleDateFormat("[HH:mm]");
		return format.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * Get the dialogue log for this client.
	 * 
	 * @return The class containing the 50 latest messages.
	 */
	public static synchronized DialogueRepository getDialogueRepository() {
		return dialogue;
	}
}