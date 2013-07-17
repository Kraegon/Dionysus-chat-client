package commandSystem;

/**
 * Generic abstract class to umbrella the various commands.
 * 
 * @author Julian G. West
 *
 * @param <E> The key to find the command by.
 * @param <O> The specific data that the command can use.
 */
public abstract class Command<E, O> {
	E key;
	
	/**
	 * Command to be executed. Can use extra parameter O.
	 * @param O - The client to send responses to.
	 */
	public abstract void command(O data);
	
	/**
	 * Return the key to find the command by.
	 * @return Key of type E (That rhymes)
	 */
	public E getKey(){
		return key;
	}
	/**
	 * Method to help list the command in the help list.
	 */
	public abstract String toString();
}
