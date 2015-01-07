import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlaySound {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		InputStream is = PlaySound.class.getResourceAsStream("/Bleep.wav");
		
		
		  AudioInputStream sound = AudioSystem.getAudioInputStream(is);
          // load the sound into memory (a Clip)
		  Clip clip = AudioSystem.getClip();
             clip.open(sound);
             
             clip.setFramePosition(0);  // Must always rewind!
             clip.start();
       
		//clip.close();

	}
}
