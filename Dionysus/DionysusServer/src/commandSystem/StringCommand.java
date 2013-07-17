package commandSystem;
/**
 * Abstract class that sets up full word commands.
 * @author Julian G. West
 *
 */
public abstract class StringCommand extends Command<String, String>{
		/**
		 * Constructor initialising a full word triggered command.
		 * @param key - keyword
		 */
		public StringCommand(String key){
			this.key = key;
		}
}
