package txtToSpeech;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextToSpeech {

    private AudioPlayer tts;
    private MaryInterface marytts;

    public TextToSpeech() {
        try {
            marytts = new LocalMaryInterface();

        } catch (MaryConfigurationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Collection<Voice> getAvailableVoices() {
        return Voice.getAvailableVoices();
    }

    public void setVoice(String voice) {
        marytts.setVoice(voice);
    }

    public void speak(String text, float gainValue, boolean daemon, boolean join) {
        stopSpeaking();
        try (AudioInputStream audio = marytts.generateAudio(text)) {
            tts = new AudioPlayer();
            tts.setAudio(audio);
            tts.setGain(gainValue);
            tts.setDaemon(daemon);
            tts.start();
            if (join)
                tts.join();
        } catch (SynthesisException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error saying phrase.", ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "IO Exception", ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Interrupted ", ex);
            tts.interrupt();
        }
    }

    public void stopSpeaking() {
        // Stop the previous player
        if (tts != null)
            tts.cancel();
    }
}
