//Importing the JavaFX libraries required to access and alter the graphics...
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
//Importing MIDI library for the MIDI unavailable exception.
import javax.sound.midi.*;
//DO I NEED THESE LIBRARIES FOR 'IMPLEMENTS INITIALISABLE' AND INITIALISE METHOD?
import java.net.URL;
import java.util.ResourceBundle;

//  MIDI
//  source for a lot of this stuff: https://docs.oracle.com/javase/tutorial/sound/MIDI-synth.html

//The controller class for my JavaFX UI. Also my main class where I will 'call MusicGen' functionality.
public class Main {// implements Initializable {//} extends MusicGen {

    public static int OCTAVE = 3;
    //This is the amount of times +12 is added to a note...

    public static long tempo = 60; //bpm
    public static long lastTempoClickMillis;
    public static long lastTempoClickNewTime;
    //The FXML objects created and referenced and initialised for use here.
    @FXML public AnchorPane panelRoot;
    @FXML public Sphere icon;
    @FXML public Cylinder icon1;
    @FXML public Cylinder icon2;
    @FXML public Cylinder icon3;
    @FXML public Cylinder icon4;
    @FXML public Cylinder icon5;
    @FXML public Group iconGroup;

    static boolean reverse;// = false;
    static int currentColor;// = 0;
    static boolean canPlayAnim = true;
    static boolean playing = true;

    static int currentKey;

    //Camera camera = new PerspectiveCamera();


    public static void main(String args[]) throws MidiUnavailableException {
       //NOTHING HAPPENS....... :3
    }

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

    //This method controls an animation for the graphic icon.
    public void growSphereAnim(int timeLength, double scaleAmount) {
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

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        currentKey = 101;
//    }

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
    public void playPauseMusic() throws MidiUnavailableException {
        playing = !playing;
        if (playing == true)
        {
            MusicManager.isPlaying = true;
        }
        else
        {
            MusicManager.isPlaying = false;
        }
    }

    @FXML
    public void initialisePlayingMusic() throws MidiUnavailableException {
        System.out.println("Helo");
        MusicManager.musicPlayer((int) tempo);
    }

    @FXML
    public void setKey(){
        //MusicManager.setKey();
    }
}