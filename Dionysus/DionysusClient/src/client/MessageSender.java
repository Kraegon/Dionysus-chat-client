package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.Timer;
/**
 * Sending class that sends data over the socket to the server.
 * Also contains a pinger to stop the program automatically if the server isn't connected.
 * @author Julian G. West
 *
 */
public class MessageSender{
	static DataOutputStream out;
	/**
	 * Only viable constructor. Output stream of the socket is used here.
	 * @param out - final DataOutputStream. Made final for the use in the pinger.
	 */
	public MessageSender(final DataOutputStream out){
		MessageSender.out = out;
		Timer pinger = new Timer(10000, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeChar('0');
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		pinger.start();
	}
	/**
	 * Sends the message to the server for handling. Uses my personal protocol.
	 * @param message - message that gets broadcast to the server.
	 */
	public static void sendMessage(String message){
		try {
			out.writeChar('*');
			char[] temp = message.toCharArray();
			for(int i = 0; i < temp.length; i++){
				out.writeChar(temp[i]);
			}
			out.writeChar('þ');		
		} catch (IOException e) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				System.out.println("ERR>> Not so fast!");
			}
			e.printStackTrace();
		}
	}
	/**
	 * Assist method with protocol to transmit user specified name.
	 * 
	 * @param name
	 */
	public static void sendName(String name) {
		try {
			out.writeChar('$');
			char[] temp = name.toCharArray();
			for (int i = 0; i < temp.length; i++) {
				out.writeChar(temp[i]);
			}
			out.writeChar('%');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}