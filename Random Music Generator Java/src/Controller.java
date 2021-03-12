//Importing the JavaFX libraries required to access and alter the graphics...
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;
//Importing MIDI library for the MIDI unavailable exception.
import javax.sound.midi.*;
import javax.swing.*;

//  MIDI
//  source for a lot of this stuff: https://docs.oracle.com/javase/tutorial/sound/MIDI-synth.html

//The controller class for my JavaFX UI. Also my main class where I will 'call MusicGen' functionality.
public class Controller extends Thread {// implements Initializable {

    public static long tempo = 60; //bpm
    public static long lastClickMillis;
    //The FXML objects created and referenced and initialised for use here.
    @FXML
    public AnchorPane panelRoot;
    @FXML
    public Sphere icon;
    @FXML
    public Cylinder icon1;
    @FXML
    public Cylinder icon2;
    @FXML
    public Cylinder icon3;
    @FXML
    public Cylinder icon4;
    @FXML
    public Cylinder icon5;
    @FXML
    public TextArea helpMenu;
    @FXML
    public Button helpMenuCloseBtn;
    @FXML
    public TextArea instrumentMenu;
    @FXML
    public Button instrumentMenuCloseBtn;
    @FXML
    public ComboBox voiceDropDown;
    @FXML
    public ComboBox trackDropDown;
    @FXML
    public Button applyInstrumentBtn;
    @FXML
    public static Text songNameTxt;
    @FXML
    public static ProgressBar progressBar = new ProgressBar();

    static boolean playing = true;

    public void main(String args[]) throws MidiUnavailableException {
        musicPlayerThread thread = new musicPlayerThread();
        thread.start();
        MusicManager.musicPlayer((int) tempo);
        //NOTHING HAPPENS....... :3
    }

    //This method changes the tempo value and is passed to 'MusicGen'.
    public void tempoChange(MouseEvent mouseEvent) {
        ////////////////////////////////////////////////////////////////////////////////////Make group?
        //Ensuring the sphere has returned to the initial size, so that two close clicks do not make a permanent size change...
        if (icon.getScaleX() == 1) {
            //Then the animation can be performed with these values, it will last 0.75 seconds and scale to this degree.
            tempoAnim(75, 0.15);
        }

        //The time is taken since the last click, the difference in time since the last click is taken.
        lastClickMillis = (System.currentTimeMillis() - lastClickMillis);
        //The tempo must be kept within sensible values... (300bpm to 20bpm).
        if (lastClickMillis > 250 || lastClickMillis < 3000) {
            //Then it is passed to MusicGen to be set.
            //The tempo is converted to a bpm value and is passed as an int.
            MusicManager.tempo = (int) (60 / (lastClickMillis / 1000));
        }
        //I DON'T NEED THIS... RIGHT?
        //lastClickMillis = System.currentTimeMillis();
    }

    //This method controls an animation for the graphic icon.
    public void tempoAnim(int timeLength, double scaleAmount) {
        //The scale animation/transition is made for the object...
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(timeLength), icon);
        //The amount it should scale in respective directions is set.
        scaleTransition.setByX(scaleAmount);
        scaleTransition.setByY(scaleAmount);
        scaleTransition.setByZ(scaleAmount);
        //The animation will reverse and will attempt to return the object to it's original state.
        //In this case the object is shrinking back to normal.
        scaleTransition.setAutoReverse(true);
        //It will perform 2 'animations'/cycles.
        scaleTransition.setCycleCount(2);
        //It plays and reverses.
        scaleTransition.play();
    }

    public static void progressBarSongLength() {
        progressBar.setProgress(MusicManager.getSongProgress());
    }

    @FXML
    public void initialize() throws MidiUnavailableException {
        helpMenu.setVisible(false);
        helpMenuCloseBtn.setVisible(false);

        instrumentMenu.setVisible(false);
        instrumentMenuCloseBtn.setVisible(false);
        voiceDropDown.setVisible(false);
        trackDropDown.setVisible(false);
        applyInstrumentBtn.setVisible(false);

        Voice[] voiceArray = Voice.getVoiceArray();
        int numOfInstruments = voiceArray.length;
        for (int i = 0; i < numOfInstruments; i++) {
            voiceDropDown.getItems().add(voiceArray[i].name);
        }
        trackDropDown.getItems().addAll("Lead", "Chords", "Drums");

        ///////////////////////////////////////////////////////////////////////////musicPlayerThread.run((int) tempo);
        //MusicManager.musicPlayer((int) tempo);
        System.out.println("PLEASE PLEASE GET TO THIS POINT");
    }

    @FXML
    public void pauseMusic(KeyEvent key) {
        switch (key.getCode()) {
            case SPACE:
                System.out.println("HA! you thought I could work. You were wrong idiot.");
                break;
            default:
                break;
        }
    }

    @FXML
    public void playPauseMusic(MouseEvent mouseEvent) throws MidiUnavailableException {
        playing = !playing;
        if (playing == true) {
            System.out.println("THIS IS PLAYING");
            MusicManager.isPlaying = true;
        } else {
            System.out.println("THIS IS PAUSED");
            MusicManager.isPlaying = false;
        }
    }

    @FXML
    public void setKey() {
        //MusicManager.setKey();
    }

    public void openHelp(MouseEvent mouseEvent) {
        helpMenu.setVisible(true);
        helpMenu.toFront();
        helpMenuCloseBtn.setVisible(true);
        helpMenuCloseBtn.toFront();
    }

    public void closeHelp(MouseEvent mouseEvent) {
        helpMenu.setVisible(false);
        helpMenuCloseBtn.setVisible(false);
    }

    public void openInstrumentMenu(MouseEvent mouseEvent) {
        instrumentMenu.setVisible(true);
        instrumentMenu.toFront();
        instrumentMenuCloseBtn.setVisible(true);
        instrumentMenuCloseBtn.toFront();
        trackDropDown.setVisible(true);
        trackDropDown.toFront();
        voiceDropDown.setVisible(true);
        voiceDropDown.toFront();
        applyInstrumentBtn.setVisible(true);
        applyInstrumentBtn.toFront();
    }

    public void closeInstrumentMenu(MouseEvent mouseEvent) {
        instrumentMenu.setVisible(false);
        instrumentMenuCloseBtn.setVisible(false);
        trackDropDown.setVisible(false);
        voiceDropDown.setVisible(false);
        applyInstrumentBtn.setVisible(false);
    }

    public void setInstrumentChange() {
        Voice[] voiceArray = Voice.getVoiceArray();
        String track = (String) trackDropDown.getValue();
        String instrument = (String) voiceDropDown.getValue();
        int trackValue = 0;
        int bankValue = 0;
        int programValue = 0;

        for (int i = 0; i < voiceArray.length; i++) {
            if (voiceArray[i].name == instrument) {
                bankValue = voiceArray[i].bank;
                programValue = voiceArray[i].program;
            }
        }
        if (track == "Lead") {
            trackValue = 0;
        }
        if (track == "Chords") {
            trackValue = 1;
        }
        if (track == "Drums") {
            trackValue = 2;
        }
        MusicManager.setInstrument(trackValue, bankValue, programValue);
    }

}

//WITH lastTempoClickNewTime...
/*
//This method changes the tempo value and is passed to 'MusicGen'.
    public void tempoChange(MouseEvent mouseEvent) {
        ////////////////////////////////////////////////////////////////////////////////////Make group?
        //Ensuring the sphere has returned to the initial size, so that two close clicks do not make a permanent size change...
        if (icon.getScaleX() == 1) {
            //Then the animation can be performed with these values, it will last 0.75 seconds and scale to this degree.
            growSphereAnim(75, 0.15);
        }

        //The time is taken since the last click, the difference in time since the last click is taken.
        lastTempoClickNewTime = (System.currentTimeMillis() - lastTempoClickMillis);
        //The tempo must be kept within sensible values... (300bpm to 20bpm).
        if (lastTempoClickNewTime > 250 || lastTempoClickNewTime < 3000) {
            //Then it is passed to MusicGen to be set.
            //The tempo is converted to a bpm value and is passed as an int.
            MusicManager.tempo = (int) (60 / (lastTempoClickNewTime/1000));
        }
        //I DON'T NEED THIS... RIGHT?
        //lastTempoClickMillis = System.currentTimeMillis();
    }
 */

























//INTERFACE

        /*
        MusicGen musicGen = new MusicGen() {

            @Override
            public void musicPlayer(int tempo) throws MidiUnavailableException {

            }

            @Override
            public void setKey(int chosenKeyID) {

            }

            @Override
            public Key getKey() {
                return null;
            }

            @Override
            public void setInstrument(int trackNumber, int bankValue, int programValue) {

            }

            @Override
            public void playMusic() {

            }

            @Override
            public int getTimeSigTop() {
                return 0;
            }
        };
    */