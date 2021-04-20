//Importing the JavaFX libraries required to access and alter the graphics...
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.util.Duration;
//Importing MIDI library for the MIDI unavailable exception.
import javax.sound.midi.*;
//Imports the function that allows for parsing ints. (Attempted integer conversion from another data type.)
import static java.lang.Integer.parseInt;

//The controller class for my JavaFX UI. Also my main class where I will 'call MusicGen' functionality.
public class Controller extends Thread {

    //The time since the graphic was clicked last. This is used to determine the time between each note being played, therefore, tempo.
    public static long lastClickMillis = 1000;
    //The FXML objects created and referenced and initialised for use here.
    //@FXML allows the FXMLLoader to inject the pre-defined FXML values into the ones defined here, in the controller class.
    @FXML
    //Root pane object.
    public AnchorPane panelRoot;

    //Objects on pane (not graphic).
    public Button pausePlayBtn;
    public Button FFBtn;
    public Button RWBtn;
    public Text songNameTxt;
    public ProgressBar progressBar = new ProgressBar();

    //Graphic objects.
    public Sphere graphicBody;
    public Cylinder graphic1;
    public Cylinder graphic2;
    public Cylinder graphic3;
    public Cylinder graphic4;
    public Cylinder graphic5;

    //Help Menu objects.
    public TextArea helpMenu;
    public Button openHelpMenuBtn;
    public Button helpMenuCloseBtn;

    //Instrument settings menu objects.
    public TextArea instrumentMenu;
    public Button openInstrumentMenuBtn;
    public Button instrumentMenuCloseBtn;
    public ComboBox voiceDropDown;
    public ComboBox trackDropDown;
    public Label TimeSignatureLbl;
    public TextField sigTopTxtField;
    public TextField sigBotTxtField;
    public Label OctaveLbl;
    public TextField octaveTxtField;
    public Button applyInstrumentBtn;

    //With the 'MusicGen' interface, an object of the music system can be created for use within this application.
    MusicGen MusicGenerator = new MusicManager();

    //The index for the mood array, determines current mood.
    public int mood = 0;
    //The key IDs for their respective moods... (Future keys could be added.)
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

    //The decimal of how long a song has been played for. It is passed to a progress bar to be displayed.
    public double progressValue;
    //The name of the current song being played, it's passed to a text object to be displayed here.
    public String songNameText;

    //At runtime, the interface is prepared and the music starts playing in its own thread.
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

        TimeSignatureLbl.setVisible(false);
        sigTopTxtField.setVisible(false);
        sigBotTxtField.setVisible(false);

        OctaveLbl.setVisible(false);
        octaveTxtField.setVisible(false);

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
        MusicGenerator.setInstrument(0, 0, 0);
        //Clean guitar voice for chord track.
        MusicGenerator.setInstrument(1, 0, 27);
        //Synth drum voice for drum track.
        MusicGenerator.setInstrument(2, 0, 118);

        //The initial/default key is set for music generation to begin.
        MusicGenerator.setKey(moods[mood]);

        //The default black backgrounds are set to the UI to make it pretty and so the white content shows up more clearly.
        progressBar.setStyle("-fx-accent: black;");
        instrumentMenu.setStyle("-fx-control-inner-background:black");
        helpMenu.setStyle("-fx-control-inner-background:black");

        //The backgrounds of the buttons are set to be transparent so the icon image can show up better and is place of the button itself.
        pausePlayBtn.setStyle("-fx-background-color:transparent");
        helpMenuCloseBtn.setStyle("-fx-background-color:transparent");
        instrumentMenuCloseBtn.setStyle("-fx-background-color:transparent");
        openHelpMenuBtn.setStyle("-fx-background-color:transparent");
        openInstrumentMenuBtn.setStyle("-fx-background-color:transparent");
        FFBtn.setStyle("-fx-background-color:transparent");
        RWBtn.setStyle("-fx-background-color:transparent");

        //These representative images are applied to their corresponding buttons.
        Image pauseBtnImage = new Image("Btn_Icons/PauseBtnIcon2.png");
        pausePlayBtn.setGraphic(new ImageView(pauseBtnImage));
        Image closeBtnImage = new Image("Btn_Icons/Close_Btn_Icon.png");
        helpMenuCloseBtn.setGraphic(new ImageView(closeBtnImage));
        instrumentMenuCloseBtn.setGraphic(new ImageView(closeBtnImage));
        Image helpBtnImage = new Image("Btn_Icons/HelpBtnIcon2.png");
        openHelpMenuBtn.setGraphic(new ImageView(helpBtnImage));
        Image instrumentBtnImage = new Image("Btn_Icons/InstrumentBtnIcon.png");
        openInstrumentMenuBtn.setGraphic(new ImageView(instrumentBtnImage));
        Image ffBtnImage = new Image("Btn_Icons/FF_Btn.png");
        FFBtn.setGraphic(new ImageView(ffBtnImage));
        Image rwBtnImage = new Image("Btn_Icons/RW_Btn.png");
        RWBtn.setGraphic(new ImageView(rwBtnImage));

        //The music thread in the system starts playing infinitely.
        MusicGenerator.startMusicThread();

        //A thread that continuously updates the UI's song details is made.
        Thread progressThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    //The progress decimal is continuously updated...
                    progressValue = MusicGenerator.getSongProgress();
                    //And set.
                    progressBar.setProgress(progressValue);
                    //The song name is continuously updated...
                    songNameText = MusicGenerator.getSongName();
                    //And set.
                    songNameTxt.setText(songNameText);
                }
            }
        };
        //The tread starts playing infinitely.
        progressThread.start();
    }

    //This method is attached to the play/pause button on the UI.
    @FXML
    public void playPauseMusic(MouseEvent mouseEvent) {
        //The music's 'play/pause' state is inverted within the music system and the value returned...
        boolean isPlaying = MusicGenerator.switchPlayPause();
        //This value changes the buttons icon/graphic to show the effect of clicking the button next time.
        //If the music is paused...
        if (isPlaying == false) {
            //Set 'play' icon.
            Image playBtnImage = new Image("Btn_Icons/PlayBtnIcon2.png");
            pausePlayBtn.setGraphic(new ImageView(playBtnImage));
            //Else the music is playing...
        } else {
            //Set 'pause' icon.
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

        //If the dropdown's currently contain user selected values...
        if (trackDropDown.getValue() != null && voiceDropDown.getValue() != null) {
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
            MusicGenerator.setInstrument(trackValue, bankValue, programValue);
            System.out.println("SUCCESSFULLY CHANGED VOICE FOR TRACK " + trackValue + ". THE INSTRUMENT " + bankValue + ", " + programValue + ".");
        }

        //If both the fields are not currently empty...
        if (!sigTopTxtField.getText().equals("") && !sigBotTxtField.getText().equals("")) {
            //The values are converted to integers.
            int sigTop = parseInt(sigTopTxtField.getText());
            int sigBot = parseInt(sigBotTxtField.getText());
            //They must be in range of valid time signature values...
            if (sigTop <= 1 || sigTop > 12) {
                System.out.println("Your top Time Signature value is out of bounds. Please pick a value between 2 and 12.");
            } else if (sigBot <= 1 || sigBot > 8) {
                System.out.println("Your bottom Time Signature value is out of bounds. Please pick a value between 2 and 8.");
            } else {
                //Then they can be assigned.
                MusicGenerator.setTimeSig(sigTop, sigBot);
            }
        }

        //If the field is not currently empty...
        if (!octaveTxtField.getText().equals("")) {
            //The values is converted to an integer.
            int octave = parseInt(octaveTxtField.getText());
            //It must be in range of valid time octave values...
            if (octave < 0 || octave > 3) {
                System.out.println("Your octave value is out of bounds. Please pick a value between 0 and 3.");
            } else {
                //Then it can be assigned.
                MusicGenerator.setOctave(octave);
            }
        }
    }

    //If a key is pressed a listener calls this method...
    //Select moods are cycled, like they would be on the circle of fifths.
    //'D' to go clockwise and 'A' to reverse.
    @FXML
    void keyChangeFromArrowKeys(KeyEvent keyEvent) {
        //If this key is 'D'...
        if (keyEvent.getCode() == KeyCode.D) {
            //And the index value is equal to the maximum mood index...
            if (mood == (moods.length - 1)) {
                //Then the minimum index is set. (Makes the array go full circle.)
                mood = 0;
            }
            //Otherwise...
            else {
                //This index value should be incremented, and the next clockwise mood accessed.
                mood++;
            }
        }
        //Else if this key is 'A'...
        else if (keyEvent.getCode() == KeyCode.A) {
            //And the index value is equal to the minimum mood index...
            if (mood == 0) {
                //Then the maximum index is set. (Makes the array go full circle.)
                mood = (moods.length - 1);
            }
            //Otherwise...
            else {
                //This index value should be decremented, and the next anti-clockwise mood accessed.
                mood--;
            }
        }
        //This mood value (that represents a key ID,) is passed to be identified as a Key, set, and utilised in the MusicManager.
        MusicGenerator.setKey(moods[mood]);
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
        if (lastClickMillis > 50 && lastClickMillis < 3000) {
            //Pass the millisecond value to the music playing class to indicate how long the thread should sleep for before the next note should be played.
            MusicGenerator.setTempo((int) lastClickMillis);
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


    //This method is attached to the 'skip' button on the UI.
    public void nextSong() {
        //Calls for the next song to be generated and played within the music system object.
        MusicGenerator.playNextSong();
    }

    //This method is attached to the 'rewind' button on the UI.
    public void previousSong() {
        //Calls for the next song to be generated and played within the music system object.
        MusicGenerator.playPreviousSong();
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

        TimeSignatureLbl.setVisible(true);
        TimeSignatureLbl.toFront();
        sigTopTxtField.setVisible(true);
        sigTopTxtField.toFront();
        sigBotTxtField.setVisible(true);
        sigBotTxtField.toFront();
        OctaveLbl.setVisible(true);
        OctaveLbl.toFront();
        octaveTxtField.setVisible(true);
        octaveTxtField.toFront();
    }

    //The related objects are hidden in order to close this 'set instrument' menu.
    public void closeInstrumentMenu(MouseEvent mouseEvent) {
        instrumentMenu.setVisible(false);
        instrumentMenuCloseBtn.setVisible(false);
        trackDropDown.setVisible(false);
        voiceDropDown.setVisible(false);
        applyInstrumentBtn.setVisible(false);

        TimeSignatureLbl.setVisible(false);
        sigTopTxtField.setVisible(false);
        sigBotTxtField.setVisible(false);

        OctaveLbl.setVisible(false);
        octaveTxtField.setVisible(false);
    }
}