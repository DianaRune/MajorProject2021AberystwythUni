import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MusicManager {

    //public int noteRange = 127;
    //+12 when you go up an octave
    //public final int MIDDLEC = 60;


    /////////////////////////
    public static int[] MAJORTRIAD = {0, 4, 7};
    public static int[] MINORTRIAD = {0, 3, 7};
    /////////////////////////

    public static Key currentKey = Key.CMajorScale;// = new Key();

    public static int timeSignatureTop = 4;
    public static int timeSignatureBottom = 4;


///////////////////////////////////////////////////////////////////////////////////////////////






    public static int[][] pianoRoll = new int[40][2];
    public static int[][] drumRoll = new int[40][2];
    public static int[][] drumRoll2 = new int[40][2];


    public static int EMPTYFIELD = -1;

    public static int OCTAVE = 2;
    //This is the amount of times +12 is added to a note...
    //second
    //60/30
    //minute divided by beat tempo = second interval

    public static int currentNote;




    public static int[] triadChordCalculator(int keyID, Note rootNote) { //?
        int[] triadChordNotes = new int[3];
        //if minor/minor scale...
        if (keyID - 100 > 100) {
            triadChordNotes = MAJORTRIAD;
            //cycle round the chord pattern for minor/major and return the note values in the scale... (0 for the root (enum))
            for (int i = 0; i < currentKey.noteValues.length; i++) {
                triadChordNotes[i] = currentKey.noteValues[triadChordNotes[i]];
            }
            return triadChordNotes;
        } else {
            triadChordNotes = MINORTRIAD;
            //cycle round the chord pattern for minor/major and return the note values in the scale... (0 for the root (enum))
            for (int i = 0; i < currentKey.noteValues.length; i++) {
                triadChordNotes[i] = currentKey.noteValues[triadChordNotes[i]];
            }
            return triadChordNotes;
        }
    }

    public static int[][] fillEmpty(int[][] track) {
        //fill array with 'empty values'
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                track[i][j] = EMPTYFIELD;
            }
        }
        return track;
    }

    public static void musicPlayer(int tempo) throws MidiUnavailableException {
        // get a synthesizer
        Synthesizer s = MidiSystem.getSynthesizer();
        // open it (I forgot to do this, the tutorial forgot to mention it!)
        s.open();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Sequencer sequencer = MidiSystem.getSequencer();
        //sequencer.open();
        //sequencer.setSequence(is); // is = Midi file
        //sequencer.start();
        //while (sequencer.isRunning())
        //sequencer.close();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // get the first MIDI channel in the synth (each channel supports one instrument)
        MidiChannel chan = s.getChannels()[0];
        // set bank 0, instrument 0 (it's a piano)
        chan.programChange(0, 0);
        // play a C/G perfect fifth - this is asynchronous; the notes will start playing
        // while your code can do something else.
        pianoRoll = fillEmpty(pianoRoll);
        //isEmpty()
        int instrument = 0;
        while (pianoRoll[0][0] != 128 || pianoRoll[0][1] != -1) {
            pianoRoll = fillEmpty(pianoRoll);
            musicLoader();
            chan.noteOn((pianoRoll[0][0]), (pianoRoll[0][1]));
            ///////////////////////////////////////////////////////////////////////chan.programChange(1024, 118); drum
            chan.noteOn((drumRoll[0][0]), (drumRoll[0][1]));
            //Ready next note
            //SHIFT ALL ELEMENTS ONE CLOSER THE THE FIRST ELEMENT

            //minute divided by beat tempo = second interval
            // wait for 3 seconds
            try {
                TimeUnit.SECONDS.sleep(60 / tempo);          //////////////////////////////////////// divide by two because omg
                //chan.noteOff(pianoRoll[0][0]);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            chan.noteOff(pianoRoll[0][0]);/////////////////////////////////////// umm?
        }
        //while piano roll not empty or Got weird un-playable note in it...
        System.out.println("UGHHH EXCUSE ME... THIS ISN'T A PLAYABLE NOTE, THIS IS THE EMPTY PLACEHOLDER.");
        //We want to fix the pianoroll... This should never happen? Adapt improvise overcome lol?
    }

    public static void musicLoader() {
        int[] pianoNotesGenerated = melodyPatterns(ThreadLocalRandom.current().nextInt(0, 2 + 1), timeSignatureTop); //2 + 1 < number of current cases...

        for (int i = 0; i < pianoRoll.length; i++) {
            //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (pianoRoll[i][0] == EMPTYFIELD && pianoRoll[i][1] == EMPTYFIELD) {
                if (i < pianoNotesGenerated.length) {
                    System.out.println(":(((((((( " + pianoNotesGenerated[i]);
                    pianoRoll[i][0] = (pianoNotesGenerated[i] + (12 * OCTAVE));    //(randomComposer().ordinal)????
                    System.out.println(pianoRoll[i][0]);
                    pianoRoll[i][1] = 100;
                }
            } else {
                //System.out.println("Piano music queue full.");
            }
        }
        for (int i = 0; i < drumRoll.length; i++) {
            //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (drumRoll[i][0] == EMPTYFIELD && drumRoll[i][1] == EMPTYFIELD) {
                drumRoll[i][0] = ((Note.B.ordinal()) + (12 * OCTAVE));
                pianoRoll[i][1] = 100;
            } else {
                ///System.out.println("Drum music queue full.");
            }
        }
        ChordManager.loadChord();
    }



    public static Key setKey(int chosenKeyID)
    {
        Key keys [] = Key.getKeyArray();

        for (int i = 0; i <= keys.length; i++) {
            if (keys[i].ID == chosenKeyID) {
                //Does this make the keys not initialise... They are null? Why?
                currentKey = keys[i];
                return keys[i];
            }
        }
        System.out.println("Not a valid ID, no key returned.");
        return null;
    }

    public static Key getKey()
    {
        return currentKey;
    }


/*    //loop feeding ints.. [note & velocity...]
    public static int[] randomComposer()
    {
        int pattern[] = new int[4];
        //Time signature..
        //* (max - min)) + min);
        int barsToRepeatFor = ThreadLocalRandom.current().nextInt(1, 4 + 1); //MAKE NOT ZERO.. SET MIN
        int barsLoaded = 0;
        currentNote = 0;
        int i = 0;
        while (barsToRepeatFor != barsLoaded)
        {
            System.out.println("please omg");
            pattern[i] = melodyPatterns(ThreadLocalRandom.current().nextInt(0, 2 + 1), timeSignatureTop)[i];

            if (timeSignatureTop != i) //ime sig top = 4 for now...
            {
                barsLoaded++;
                i = 0;
                return melodyPatterns(ThreadLocalRandom.current().nextInt(0, 2 + 1), timeSignatureTop);
            }
            i++;
            //pattern;//[i - 1];
        };
        //BAD??
        return melodyPatterns(ThreadLocalRandom.current().nextInt(0, 2 + 1), timeSignatureTop);//pattern;//[i];
        //if over eight just go up an octave...
    }
 */

    public static int[] melodyPatterns(int randomPatternChosen, int timeSigTop){

        int[] noteValues = currentKey.noteValues;
        int randomIndex = ThreadLocalRandom.current().nextInt(0, 7);
        int baseRandomNote = noteValues[randomIndex];
        //System.out.println(randomNote);
        //System.out.println(randomNote + 1);
        int baseRandomNote2 = currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 7)];

        int [] returningPattern = new int[timeSigTop];

        for (int i = 0; i < timeSigTop; i++) {

            switch (randomPatternChosen) {
                //Straight same note in bar...
                case 0:
                    returningPattern[i] = baseRandomNote;
                    break;
                //Random up/down note...
                case 1:
                    returningPattern[i] = baseRandomNote;
                    int randomNoteInBar = ThreadLocalRandom.current().nextInt(0, timeSigTop);// + 1);
                    if (i == randomNoteInBar && randomIndex < 8)
                    {
                        if (Math.random() >= 0.5)
                        {
                            returningPattern[i] = noteValues[randomIndex + 1];
                        }
                        else if (randomIndex > 0)
                        {
                            returningPattern[i] = noteValues[randomIndex - 1];
                        }
                    }
                    break;
                //Note, up/down, note, random.
                case 2:
                    returningPattern[i] = baseRandomNote;
                    if (i == 2 && randomIndex < 8)
                    {
                        if (Math.random() <= 0.5)
                        {
                            returningPattern[i] = noteValues[randomIndex + 1];
                        }
                        else if (randomIndex > 0)
                        {
                            returningPattern[i] = noteValues[randomIndex - 1];
                        }
                    }
                    if (i == 4 && randomIndex < 8)
                    {
                        returningPattern[i] = baseRandomNote2;
                    }
                    break;
                default:
                    return null;
            }
            //System.out.println(returningPattern[i]);////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        return returningPattern;
    }

    public static void emptyTracks()
    {

    }
}