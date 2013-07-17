package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import console.Frame;
/**
 * Listener to read input. Backspace removes characters and enters sends the string.
 * @author Julian G. West
 *
 */
public class Keyboard implements KeyListener{
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case 8:
			Frame.getInstance().descendGigaPanel().removeCharacter();
			break;
		case 10:
			Frame.getInstance().descendGigaPanel().sendLine();
			break;
		case 37://LEFT
			//Frame.getInstance().descendGigaPanel().movePointer(-1);
			break;
		case 38://UP
			//Optional: message history
			break;
		case 39://RIGHT
			//Frame.getInstance().descendGigaPanel().movePointer(1);
			break;
		case 40://DOWN
			//Optional: message history
			break;
		default:
			key = (int) e.getKeyChar();
			if(Character.isDefined(key))
				Frame.getInstance().descendGigaPanel().addCharacter((char) key);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
