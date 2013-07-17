package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import commandSystem.CommandList;
import commandSystem.StringCommand;


/**
 * Console input class for the server.
 * Takes commands and broadcasts messages to all clients under the name "SYS>>"
 * @author Julian G. West
 *
 */
public class OverrideInput extends Thread implements Runnable {
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	/**
	 * Gets to work.
	 */
	public void run() {
		while (true) {
			String message = "";
			try {
				message = reader.readLine();
				if (message.charAt(0) != '-') {
					Server.broadcastServer(message, null);
				} else if(message.length() > 1){
					for(StringCommand c : CommandList.getServerCommands()) {
						if (message.contains(c.getKey())) {
							c.command(message);
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Wow, this is really unexpected.");
			}
		}
	}
}
