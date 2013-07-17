package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clientHandling.ClientData;
import clientHandling.ClientHandler;

import commandSystem.CommandList;
import environment.Room;


/**
 * Main server for Dionysus. Sets up a pool of threads to handle clients after accepting them.
 * 
 * @author Julian G. West
 * 
 */
public class Server {
	private static ExecutorService service = Executors.newCachedThreadPool();
	private static Room headroom;
	private static boolean isRunning = true;
	private static ServerSocket server;
	/**
	 * Runs the application
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new CommandList();
		headroom = (new Room("Athena", -1, null));
		int port = 8500;
		server = new ServerSocket(port);
		System.out.println("Dionysus -- chat server");
		System.out.println("Online at " + now());
		System.out.println("Location: " + InetAddress.getLocalHost());
		service.submit(new OverrideInput());
		while (isRunning) {
			Socket socket = server.accept();
			service.submit(new ClientHandler(socket));
		}
	}
	/**
	 * Get the current time from where the server is running.
	 * @return - The current time in format yyyy/MM/dd HH:mm
	 */
	public static String now() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return format.format(new Date(System.currentTimeMillis()));
	}
	
	/**
	 * Stops the server and shuts it down.
	 */
	public static void stopRunning() throws IOException{
		System.exit(0);
	}
	/**
	 * Broadcast a message over the entire server
	 * @param message - message to broadcast
	 * @param sourceClient - client from which the message stems, null for a SYS>> message.
	 */
	public static void broadcastServer(String message, ClientData sourceClient){
		headroom.broadcastSubrooms(message, sourceClient);
	}
	/**
	 * Get the very first room in the tree.
	 * @return - The first room (Has no parent)
	 */
	public static Room getHeadroom() {
		return headroom;
	}
	/**
	 * Get all the clients currently connected to the server from the rooms.
	 * @return - all clients
	 */
	public static ArrayList<ClientData> getAllClients(){
		return headroom.getAllClients();
	}
}