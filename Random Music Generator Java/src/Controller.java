//Importing the JavaFX libraries required to access and alter the graphics...
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

import static java.lang.StrictMath.round;

//  MIDI
//  source for a lot of this stuff: https://docs.oracle.com/javase/tutorial/sound/MIDI-synth.html

//The controller class for my JavaFX UI. Also my main class where I will 'call MusicGen' functionality.
public class Controller extends Thread {// implements Initializable {

    //public static int tempo = 60; //bpm
    public static long lastClickMillis = 1000;
    //The FXML objects created and referenced and initialised for use here.
    //@FXML allows the FXMLLoader to inject the pre-defined FXML values into the ones defined here, in the controller class.
    @FXML
    public AnchorPane panelRoot;
    @FXML
    public Sphere graphicBody;
    @FXML
    public Cylinder graphic1;
    @FXML
    public Cylinder graphic2;
    @FXML
    public Cylinder graphic3;
    @FXML
    public Cylinder graphic4;
    @FXML
    public Cylinder graphic5;
    @FXML
    public TextArea helpMenu;
    @FXML
    public Button helpMenuCloseBtn;
    @FXML
    public Button openHelpMenuBtn;
    @FXML
    public Button openInstrumentMenuBtn;
    @FXML
    public Button pausePlayBtn;
    @FXML
    public Button instrumentMenuCloseBtn;
    @FXML
    public Button FFBtn;
    @FXML
    public Button RWBtn;
    @FXML
    public TextArea instrumentMenu;
    @FXML
    public ComboBox voiceDropDown;
    @FXML
    public ComboBox trackDropDown;
    @FXML
    public Button applyInstrumentBtn;
    @FXML
    public Text songNameTxt;
    @FXML
    public ProgressBar progressBar = new ProgressBar();

    //The index for the mood array, determines current mood.
    public int mood = 0;
    //The key IDs for their respective moods...
    public final int HAPPY = 200; //Orange
    public final int CALM = 100; //Green
    public final int LONGING = 101; //Blue
    public final int DISCONTENTMENT = 103; //Grey
    public final int GRIEF = 104; //Black
    public final int RAGE = 205; //Red
    public final int DEATH = 210; //Purple
    public final int HOPE = 212; //Pink
    //These are grouped in an array for access.
    public int[] moods = {HAPPY, CALM, LONGING, DISCONTENTMENT, GRIEF, RAGE, DEATH, HOPE};

    public double progressValue;
    public String songNameText;

    //At runtime, the interface is prepared and the music is starts playing in its own thread.
    @FXML
    public void initialize() {
        //The appropriate objects are hidden/shown for the main menu to be shown.
        helpMenu.setVisible(false);
        helpMenuCloseBtn.setVisible(false);

        instrumentMenu.setVisible(false);
        instrumentMenuCloseBtn.setVisible(false);
        voiceDropDown.setVisible(false);
        trackDropDown.setVisible(false);
        applyInstrumentBtn.setVisible(false);

        //All the instruments/'voices' available are retrieved.
        Voice[] voiceArray = Voice.getVoiceArray();
        //For every voice...
        for (int i = 0; i < voiceArray.length; i++) {
            //The 'voiceDropDown' UI object is filled with the available voice names. (For user selection.)
            voiceDropDown.getItems().add(voiceArray[i].name);
        }
        //The 'trackDropDown' UI object is filled with available 'track' names. (For user selection.)
        trackDropDown.getItems().addAll("Lead", "Chords", "Drums");

        //The initial voice is set for each track, they are unique to be appropriate and auditorily identifiable.
        //Piano voice for lead track.
        MusicManager.setInstrument(0, 0, 0);
        //Clean guitar voice for chord track.
        MusicManager.setInstrument(1, 0, 27);
        //Synth drum voice for drum track.
        MusicManager.setInstrument(2, 0, 118);

        //The initial/default key is set for music generation to begin.
        MusicManager.setKey(moods[mood]);

        progressBar.setStyle("-fx-accent: black;");
        helpMenuCloseBtn.setStyle("-fx-background-color:transparent");
        instrumentMenuCloseBtn.setStyle("-fx-background-color:transparent");
        openHelpMenuBtn.setStyle("-fx-background-color:transparent");
        pausePlayBtn.setStyle("-fx-background-color:transparent");
        openInstrumentMenuBtn.setStyle("-fx-background-color:transparent");
        FFBtn.setStyle("-fx-background-color:transparent");
        RWBtn.setStyle("-fx-background-color:transparent");

        instrumentMenu.setStyle("-fx-control-inner-background:black");
        helpMenu.setStyle("-fx-control-inner-background:black");

        Image pauseBtnImage = new Image("Btn_Icons/PauseBtnIcon2.png");
        pausePlayBtn.setGraphic(new ImageView(pauseBtnImage));
        Image helpBtnImage = new Image("Btn_Icons/HelpBtnIcon2.png");
        openHelpMenuBtn.setGraphic(new ImageView(helpBtnImage));
        Image closeBtnImage = new Image("Btn_Icons/Close_Btn_Icon.png");
        helpMenuCloseBtn.setGraphic(new ImageView(closeBtnImage));
        instrumentMenuCloseBtn.setGraphic(new ImageView(closeBtnImage));
        //Image playBtnImage = new Image("Btn_Icons/PlayBtnIcon2.png");
        //controller.pausePlayBtn.setGraphic(new ImageView(playBtnImage));
        Image instrumentBtnImage = new Image("Btn_Icons/InstrumentBtnIcon.png");
        openInstrumentMenuBtn.setGraphic(new ImageView(instrumentBtnImage));
        Image rwBtnImage = new Image("Btn_Icons/RW_Btn.png");
        RWBtn.setGraphic(new ImageView(rwBtnImage));
        Image ffBtnImage = new Image("Btn_Icons/FF_Btn.png");
        FFBtn.setGraphic(new ImageView(ffBtnImage));

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    MusicManager.playMusic();
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        Thread progressThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    progressValue = MusicManager.getSongProgress();
                    progressBar.setProgress(progressValue);
                    songNameText = MusicManager.getSongName();
                    songNameTxt.setText(songNameText);
                }
            }
        };
        progressThread.start();
    }

    public void main(String args[]) throws MidiUnavailableException {
        //NOTHING HERE?
    }

    //The related objects are shown and ordered in order to display this 'help' menu.
    public void openHelp(MouseEvent mouseEvent) {
        helpMenu.setVisible(true);
        helpMenu.toFront();
        helpMenuCloseBtn.setVisible(true);
        helpMenuCloseBtn.toFront();
    }

    //The related objects are hidden in order to close this 'help' menu.
    public void closeHelp(MouseEvent mouseEvent) {
        helpMenu.setVisible(false);
        helpMenuCloseBtn.setVisible(false);
    }

    //The related objects are shown and ordered in order to display this 'set instrument' menu.
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

    //The related objects are hidden in order to close this 'set instrument' menu.
    public void closeInstrumentMenu(MouseEvent mouseEvent) {
        instrumentMenu.setVisible(false);
        instrumentMenuCloseBtn.setVisible(false);
        trackDropDown.setVisible(false);
        voiceDropDown.setVisible(false);
        applyInstrumentBtn.setVisible(false);
    }

    //This method is attached to the play/pause button on the UI.
    @FXML
    public void playPauseMusic(MouseEvent mouseEvent) throws MidiUnavailableException {
        //The 'isPLaying' value is inverted, this permits or prevents music generation and playing.
        MusicManager.isPlaying = !MusicManager.isPlaying;

        if (MusicManager.isPlaying == false)
        {
            Image playBtnImage = new Image("Btn_Icons/PlayBtnIcon2.png");
            pausePlayBtn.setGraphic(new ImageView(playBtnImage));
        }
        else
        {
            Image pauseBtnImage = new Image("Btn_Icons/PauseBtnIcon2.png");
            pausePlayBtn.setGraphic(new ImageView(pauseBtnImage));
        }
    }

    //This method is attached to the 'apply change' button on the 'set instrument' menu UI. It sets the user selected voice to user selected track.
    public void setInstrumentChange() {
        //Get all voices available.
        Voice[] voiceArray = Voice.getVoiceArray();
        //The string value is retrieved from the DropDown UI object.
        String track = (String) trackDropDown.getValue();
        //The string value is retrieved from the DropDown UI object.
        String instrument = (String) voiceDropDown.getValue();
        //The values that will passed to the Music Player are initialised...
        int trackValue = 0;
        int bankValue = 0;
        int programValue = 0;

        //For each voice available, the selected string is compared to the current voice name...
        for (int i = 0; i < voiceArray.length; i++) {
            if (voiceArray[i].name == instrument) {
                //And if it is found all desired values are set.
                bankValue = voiceArray[i].bank;
                programValue = voiceArray[i].program;
            }
        }
        //Depending on the track the user selected, this value is also passed.
        if (track == "Lead") {
            trackValue = 0;
        }
        if (track == "Chords") {
            trackValue = 1;
        }
        if (track == "Drums") {
            trackValue = 2;
        }
        //The selected voice is set to the selected track in the method these values are passed to.
        MusicManager.setInstrument(trackValue, bankValue, programValue);
        System.out.println("SUCCESSFULLY CHANGED VOICE FOR TRACK. = " + trackValue + ". THE INSTRUMENT = " + bankValue +  ", " + programValue);
    }

    //If a key is pressed a listener calls this method...
    //Select moods are cycled, like they would be on the circle of fifths.
    //'D' to go clockwise and 'A' to reverse.
    @FXML
    void keyChangeFromArrowKeys(KeyEvent keyEvent) {
        //If this key is 'D'...
        if (keyEvent.getCode() == KeyCode.D)
        {
            //And the index value is equal to the maximum mood index...
            if (mood == (moods.length - 1))
            {
                //Then the minimum index is set. (Makes the array go full circle.)
                mood = 0;
            }
            //Otherwise...
            else
            {
                //This index value should be incremented, and the next clockwise mood accessed.
                mood++;
            }
        }
        //Else if this key is 'A'...
        else if (keyEvent.getCode() == KeyCode.A)
        {
            //And the index value is equal to the minimum mood index...
            if (mood == 0)
            {
                //Then the maximum index is set. (Makes the array go full circle.)
                mood = (moods.length - 1);
            }
            //Otherwise...
            else
            {
                //This index value should be decremented, and the next anti-clockwise mood accessed.
                mood--;
            }
        }
        //This mood value (that represents a key ID,) is passed to be identified as a Key, set, and utilised in the MusicManager.
        MusicManager.setKey(moods[mood]);
        //The colour that represents the mood is accessed and assigned to the background 'root panel' with the same index 'mood'.
        String[] moodColours = {"orange", "green", "blue", "grey", "black", "red", "purple", "pink"};
        panelRoot.setStyle("-fx-background-color:" + moodColours[mood]);

        System.out.println("KEY SUCCESSFULLY CHANGED. THE ID = " + (moods[mood]));
    }

    //This method changes the tempo value (milliseconds) and is passed to 'MusicGen'.
    public void tempoChange(MouseEvent mouseEvent) {
        //Ensuring the sphere has returned to the initial size, so that two close clicks do not make a permanent size change...
        if (graphicBody.getScaleX() == 1) {
            //Then the animation can be performed with these values, it will last 0.75 seconds and scale to this degree.
            tempoAnim(75, 0.15);
        }

        //The time is taken since the last click, the difference in time since the last click is taken.
        lastClickMillis = (System.currentTimeMillis() - lastClickMillis);
        //The tempo must be kept within sensible values... (300bpm to 20bpm).
        if (lastClickMillis > 50 && lastClickMillis < 3000) {  //// > 250
            //Pass the millisecond value to the music playing class to indicate how long the thread should sleep for before the next note should be played.
            MusicManager.tempo = ((int) lastClickMillis);
            System.out.println("TEMPO SUCCESSFULLY CHANGED. THE TEMPO (in milliseconds) = " + (int) lastClickMillis);
        }
        //The time of this click is taken.
        lastClickMillis = System.currentTimeMillis();
    }

    //This method controls an animation for the graphic body.
    public void tempoAnim(int timeLength, double scaleAmount) {
        //The scale animation/transition is made for the object...
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(timeLength), graphicBody);
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
}