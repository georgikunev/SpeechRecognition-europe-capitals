package Model;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import txtToSpeech.TextToSpeech;

public class SpeechRecognizerMain {
    private LiveSpeechRecognizer recognizer;
    TextToSpeech textToSpeech = new TextToSpeech();
    private Logger logger = Logger.getLogger(getClass().getName());

    private String speechRecognitionResult;

    private boolean ignoreSpeechRecognitionResults = false;
    private boolean speechRecognizerThreadRunning = false;

    private boolean resourcesThreadRunning;
    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);

    public SpeechRecognizerMain() {

        // Loading Message
        logger.log(Level.INFO, "Loading Speech Recognizer...\n");

        // Configuration
        Configuration configuration = new Configuration();

        // Load model from the jar
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        //Check if needed resources are available
        startResourcesThread();
        //Start speech recognition thread
        startSpeechRecognition();
    }
    public synchronized void startSpeechRecognition() {

        if (speechRecognizerThreadRunning)
            logger.log(Level.INFO, "Speech Recognition Thread already running...\n");
        else
            //submit to ExecutorService
            eventsExecutorService.submit(() -> {

                speechRecognizerThreadRunning = true;
                ignoreSpeechRecognitionResults = false;

                //Start Recognition
                recognizer.startRecognition(true);

                logger.log(Level.INFO, "You can start to speak...\n");

                try {
                    while (speechRecognizerThreadRunning) {

                        SpeechResult speechResult = recognizer.getResult();

                        if (!ignoreSpeechRecognitionResults) {

                            //Check the result
                            if (speechResult == null)
                                logger.log(Level.INFO, "I can't understand what you said.\n");
                            else {
                                speechRecognitionResult = speechResult.getHypothesis();

                                System.out.println("You said: [" + speechRecognitionResult + "]\n");

                                makeDecision(speechRecognitionResult);
                            }
                        } else
                            logger.log(Level.INFO, "Ignoring Speech Recognition Results...");
                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                    logger.log(Level.WARNING, "Invalid sentence!");
                    speechRecognizerThreadRunning = false;
                    logger.log(Level.WARNING, "Please enter a valid sentence!");

                }
                logger.log(Level.INFO, "SpeechThread has exited...");

            });
    }
    public synchronized void stopIgnoreSpeechRecognitionResults() {
        ignoreSpeechRecognitionResults = false;
    }
    public synchronized void ignoreSpeechRecognitionResults() {
        ignoreSpeechRecognitionResults = true;
    }
    public void startResourcesThread() {

        if (resourcesThreadRunning)
            logger.log(Level.INFO, "Resources Thread already running...\n");
        else
            eventsExecutorService.submit(() -> {
                try {
                    resourcesThreadRunning = true;
                    // Detect if the microphone is available
                    while (true) {
                        if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE))
                            logger.log(Level.INFO, "Microphone is not available.\n");
                        Thread.sleep(350);
                    }

                } catch (InterruptedException ex) {
                    logger.log(Level.WARNING, null, ex);
                    resourcesThreadRunning = false;
                }
            });
    }
    public void makeDecision(String speech) {

        String country = "";
        String result = "";
        boolean isCountry = true;

        if (speech.contains("bulgaria")) {
            country = "Bulgaria";
            result = "Sofia";
        } else if (speech.contains("germany")) {
            country = "Germany";
            result = "Berlin";
        } else if (speech.contains("france")) {
            country = "France";
            result = "Paris";
        } else if (speech.contains("united kingdom")) {
            country = "United kingdom";
            result = "London";
        } else if (speech.contains("ireland")) {
            country = "Ireland";
            result = "Dublin";
        } else if (speech.contains("poland")) {
            country = "Poland";
            result = "Warsaw";
        } else if (speech.contains("sweden")) {
            country = "Sweden";
            result = "Stockholm";
        } else if (speech.contains("norway")) {
            country = "Norway";
            result = "Oslo";
        } else if (speech.contains("romania")) {
            country = "romania";
            result = "bucharest";
        } else if (speech.contains("finland")) {
            country = "Finland";
            result = "Helsinki";
        } else if (speech.contains("denmark")) {
            country = "Denmark";
            result = "Copenhagen";
        } else if (speech.contains("spain")) {
            country = "Spain";
            result = "Madrid";
        } else if (speech.contains("portugal")) {
            country = "Portugal";
            result = "Lisbon";
        } else if (speech.contains("estonia")) {
            country = "Estonia";
            result = "Tallinn";
        } else if (speech.contains("ukraine")) {
            country = "Ukraine";
            result = "Kyiv";
        } else if (speech.contains("slovakia")) {
            country = "Slovakia";
            result = "Bratislava";
        } else if (speech.contains("netherlands")) {
            country = "Netherlands";
            result = "Amsterdam";
        } else if (speech.contains("belgium")) {
            country = "Belgium";
            result = "Brussels";
        } else if (speech.contains("czech republic")) {
            country = "Czech Republic";
            result = "Prague";
        } else if (speech.contains("albania")) {
            country = "Albania";
            result = "Tirana";
        } else if (speech.contains("andorra")) {
            country = "Andorra";
            result = "Andorra La Vella";
        } else if (speech.contains("austria")) {
            country = "Austria";
            result = "Vienna";
        } else if (speech.contains("belarus")) {
            country = "Belarus";
            result = "Minsk";
        } else if (speech.contains("bosnia and herzegovina")) {
            country = "Bosnia and Herzegovina";
            result = "Sarajevo";
        } else if (speech.contains("croatia")) {
            country = "Croatia";
            result = "Zagreb";
        } else if (speech.contains("greece")) {
            country = "Greece";
            result = "Athens";
        } else if (speech.contains("hungary")) {
            country = "Hungary";
            result = "Budapest";
        } else if (speech.contains("iceland")) {
            country = "Iceland";
            result = "Reykjavik";
        } else if (speech.contains("italy")) {
            country = "Italy";
            result = "Rome";
        } else if (speech.contains("latvia")) {
            country = "Latvia";
            result = "Riga";
        } else if (speech.contains("liechtenstein")) {
            country = "Liechtenstein";
            result = "Vaduz";
        } else if (speech.contains("lithuania")) {
            country = "Lithuania";
            result = "Vilnius";
        } else if (speech.contains("luxembourg")) {
            country = "Luxembourg";
            result = "Luxembourg";
        } else if (speech.contains("malta")) {
            country = "Malta";
            result = "Valletta";
        } else if (speech.contains("moldova")) {
            country = "Moldova";
            result = "Chisinau";
        } else if (speech.contains("monaco")) {
            country = "Monaco";
            result = "Monaco";
        } else if (speech.contains("montenegro")) {
            country = "Montenegro";
            result = "Podgorica";
        } else if (speech.contains("north macedonia")) {
            country = "North Macedonia";
            result = "Skopje";
        } else if (speech.contains("san marino")) {
            country = "San Marino";
            result = "San Marino";
        } else if (speech.contains("serbia")) {
            country = "Serbia";
            result = "Belgrade";
        } else if (speech.contains("slovenia")) {
            country = "Slovenia";
            result = "Ljubljana";
        } else if (speech.contains("germany")) {
            country = "Germany";
            result = "Berlin";
        } else if (speech.contains("russia")) {
            country = "Russia";
            result = "Moscow";
        } else if (speech.contains("kosovo")) {
            country = "Kosovo";
            result = "Pristina";
        } else if (speech.contains("stop")) {
            System.exit(0);
        } else {
            isCountry = false;
        }
        if (isCountry) {
            System.out.println("The capital of " + country + " is " + result);
            textToSpeech.speak("The capital of " + country + " is " + result, 1.5f, false, true);
        } else {
            System.out.println("Please repeat!");
            textToSpeech.speak("Please repeat", 1.5f, false, true);
        }

    }

    public boolean getIgnoreSpeechRecognitionResults() {
        return ignoreSpeechRecognitionResults;
    }

    public boolean getSpeechRecognizerThreadRunning() {
        return speechRecognizerThreadRunning;
    }
    public static void main(String[] args) {
        new SpeechRecognizerMain();
    }
}