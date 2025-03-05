import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Music {


    public void play(String filePath) {
        try {
            // Open an audio input stream.
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();

            // keep the program running until the audio clip is finished playing.
            while (!clip.isRunning())
                Thread.sleep(10);
            while (clip.isRunning())
                Thread.sleep(10);

            clip.close();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Audio file format not supported: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading the audio file: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line for playing back is unavailable: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Playback interrupted: " + e.getMessage());
        }
    }
}
