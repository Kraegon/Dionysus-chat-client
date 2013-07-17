package client;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
/**
 * Sound player for midi files in /sound/
 * @author Julian G. West
 *
 */
public class Sound {
	private static Sequencer SEQUENCER;
	
	private static boolean isMute = false;
	
	final static int LOGON = 1;
	final static int MESSAGE = 2;
	final static int BUZZER = 3;
	/**
	 * Initialises the sequencer.
	 */
	public Sound(){
		initSeq();
	}
	/**
	 * Assist method. Initialises the sequencer.
	 */
	private static void initSeq(){
		try{
			SEQUENCER = MidiSystem.getSequencer();
			SEQUENCER.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Play specific sequence from the /sound/ folder.
	 * @param sound - Specified by static integers in this class, corresponds with midi files.
	 */
	public static void playSeq(int sound){
		if(!isMute()){
			Sequence sequence;
			try {
				switch(sound){
				case 1: 
					sequence = MidiSystem.getSequence(new File("Sounds/Log on.mid"));
					break;
				case 2:
					sequence = MidiSystem.getSequence(new File("Sounds/Message.mid"));
					break;
				case 3:
					sequence = MidiSystem.getSequence(new File("Sounds/Buzzer.mid"));
					break;
				default:
					sequence = null;
				}
				SEQUENCER.setSequence(sequence);
				SEQUENCER.start();
			} catch (InvalidMidiDataException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Play sounds or don't play sounds.
	 * @return - True if no sounds are to be played.
	 */
	public static boolean isMute() {
		return isMute;
	}

	/**
	 * Mutes the soundFX on the client.
	 */
	public static void toggleIsMute() {
		isMute = !isMute;
	}
}
