//Loads in the Java MIDI libraries for note playing use.
import javax.sound.midi.*;
//Loads in Java time management to handle the tempo delay.
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

//This the core??? class for the music generating system.
public class MusicManager implements MusicGen {

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

    //The current octave chosen and set. This is the amount of times +12 is added to a note.
    public static int OCTAVE = 1;

    //The pace of the music, set here. (Alters the duration at which notes are played and removed from their arrays.)
    public static int tempo = 50;

    public static int[][] trackInstruments = new int[Voice.getVoiceArray().length][2];

    public static double songStart;
    public static double currentTime;
    public static double songDuration;
    public static String currentSongName = SongnameDictionary.generateSongName();

    //The method that calls for music generation and controls all note playing, should run continuously from the first time it is called.
    public static void musicPlayer(int tempo) throws MidiUnavailableException {
        int[][] leadNoteToPlay1 = new int[40][2];
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
        System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

        //GET A NEW CHANNEL FOR OTHER TRACKS AND THEIR SET INSTRUMENTS...
        int[][][] AllChordRolls = ChordManager.playChord();
        int indexCount = 40;
        int instrument = 0;
        //While the music is not paused, the music should play continuously.
        while (isPlaying) {
            ////////////////////////////////////// WHERE EVERYTHING USED TO BELONG...
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
    public Key getKey() {
        //When this is called the current key is returned at that place.
        return currentKey;
    }

    public static void setInstrument(int trackNumber, int bankValue, int programValue) {
        System.out.println("Successful change.");
        trackInstruments[trackNumber][0] = bankValue;
        trackInstruments[trackNumber][1] = programValue;
    }

    public void playMusic() {
        //LeadManager.playLead(key);
    }

    public int getTimeSigTop() {
        return timeSignatureTop;
    }

    public static double getSongProgress() {
        double songProgress = (currentTime - songStart) / songDuration;
        return songProgress;
    }

    public static void playSong() throws MidiUnavailableException {
        int[][] leadNoteToPlay1 = new int[40][2];
        //THIS ISN'T GOING TO STAY HERE.
        setKey(109); //109 & 210
        //Get a synthesizer in order to access Java's MIDI channels and instruments.
        Synthesizer s = MidiSystem.getSynthesizer();
        //Open it.
        s.open();
        //Get the first MIDI channel in the synthesizer (each channel supports one instrument).
        MidiChannel leadChan = s.getChannels()[0];
        MidiChannel chordChan = s.getChannels()[1];
        MidiChannel drumChan = s.getChannels()[2];

        //Set instrument to that channel. (bank 0, instrument 0 (it's a piano)).
        leadChan.programChange(trackInstruments[0][0], trackInstruments[0][1]);
        chordChan.programChange(trackInstruments[1][0], trackInstruments[1][1]);
        drumChan.programChange(trackInstruments[2][0], trackInstruments[2][1]);
        System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

        //GET A NEW CHANNEL FOR OTHER TRACKS AND THEIR SET INSTRUMENTS...
        int[][][] AllChordRolls = ChordManager.playChord();
        int indexCount = 40;
        int instrument = 0;

        currentSongName = SongnameDictionary.generateSongName();
        songStart = System.currentTimeMillis();
        songDuration = ThreadLocalRandom.current().nextDouble(10000, 30000); //should be ten and 30 seconds

        while (isPlaying) {
            while (songDuration > currentTime) {
                //Update instrument values.
                leadChan.programChange(trackInstruments[0][0], trackInstruments[0][1]);
                chordChan.programChange(trackInstruments[1][0], trackInstruments[1][1]);
                drumChan.programChange(trackInstruments[2][0], trackInstruments[2][1]);
                //Involve pause time...???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                currentTime = System.currentTimeMillis() - songStart;
                //Playmusic();'
                //The queued notes to be played and generated and returned here.
                //Fill empty/null value, fill array again...
                if (indexCount == 40) {
                    indexCount = 0;
                    leadNoteToPlay1 = LeadManager.playLead();
                }
                //They are then played with the respective note pitch and velocity.
                leadChan.noteOn(leadNoteToPlay1[0][0], leadNoteToPlay1[0][1]);
                for (int i = 0; i < (leadNoteToPlay1.length - 1); i++) {
                    leadNoteToPlay1[i][0] = leadNoteToPlay1[(i + 1)][0];
                    leadNoteToPlay1[i][1] = leadNoteToPlay1[(i + 1)][1];
                }
                //There is an attempt to wait/'sleep' a set amount of time before going round the loop again and playing the next note.
                try {
                    TimeUnit.SECONDS.sleep(60 / (tempo * 2));
                    //If this is not achievable though the loop being interrupted then...
                } catch (InterruptedException ex) {
                    //An error is printed.
                    ex.printStackTrace();
                }
                indexCount++;
                AllChordRolls = ChordManager.playChord();

                chordChan.noteOn(AllChordRolls[0][0][0], AllChordRolls[0][0][1]);
                //System.out.println("Chord 1 in music Manager: " + AllChordRolls[0][0][0]);
                chordChan.noteOn(AllChordRolls[1][0][0], AllChordRolls[1][0][1]);
                //System.out.println("Chord 2 in music Manager: " + AllChordRolls[1][0][0]);
                chordChan.noteOn(AllChordRolls[2][0][0], AllChordRolls[2][0][1]);
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
        }
        //OTHERWISE IS PUASED
    }
}