package clientHandling;
import java.net.InetAddress;

import environment.Room;


/**
 * Data class that stores information on a client.
 * @author Julian G. West
 *
 */
public class ClientData implements Comparable<ClientData>{
	private String name;	
	private Room currentRoom;
	private InetAddress address;
	private ClientHandler handler;
	private boolean isAdmin = false;
	/**
	 * Constructor for empty client objects.
	 */
	public ClientData() {
	}
	/**
	 * Constructor for the basics of a client.
	 * @param name - client's screenname 
	 * @param address - clients IP address
	 * @param handler - Attributed clienthandler. Separate thread
	 */
	public ClientData(String name, InetAddress address, ClientHandler handler) {
		this.name = name;
		this.address = address;
		this.handler = handler;
	}
	/**
	 * Get the screenname of the user.
	 * @return - username
	 */
	public String getName() {
		return name;
	}
	/**
	 * Change the client's username.
	 * @param name - username
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Get the internet specifics of the client
	 * @return - container of information on the client
	 */
	public InetAddress getAddress() {
		return address;
	}
	/**
	 * Change the client's internet address.
	 * @param address - internetaddress
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	/**
	 * Get the handler for execution of tasks to and from the client.
	 * @return - the handler
	 */
	public ClientHandler getHandler() {
		return handler;
	}
	/**
	 * Sets this client's attributed handler.
	 * @param handler - a clienthandler thread to do this client's dirty work
	 */
	public void setHandler(ClientHandler handler) {
		this.handler = handler;
	}
	/**
	 * The rights a user has.
	 * @return - True if the user is an admin
	 */
	public boolean getIsAdmin(){
		return isAdmin;
	}
	/**
	 * Switch a users admin-ness on or off.
	 */
	public void toggleIsAdmin(){
		isAdmin = !isAdmin;
	}
	/**
	 * Compares clients among eachother, admins take precedence over normal users.
	 */
	public int compareTo(ClientData o) {
		if(isAdmin)
			return -1;
		else
			return 0;
	}
	/**
	 * Get the location of the client. This does not necessarily correspond to the room's clientlist.
	 * @return - Room associated with the client
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}
	/**
	 * Change the room the client is in.  This does not necessarily correspond to the room's clientlist.
	 * @param newRoom - Room to move the player into.
	 */
	public void setCurrentRoom(Room newRoom) {
		this.currentRoom = newRoom;
	}
}
