package commandSystem;

import clientHandling.ClientData;
/**
 * Abstract class that sets up single character chat commands.
 * @author Julian G. West
 *
 */
public abstract class CharCommand extends Command<Character, ClientData>{
	private boolean isAdminCommand;
	/**
	 * Constructor initialising a possible command.
	 * @param key - the character that triggers the command.
	 * @param isAdminCommand - is admin access required to execute the command.
	 */
	public CharCommand(char key, boolean isAdminCommand){
		this.key = key;
		this.isAdminCommand = isAdminCommand;
	}
	
	/**
	 * Validates whether a user is allowed to make use of the command.
	 * @param client - The client to validate access of.
	 * @return - True if allowed, false if not.
	 */
	public boolean validateUse(ClientData client){
		boolean isAllowed = true;
		if(!client.getIsAdmin() && isAdminCommand)
			isAllowed = false;
		return isAllowed;
	}
	
}
