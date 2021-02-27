//Loads in the Java MIDI libraries for note playing use.
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
//Loads in Java time management to handle the tempo delay.
import java.util.concurrent.TimeUnit;

//This the core??? class for the music generating system.
public class MusicManager {

    // ############################################################################################################################################################
    // THE SECOND INDEX IS THE OFF BEAT
    // ############################################################################################################################################################

    ////public int noteRange = 127;

    //Boolean that is switched by playing/pausing. It prevents the track access and note playing if paused.
    public static boolean isPlaying = true;

    //This is the currently used key that is accessed and set here.
    public static Key currentKey;

    //These integers describe the time signature set. (4/4 default.)
    public static int timeSignatureTop = 4;
    public static int timeSignatureBottom = 4;

    ////DO I REALLY NEED THIS?
    //Fills the arrays with empty values in order to give them length and determine their aren't real notes under the index.
    public static int EMPTYFIELD = -1;

    //The current octave chosen and set. This is the amount of times +12 is added to a note.
    public static int OCTAVE = 1;

    //The pace of the music, set here. (Alters the duration at which notes are played and removed from their arrays.)
    public static int tempo = 50;

    //Fills the arrays with empty values in order to give them length and determine their aren't real notes under the index.
    public static int[][] fillEmpty(int[][] track) {
        //Fill every array index with 'empty values'
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                track[i][j] = EMPTYFIELD;
            }
        }
        //Return the empty track.
        return track;
    }

    //The method that calls for music generation and controls all note playing, should run continuously from the first time it is called.
    public static void musicPlayer(int tempo) throws MidiUnavailableException {
        //THIS ISN'T GOING TO STAY HERE.
        setKey(109); //109 & 210
        //Get a synthesizer in order to access Java's MIDI channels and instruments.
        Synthesizer s = MidiSystem.getSynthesizer();
        //Open it.
        s.open();
        //Get the first MIDI channel in the synthesizer (each channel supports one instrument).
        MidiChannel chan = s.getChannels()[0];
        //Set instrument to that channel. (bank 0, instrument 0 (it's a piano)).
        chan.programChange(0, 0);

        //GET A NEW CHANNEL FOR OTHER TRACKS AND THEIR SET INSTRUMENTS...
        int[][][] AllChordRolls = ChordManager.playChord();

        int instrument = 0;
        //While the music is not paused, the music should play continuously.
        while (isPlaying) {
            //The queued notes to be played and generated and returned here.
            int[][] leadNoteToPlay1 = LeadManager.playLead();
            //They are then played with the respective note pitch and velocity.
            chan.noteOn(leadNoteToPlay1[0][0], leadNoteToPlay1[0][1]);

            //There is an attempt to wait/'sleep' a set amount of time before going round the loop again and playing the next note.
            try {
                TimeUnit.SECONDS.sleep(60 / (tempo * 2));
                //If this is not achievable though the loop being interrupted then...
            } catch (InterruptedException ex) {
                //An error is printed.
                ex.printStackTrace();
            }

            AllChordRolls = ChordManager.playChord();

            chan.noteOn(AllChordRolls[0][0][0], AllChordRolls[0][0][1]);
            //System.out.println("Chord 1 in music Manager: " + AllChordRolls[0][0][0]);
            chan.noteOn(AllChordRolls[1][0][0], AllChordRolls[1][0][1]);
            //System.out.println("Chord 2 in music Manager: " + AllChordRolls[1][0][0]);
            chan.noteOn(AllChordRolls[2][0][0], AllChordRolls[2][0][1]);
            //System.out.println("Chord 3 in music Manager: " + AllChordRolls[2][0][0]);

            for (int i = 0; i < AllChordRolls.length; i++) {
                AllChordRolls[0][i][0] = AllChordRolls[0][(i + 1)][0];
                AllChordRolls[0][i][1] = AllChordRolls[0][(i + 1)][1];
                AllChordRolls[1][i][0] = AllChordRolls[1][(i + 1)][0];
                AllChordRolls[1][i][1] = AllChordRolls[1][(i + 1)][1];
                AllChordRolls[2][i][0] = AllChordRolls[2][(i + 1)][0];
                AllChordRolls[2][i][1] = AllChordRolls[2][(i + 1)][1];
            }

            instrument++;

            try {
                TimeUnit.SECONDS.sleep(60 / tempo);          //////////////////////////////////////// divide by two because omg
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        ////while piano roll not empty or Got weird un-playable note in it...
        System.out.println("UGHHH EXCUSE ME... THIS ISN'T A PLAYABLE NOTE, THIS IS THE EMPTY PLACEHOLDER.");
    }


        //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //This method sets the music's key...
    //It takes the value passed to it as a parameter and attempts to find and set the required key in the key class by searching for it via integer ID.
    public static void setKey(int chosenKeyID) {
        //Gets all keys in Key.
        Key keys[] = Key.getKeyArray();
        //Makes a boolean to determine when the method can stop searching and the key is found.
        boolean keyFound = false;
        //For every key there is...
        for (int i = 0; i < keys.length; i++) {
            //If the ID is correct...
            if (keys[i].ID == chosenKeyID) {
                //Then keyFound is true.
                keyFound = true;
                //The current Key is set to the desired Key in the Key class found.
                currentKey = keys[i];
                //return keys[i];
            }
        }
        //If the key is not found...
        if (keyFound == false) {
            //There should be an error printed.
            System.out.println("Not a valid ID, no key returned.");
        }
    }

    //This method gets the music's key...
    public static Key getKey()
    {
        //When this is called the current key is returned at that place.
        return currentKey;
    }

    public void setInstrument()
    {
    }

    public void playMusic()
    {
        //LeadManager.playLead(key);
    }
}