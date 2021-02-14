import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
//import javafx.scene.panelRoot.iconGroup;
import javax.sound.midi.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

//  MIDI
//  source for a lot of this stuff: https://docs.oracle.com/javase/tutorial/sound/MIDI-synth.html



//extend or (implements < doesn't work?)////////////////////////////////////////////////////////////////////////////////
public class Main implements Initializable {//} extends Composer {

    //SHOULD J INDEX BE 1?
    public static int[][] pianoRoll = new int[40][2];
    //velocity can be 0 when silence
    public static int EMPTYFIELD = -1;

    public static int OCTAVE = 3;
    //This is the amount of times +12 is added to a note...

    public static long tempo = 120; //bpm
    public static long lastTempoClickMillis;
    public static long lastTempoClickNewTime;
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

    static int currentKey;

    //second
    //60/30
    //minute divided by beat tempo = second interval

    //Camera camera = new PerspectiveCamera();


    public static void main(String args[]) {

        //m.scaleCalculator(100,Note.A);

        /////////////////////////////////////////////////////////////iconIdle();

        System.out.println("PLEAAAAAAAAAAASE");

        //fill array with 'empty values'
        for (int i = 0; i < pianoRoll.length; i++) {
            for (int j = 0; j < pianoRoll[0].length; j++) {
                pianoRoll[i][j] = EMPTYFIELD;
            }
        }

        try {
            // get a synthesizer
            Synthesizer s = MidiSystem.getSynthesizer();
            // open it (I forgot to do this, the tutorial forgot to mention it!)
            s.open();
            // get the first MIDI channel in the synth (each channel supports one instrument)
            MidiChannel chan = s.getChannels()[0];
            // set bank 0, instrument 0 (it's a piano)
            chan.programChange(0, 0);
            // play a C/G perfect fifth - this is asynchronous; the notes will start playing
            // while your code can do something else.
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////musicLoader();

            //isEmpty()
            while (pianoRoll[0][0] != 128 || pianoRoll[0][1] != -1) {
                //while (pianoRoll[0][0] != 128 || pianoRoll[0][1] != -1) {
                chan.noteOn((pianoRoll[0][0]), (pianoRoll[0][1]));
                //Ready next note
                //SHIFT ALL ELEMENTS ONE CLOSER THE THE FIRST ELEMENT

                //minute divided by beat tempo = second interval
                // wait for 3 seconds
                try {
                    TimeUnit.MILLISECONDS.sleep(tempo);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                chan.noteOff(pianoRoll[0][0]);/////////////////////////////////////// umm?
            }
            //while piano roll not empty or Got weird un-lpayable note in it...
            System.out.println("UGHHH EXCUSE ME... THIS ISN'T A PLAYABLE NOTE, THIS IS THE EMPTY PLACEHOLDER.");
            //We want to fix the pianoroll... This should never happen? Adapt improvise overcome lol?

            /////////////// note number=60, velocity=93
            /////////////chan.noteOn(67, 93);
            /////////////// wait for 3 seconds
            /////////////TimeUnit.SECONDS.sleep(3);
            /////////////// turn the notes off
            /////////////chan.noteOff(60);
            /////////////chan.noteOff(67);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void musicLoader() {
        for (int i = 0; i < pianoRoll.length; i++) {
            //velocity can be 0 when silence
            if (pianoRoll[i][0] == EMPTYFIELD && pianoRoll[i][1] == EMPTYFIELD) {
                pianoRoll[i][0] = 40;
                pianoRoll[i][1] = 100;
            } else {
                System.out.println("Piano music queue full.");
            }
        }
    }

    public void tempoChange(MouseEvent mouseEvent) {
        //Make group?
        System.out.println("CLICKY CLICK");

        if (icon.getScaleX() == 1) {
            growSphereAnim(75, 0.15);
        }
        //scaleTransition.stop();

        lastTempoClickNewTime = (System.currentTimeMillis() - lastTempoClickMillis);
        //System.out.println(lastTempoClickNewTime);
        if (lastTempoClickNewTime > 0 || lastTempoClickNewTime < 3000) {
            tempo = (60 / lastTempoClickNewTime);
        }
        //System.out.println(tempo);
        lastTempoClickMillis = System.currentTimeMillis();
    }

    public void growSphereAnim(int timeLength, double scaleAmount) {
        Duration duration = Duration.millis(timeLength);
        //Create new scale transition
        ScaleTransition scaleTransition = new ScaleTransition(duration, icon);
            //Set how much X should enlarge
            scaleTransition.setByX(scaleAmount);
            //Set how much Y should
            scaleTransition.setByY(scaleAmount);
            //Set how much Y should
            scaleTransition.setByZ(scaleAmount);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(2);
            scaleTransition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //icon.getRadius();
        //icon.getMaterial();
        //icon.getTranslateZ();
        //icon.getRotate();
        currentKey = 101;
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
}