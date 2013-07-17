package commandSystem;

import java.io.IOException;
import java.util.LinkedList;

import clientHandling.ClientData;

import server.Server;

import environment.Room;

/**
 * Class to store all viable textual commands in.
 * @author Julian G. West
 *
 */
public class CommandList {
	static LinkedList<CharCommand> clientCommands = new LinkedList<CharCommand>();
	static LinkedList<StringCommand> serverCommands = new LinkedList<StringCommand>();
	/**
	 * Constructor setting the commands in advance.
	 */
	public CommandList(){
		createCommands();
	}
	/**
	 * Sets up each command individually with it's proper key and command,
	 * then adds it to the respective list for console or client use.
	 */
	public void createCommands(){
		serverCommands.add(new StringCommand("admin") {
			@Override
			public void command(String message) {
				if(message.length() <= 7)
					return;
				String adminName = message.substring(7);
				boolean isFound = false;
				for (ClientData client : Server.getAllClients()) {
					if (client.getName().equals(adminName)) {
						client.toggleIsAdmin();
						client.getCurrentRoom().broadcastRoom(client.getName()
								+ " has been chosen as new admin. ", null);
						isFound = true;
					}
				}
				if(!isFound)
					System.out.println("User not found.");
				else
					System.out.println("Succesfully adminized.");
			}

			@Override
			public String toString() {
				return "-admin [name]: Elevate person to admin.";
			}
		});
		serverCommands.add(new StringCommand("kick") {
			@Override
			public void command(String message) {
				if(message.length() <= 6)
					return;
				String kickName = message.substring(6);
				ClientData toKick = null;
				for (ClientData client : Server.getAllClients()) {
					if (client.getName().equals(kickName)) {
						toKick = client;
					}
				}
				if (toKick == null)
					System.out.println("SYS>> User not found.");
				else {
					toKick.getHandler().sendMessage(
							"SYS>> You have been kicked from the server.");
					toKick.getCurrentRoom().broadcastRoom("Dearest " + toKick.getName()
							+ " has been kicked.", null);
					toKick.getCurrentRoom().removeClient(toKick);
				}
			}

			@Override
			public String toString() {
				return "-kick [name]: Kick annoying people.";
			}
		});
		serverCommands.add(new StringCommand("shutdown") {
			@Override
			public void command(String information) {
				System.out.println("Shutting down Dionysus chat server");
				try {
					Server.stopRunning();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String toString() {
				return "-shutdown: Shutdown chat server.";
			}
		});
		serverCommands.add(new StringCommand("users") {
			@Override
			public void command(String data) {
				System.out.println(Server.getHeadroom().allRoomUsersList());
			}
			@Override
			public String toString() {
				return "-users: View all online users.";
			}
		});
		serverCommands.add(new StringCommand("help") {
			@Override
			public void command(String information) {
				for (StringCommand c : serverCommands) {
					System.out.println(c.toString());
				}
			}

			@Override
			public String toString() {
				return "-help: View this list of commands.";
			}

		});
		clientCommands.add(new CharCommand('e', false) {
			@Override
			public void command(ClientData client) {
				client.getHandler().sendCharacter('µ');
				System.out.println(client.getName() + " shutdown at "
						+ Server.now());
				client.getCurrentRoom().broadcastRoom(client.getName() + " has logged off.",
						null);
				client.getCurrentRoom().removeClient(client);
			}

			@Override
			public String toString() {
				return "SYS>> -e : Exit";
			}
		});
		clientCommands.add(new CharCommand('u', false) {
			@Override
			public void command(ClientData client) {
				client.getHandler().sendMessage("SYS>> " + client.getCurrentRoom().allRoomUsersList());		
			}

			@Override
			public String toString() {
				return "SYS>> -u : Userlist of this room and all sub rooms.";
			}
		});
		clientCommands.add(new CharCommand('t', false) {
			@Override
			public void command(ClientData client) {
				client.getHandler().sendMessage("SYS>> " + Server.now());
			}

			@Override
			public String toString() {
				return "SYS>> -t : Servertime";
			}
		});
		clientCommands.add(new CharCommand('a', true) {
			@Override
			public void command(ClientData client) {
				String adminName;
				adminName = client.getHandler().receiveMessage();
				ClientData toAdmin = null;
				for (ClientData client2 : client.getCurrentRoom().getClients()) {
					if (client2.getName().equals(adminName)) {
						toAdmin = client2;
					}
				}
				if (toAdmin == null)
					client.getHandler().sendMessage("SYS>> User not found.");
				else {
					toAdmin.toggleIsAdmin();
					client.getCurrentRoom().broadcastRoom(
							toAdmin.getName()
									+ " has been chosen as new admin by "
									+ client.getName() + ".", null);
				}
			}

			@Override
			public String toString() {
				return "SYS>> -a : Elect admin";
			}
		});
		clientCommands.add(new CharCommand('k', true) {
			@Override
			public void command(ClientData client) {
				String kickName;
				client.getHandler().sendMessage(
						"SYS>> Who would you like to kick?");
				kickName = client.getHandler().receiveMessage();
				ClientData toKick = null;
				for (ClientData client2 : client.getCurrentRoom().getClients()) {
					if (client2.getName().equals(kickName)) {
						toKick = client2;
					}
				}
				if (toKick == null)
					client.getHandler().sendMessage("SYS>> User not found.");
				else {
					System.out.println(client.getName() + " has kicked "
							+ toKick.getName() + " at " + Server.now());
					toKick.getHandler().sendMessage(
							"SYS>> You have been kicked from the server.");
					client.getCurrentRoom().broadcastRoom("Dearest " + toKick.getName()
							+ " has been kicked.", null);
					client.getCurrentRoom().removeClient(toKick);
				}
			}

			@Override
			public String toString() {
				return "SYS>> -k : Kick";
			}
		});
		clientCommands.add(new CharCommand('c', true) {
			@Override
			public void command(ClientData client) {
				client.getCurrentRoom().broadcastRoom(client.getName()
						+ " has called to arms.", null);
			}

			@Override
			public String toString() {
				return "SYS>> -c : Call to Arms";
			}
		});
		clientCommands.add(new CharCommand('m', false) {
			@Override
			public void command(ClientData client) {
				client.getHandler().sendCharacter('õ');
			}

			@Override
			public String toString() {
				return "SYS>> -m : Mute/Unmute";
			}
		});
		clientCommands.add(new CharCommand('d', false) {
			@Override
			public void command(ClientData client) {
				client.getHandler().sendCharacter('©');
			}

			@Override
			public String toString() {
				return "SYS>> -d : See logo";
			}
		});
		clientCommands.add(new CharCommand('h', false) {
			@Override
			public void command(ClientData client) {
				for(CharCommand c : clientCommands){
					if(c.validateUse(client))
						client.getHandler().sendMessage(c.toString());
				}
			}

			@Override
			public String toString() {
				return "SYS>> -h : Helpscreen";
			}
		});
		clientCommands.add(new CharCommand('r', true) {
			@Override
			public void command(ClientData client) {
				String roomName;
				char usePass;
				String password = "";
				client.getHandler().sendMessage("SYS>> What will be the new rooms name?");
				roomName = client.getHandler().receiveMessage();
				client.getHandler().sendMessage("SYS>> Do you wish to use a password? y/n");
				usePass = client.getHandler().receiveMessage().toLowerCase().charAt(0);
				if(usePass == 'y'){
					client.getHandler().sendMessage("SYS>> What will be the password?");
					client.getHandler().sendCharacter('^');
					password = client.getHandler().receiveMessage();
					if(password.isEmpty())
						client.getHandler().sendMessage("SYS>> Password cancelled.");
				} else if(usePass != 'n'){
					return;
				}
				if(roomName.length() > 1){
					if(password.isEmpty())
						client.getCurrentRoom().addSubRoom(new Room(roomName, -1, client.getCurrentRoom()));
					else
						client.getCurrentRoom().addSubRoom(new Room(roomName, -1, client.getCurrentRoom(), password));
					client.getCurrentRoom().broadcastRoom(client.getName() + " has created a new subroom: " + roomName, null);
				} else {
					client.getHandler().sendMessage("SYS>> Roomname too short.");
				}
			}
			@Override
			public String toString() {
				return "SYS>> -r : Create a room under the current room you are in.";
			}
		});	
		clientCommands.add(new CharCommand('l', false){
			@Override
			public void command(ClientData client) {
				client.getHandler().sendMessage("SYS>> " + client.getCurrentRoom().getAdjacentRoomNames());
			}

			@Override
			public String toString() {
				return "SYS>> -l : List adjacent rooms.";
			}
			
		});
		/*clientCommands.add(new CharCommand('n', true) {
			
			@Override
			public void command(ClientData client) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public String toString() {
				return "SYS>> -n : Change the name of the current room. Not functional";
			}
		});*/
		clientCommands.add(new CharCommand('p', false){
			@Override
			public void command(ClientData client) {
				String roomName;
				client.getHandler().sendMessage("SYS>> " + client.getCurrentRoom().getAdjacentRoomNames());
				client.getHandler().sendMessage("SYS>> Which room is your destination?");
				roomName = client.getHandler().receiveMessage().toLowerCase();
				Room destination = null;
				for(Room r : client.getCurrentRoom().getSubRooms()){
					if(r.getRoomName().toLowerCase().equals(roomName)){
						destination = r;
					}
				}
				if(client.getCurrentRoom().getParentRoom() != null){
					if(client.getCurrentRoom().getParentRoom().getRoomName().toLowerCase().equals(roomName)){
						destination = client.getCurrentRoom().getParentRoom();
					} 
				}
				if(destination != null){
					if(destination.getPassword() != null){
						client.getHandler().sendMessage("SYS>> Password required: ");
						client.getHandler().sendCharacter('^');
						String input = client.getHandler().receiveMessage();
						if(!destination.getPassword().equals(input)){
							client.getHandler().sendMessage("SYS>> Password incorrect.");
							return;
						}
						client.getHandler().sendMessage("SYS>> Password correct.");
					}
					client.getCurrentRoom().broadcastRoom(client.getName() + " has moved." , null);
					destination.broadcastRoom(client.getName() + " has entered the room.", null);
					client.getCurrentRoom().removeClient(client);
					destination.addClient(client);
				} else {
					client.getHandler().sendMessage("SYS>> Could not find room.");
				}	
			}
			@Override
			public String toString() {
				return "SYS>> -p : Phase to another room, adjacent to this room.";
			}
			
		});
		/*clientCommands.add(new CharCommand('D', true){

			@Override
			public void command(ClientData client) {
				//TODO
				client.getHandler().sendMessage("SYS>> " + client.getCurrentRoom().getAdjacentRoomNames());
				client.getHandler().sendMessage("SYS>> Pick a subroom to destroy.");
				
			}

			@Override
			public String toString() {
				return "SYS>> -D : Destroy a subroom of this room. Not functional";
			}	
		});*/
	}
	/**
	 * Get all available single character commands, for use with the chat client's input.
	 * @return - commands
	 */
	public static LinkedList<CharCommand> getCharacterCommands() {
		return clientCommands;
	}
	/**
	 * Get all available string commands, for use with the server console.
	 * @return - commands
	 */
	public static LinkedList<StringCommand> getServerCommands() {
		return serverCommands;
	}
}
