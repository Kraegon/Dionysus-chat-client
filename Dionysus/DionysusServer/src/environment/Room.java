package environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import clientHandling.ClientData;


/**
 * Room class for users to form groups in and adventure around.
 * Uses a parent/child system to position itself in a tree of rooms.
 * @author Julian G. West
 *
 */
public class Room {
	ArrayList<ClientData> clients = new ArrayList<ClientData>();
	Room parentRoom;
	ArrayList<Room> subRooms = new ArrayList<Room>();
	String roomName;
	String password;
	int roomSize; //-1 for infinite
	/**
	 * Constructor to create a room without password.
	 * @param roomName - Name to list the room by.
	 * @param roomSize - Maximum number of users allowed.
	 * @param parentRoom - Room in which this room was created.
	 */
	public Room(String roomName, int roomSize, Room parentRoom){
		this.roomName = roomName;
		this.roomSize = roomSize;
		this.parentRoom = parentRoom;
	}
	/**
	 * Contructor to create a room with password.
	 * @param roomName - Name to list the room by.
	 * @param roomSize - Maximum number of users allowed.
	 * @param parentRoom - Room in which this room was created.
	 * @param password - Password to lock rooms.
	 */
	public Room(String roomName, int roomSize, Room parentRoom, String password){
		this.roomName = roomName;
		this.roomSize = roomSize;
		this.parentRoom = parentRoom;
		this.password = password;
	}
	/**
	 * Broadcast a message to all users in the room.
	 * @param message - Message to distribute
	 * @param sourceClient - client from which the message stems, null for a SYS>> message.
	 */
	public void broadcastRoom(String message, ClientData sourceClient){
		for (ClientData client : clients) {
			if (sourceClient != null && !sourceClient.getIsAdmin()) {
				client.getHandler().sendMessage(
						sourceClient.getName() + ">> " + message);
			} else if (sourceClient != null && sourceClient.getIsAdmin()) {
				client.getHandler().sendMessage("ADM>" + sourceClient.getName() + ">> " + message);
			} else {
				client.getHandler().sendMessage("SYS>> " + message);
			}
		}
	}
	/**
	 * Broadcast a message through the current room and all rooms below.
	 * @param message - Message to distribute
	 * @param sourceClient - client from which the message stems, null for a SYS>> message.
	 */
	public void broadcastSubrooms(String message, ClientData sourceClient){
		if(!subRooms.isEmpty()){
			for(Room r : subRooms){
				r.broadcastSubrooms(message, sourceClient);
			}
		}
		broadcastRoom(message, sourceClient);
	}
	/**
	 * Get a current list of all users in a room.
	 * @return - an ordered list of users to be printed.
	 */
	public String roomUsersList(){
		String list = clients.size() + " user";
		if(clients.size() != 1){list += "s";}
		list += " in " + roomName + ": \n SYS>> "; 
		Collections.sort(clients);
		for (ClientData client : clients) {
			if (!client.getIsAdmin())
				list += "  -" + client.getName();
			else
				list += "  -" + client.getName() + "(A)";
		}
		list += "\n SYS>> ";
		return list;
	}
	/**
	 * Set up a list of all users in a room and all subrooms.
	 * @return - an ordered list of users to be printed.
	 */
	public String allRoomUsersList(){
		String list = "Total: " + subRoomUserAmount() + " user";
		if(subRoomUserAmount() != 1){list += "s";}
		list += " online in " + roomName + " and its sub rooms. \n SYS>> ";
		list += roomUsersList();
		if(!subRooms.isEmpty()){
			for(Room r : subRooms){
				list += r.subRoomUsersList();
			}
		}
		return list;
	}
	/**
	 * Assist method for recursion: notes all users in the room and its subrooms.
	 * @return - partial users list
	 */
	public String subRoomUsersList(){
		String list = roomUsersList();
		if(!subRooms.isEmpty()){
			for(Room r : subRooms){
				list += r.subRoomUsersList();
			}
		}
		return list;
	}
	/**
	 * Find the amount of users in a room.
	 * @return all clients in the room
	 */
	public int roomUserAmount(){
		return clients.size();
	}
	/**
	 * Find the amount of users in the room together with all subrooms accounted for.
	 * @return total users in a room and subrooms
	 */
	public int subRoomUserAmount(){
		int size = roomUserAmount();
		if(!subRooms.isEmpty()){
			for(Room r : subRooms){
				size += r.subRoomUserAmount();
			}
		}
		return size;
	}
	/**
	 * The name the room goes by for listing and specifying purposes.
	 * @return - a room's name
	 */
	public String getRoomName() {
		return roomName;
	}
	/**
	 * Sets up an ordened list of rooms that are found linked to a room.
	 * @return - the parent, current and sub rooms respectively
	 */
	public String getAdjacentRoomNames(){
		String names = "";
		if(parentRoom != null)
			names += "Parent: " + parentRoom.getRoomName() + ". \n SYS>> ";
		names += "This room: " + getRoomName() + ". \n SYS>> ";
		if(!subRooms.isEmpty()){
			names += "Subrooms: ";
			Iterator<Room> it = subRooms.iterator();
			while(it.hasNext()){
				Room r = it.next();
				names += r.getRoomName();
				if(it.hasNext())
					names += "; ";
			}
		}
		return names;
	}
	/**
	 * Change room name
	 * @param roomName - name
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	/**
	 * Get the total amount of people allowed inside a room.
	 * @return - capacity
	 */
	public int getRoomSize() {
		return roomSize;
	}
	/**
	 * Change the roomsize
	 * @param roomSize - capacity
	 */
	public void setRoomSize(int roomSize) {
		this.roomSize = roomSize;
	}
	/**
	 * Get the clients in this room.
	 * @return clients specific to this room
	 */
	public ArrayList<ClientData> getClients() {
		return clients;
	}
	/**
	 * Get a list of the users in this room and all of the sub rooms.
	 * @return - list of all clients in and under this room
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ClientData> getAllClients(){
		ArrayList<ClientData> temp = (ArrayList<ClientData>) clients.clone();
		if(!subRooms.isEmpty()){
			for(Room r : subRooms){
				temp.addAll(r.getAllClients());
			}
		}
		return temp;
	}
	/**
	 * Remove a client from this room. Does not automatically bring him to another room. 
	 * Clients automatically goes to a non-existant room. 
	 * @param client - client to deport
	 */
	public synchronized void removeClient(ClientData client){
		client.setCurrentRoom(null);
		clients.remove(client);
	}
	/**
	 * Add a client to this room. Does not automatically remove him from another room.
	 * Clients are automatically updated to move.
	 * @param client - client to welcome
	 */
	public synchronized void addClient(ClientData client){
		client.setCurrentRoom(this);
		clients.add(client);
	}
	/**
	 * Get the rooms located beneath this room.
	 * @return - all rooms created in a room. Is empty when no subrooms present
	 */
	public ArrayList<Room> getSubRooms() {
		return subRooms;
	}
	/**
	 * Add a room to the list of rooms created within this room.
	 * @param subRoom - room to attribute to this room
	 */
	public void addSubRoom(Room subRoom){
		subRooms.add(subRoom);
	}
	/**
	 * Gets the room from which this room stems
	 * @return - room above a room. Returns null once at the top
	 */
	public Room getParentRoom(){
		return parentRoom;
	}
	/**
	 * Get the password for the room.
	 * @return - String format password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Set password.
	 * @param password - String format password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Assign a room to stand above this room.
	 * @param parentRoom - room to associate with this room
	 */
	public void setParentRoom(Room parentRoom) {
		this.parentRoom = parentRoom;
	}

}
