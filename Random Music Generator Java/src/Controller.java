//Importing the JavaFX libraries required to access and alter the graphics...
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
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

    //The index for the mood array, determines current mood.
    public int mood = 0;
    //The key IDs for their respective moods...
    public final int HAPPY = 200; //Yellow
    public final int CALM = 100; //Green
    public final int LONGING = 101; //Blue
    public final int WORSHIP = 203; //White
    public final int DISCONTENTMENT = 103; //Grey
    public final int GRIEF = 104; //Black
    public final int RAGE = 205; //Red
    public final int DEATH = 210; //DARK GREEN
    public final int HOPE = 212; //PINK
    //These are grouped in an array for access.
    public int[] moods = {HAPPY, CALM, LONGING, WORSHIP, DISCONTENTMENT, GRIEF, RAGE, DEATH, HOPE};


    //At runtime, the interface is prepared and the music is starts playing in its own thread.
    @FXML
    public void initialize() throws MidiUnavailableException {
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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Controller.songNameTxt.setText(MusicManager.currentSongName);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    MusicManager.playMusic();
                    progressBar.setProgress(MusicManager.getSongProgress());
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
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
    }

    //This method changes the tempo value (milliseconds) and is passed to 'MusicGen'.
    public void tempoChange(MouseEvent mouseEvent) {
        //Ensuring the sphere has returned to the initial size, so that two close clicks do not make a permanent size change...
        if (icon.getScaleX() == 1) {
            //Then the animation can be performed with these values, it will last 0.75 seconds and scale to this degree.
            tempoAnim(75, 0.15);
        }

        //The time is taken since the last click, the difference in time since the last click is taken.
        lastClickMillis = (System.currentTimeMillis() - lastClickMillis);
        //The tempo must be kept within sensible values... (300bpm to 20bpm).
        if (lastClickMillis > 50 && lastClickMillis < 3000) {  //// > 250
            //Pass the millisecond value to the music playing class to indicate how long the thread should sleep for before the next note should be played.
            MusicManager.setTempo((int) lastClickMillis);
        }
        //The time of this click is taken.
        lastClickMillis = System.currentTimeMillis();
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
}