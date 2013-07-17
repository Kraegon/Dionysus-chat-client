package clientHandling;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import server.Server;

import commandSystem.CharCommand;
import commandSystem.CommandList;

/**
 * Thread that handles in and output to/from clients.
 * @author Julian G. West
 *
 */
public class ClientHandler extends Thread implements Runnable{
	private Socket socket;
	
	private DataInputStream in;
	private DataOutputStream out;
	private ClientData client = new ClientData();
		
	private boolean isRunning = true;
	/**
	 * Constructor for a client's connection
	 * @param socket - the socket which the in and output streams use  
	 */
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	/**
	 * Initialises connection and will start receiving messages from here.
	 * Parses whether or not a message is indeed a message or a special command.
	 */
	public void run(){
		try {
			if(isRunning){
				in= new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				client.setAddress(socket.getInetAddress());
				boolean isApproved = false;
				while(!isApproved){
					String clientName = getClientName();
					if(isUsedName(clientName)){
						out.writeBoolean(true);
					}else{
						out.writeBoolean(false);
						client.setName(clientName);
						isApproved = true;
					}
				}
			
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				client.setHandler(this);
				Server.getHeadroom().addClient(client);	
				System.out.println("Connection established with: " + client.getName() + " : " + client.getAddress().getHostAddress() + " at " + Server.now());
				client.getCurrentRoom().broadcastRoom(client.getName() + " has logged on.", null);
				sendMessage("SYS>> Welcome to Dionysus, " + client.getName() + ". Thanks for staying.");
				sendMessage("SYS>> Use -h for a commandslist.");
				while(isRunning){
					String message = receiveMessage();
					if(message != null){
						if(message.length() > 100){
							client.getCurrentRoom().broadcastRoom(client.getName() + " is trying to spam.", null);
						} else {
							if(message.charAt(0) == '-' && message.length() > 1){
								executeCommand(message);
							} else {
								System.out.println(">" + message + " : by " + client.getName() + " at " + Server.now());
								client.getCurrentRoom().broadcastRoom(message, client);		
								message = null;
							}
						}
					} 
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * If a message contains the special character, it is seen as command and is parsed further here.
	 * @param message - the command bearing message
	 */
	public void executeCommand(String message){
		boolean commandFound = false;
		for(CharCommand c : CommandList.getCharacterCommands()){
			if(message.charAt(1) == c.getKey()){
				if(c.validateUse(client))
					c.command(client);
				else
					sendMessage("SYS>> This command requires admin rights.");
				commandFound = true;
			}
		}
		if(!commandFound)
			sendMessage("SYS>> Unkown Command, type -h for commandslist.");	
			
	}
	/**
	 * Receive the client's desired username via my protocol.
	 * Performs no conformation on the name.
	 * @return - Name as sent by the user. Defaults to John Dough on errors.
	 */
	public String getClientName(){
		 String name = "John Dough";
		 try{
			 char initialiser = in.readChar();
			 if(initialiser == '$'){
				 name = "";
				 while(true){
					 char letter = in.readChar();
					 if(letter == '%')
						 break;
					 else 
						 name += letter;
				 }
			 } else {
				 name = getClientName();
			 }
		 }catch(IOException e){
			 e.printStackTrace();
		 }
		 return name;
	}
	/**
	 * Checks if a name is in use on the entire server or not.
	 * @param name - name to validate
	 * @return - true if the name is not used
	 */
	public boolean isUsedName(String name){
		boolean isUsed = false;
		for(ClientData c : Server.getAllClients()){
			if(name.toLowerCase().equals(c.getName().toLowerCase()))
				isUsed = true;
		}
		return isUsed;
	}
	/**
	 * Send a single character to the client.
	 * @param character - single character to transmit
	 */
	public void sendCharacter(char character){
		try {
			out.writeChar(character);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Send a message with my protocol to the client character by character.
	 * @param message - The message to send to the client.
	 */
	public void sendMessage(String message){
		try {
			out.writeChar('*');
			char[] temp = message.toCharArray();
			for(int i = 0; i < temp.length; i++){
				out.writeChar(temp[i]);
			}
			out.writeChar('þ');		
		} catch (Exception e) {
			closeConnection();
		}
	}
	/**
	 * Receive a message from the client via my protocol. It's not specified if this is ultimately a command or message.
	 * @return - the received message
	 */
	public String receiveMessage(){
		String transmission;
		
		try{
			char initialiser = in.readChar();
			if(initialiser == '*'){
				transmission = "";
				while(true){
					char temp = in.readChar();
					if(temp == 'þ')
						return transmission;
					else
						transmission += temp;
				}	
			} else {
				return receiveMessage();
			}
		} catch (IOException e){
			System.out.println("Connection to " + client.getName() +  " lost at " + Server.now());
			client.getCurrentRoom().broadcastRoom(client.getName() + " has logged off.", null);
			client.getCurrentRoom().removeClient(client);
			closeConnection();
		} 
		return null;
	}
	/**
	 * Closes the connection in an orderly fashion.
	 * 
	 * No guarantee. 
	 */
	public void closeConnection(){
		try {
			in.close();
			out.close();
			socket.close();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		isRunning = false;
	}

}
