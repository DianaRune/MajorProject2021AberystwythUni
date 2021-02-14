import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MusicManager {
    //Major and minor?
    public int ID;
    public int[] noteValues;

    //public int noteRange = 127;
    //+12 when you go up an octave
    //public final int MIDDLEC = 60;

    public static int[] MAJORSCALEPATTERN = {0, 2, 4, 5, 7, 9, 11, 12};
    public static int[] MINORSCALEPATTERN = {0, 1, 3, 5, 7, 9, 10, 12};


    /////////////////////////
    public static int[] MAJORTRIAD = {0, 4, 7};
    public static int[] MINORTRIAD = {0, 3, 7};
    /////////////////////////

    public static Key currentKey;// = CMajorScale;// = new Key();

/*
    //keys
    public static Key CMajorScale;
    public static Key AMinorScale;
    public static Key GMajorScale;
    public static Key EMinorScale;
    public static Key DMajorScale;
    public static Key BMinorScale;
    public static Key AMajorScale;
    public static Key FSharpMinorScale;
    public static Key EMajorScale;
    public static Key CSharpMinorScale;
    public static Key BMajorScale;
    public static Key GSharpMinorScale;
    public static Key CFlatMajorScale;
    public static Key GFlatOrFSharpFlatMajorScale;
    public static Key GFlatOrFSharpMinorScale;
    public static Key CSharpMajorScale;
    public static Key DFlatMajorScale;
    public static Key BFlatMinorScale;
    public static Key AFlatMajorScale;
    public static Key FMinorScale;
    public static Key EFlatMajorScale;
    public static Key CMinorScale;
    public static Key BFlatMajorScale;
    public static Key GMinorScale;
    public static Key FMajorScale;
    public static Key DMinorScale;
*/




///////////////////////////////////////////////////////////////////////////////////////////////






    public static int[][] pianoRoll = new int[40][2];
    public static int[][] drumRoll = new int[40][2];
    public static int[][] drumRoll2 = new int[40][2];


    public static int EMPTYFIELD = -1;

    public static int OCTAVE = 3;
    //This is the amount of times +12 is added to a note...

    public static int TEMPO = 30; //bpm
    //second
    //60/30
    //minute divided by beat tempo = second interval

    public static int currentNote;





    public static int[] scaleCalculator(int keyID, Note rootNote) { //// Key key
        int[] scale;// = new int[8];

        if (keyID - 100 > 100) {
            scale = MAJORSCALEPATTERN;
            System.out.println("1");
        } else {
            scale = MINORSCALEPATTERN;
            System.out.println("2");
        }

        for (int i = 0; i < 8; i++) {//scale.length; i++) {
            //Get value of Enum note and add it to the values in our scale to adapt to key.
            scale[i] = scale[i] + rootNote.ordinal();
            System.out.println(scale[i]);
        }
        return scale;
    }

    public static int[] triadChordCalculator(int keyID, Note rootNote) { //?
        int[] triadChordNotes = new int[3];
        int[] scale = scaleCalculator(keyID, rootNote);
        //if minor/minor scale...
        if (keyID - 100 > 100) {
            triadChordNotes = MAJORTRIAD;
            //cycle round the chord pattern for minor/major and return the note values in the scale... (0 for the root (enum))
            for (int i = 0; i < MAJORSCALEPATTERN.length; i++) {
                triadChordNotes[i] = scale[triadChordNotes[i]];
            }
            return triadChordNotes;
        } else {
            triadChordNotes = MINORTRIAD;
            //cycle round the chord pattern for minor/major and return the note values in the scale... (0 for the root (enum))
            for (int i = 0; i < MINORSCALEPATTERN.length; i++) {
                triadChordNotes[i] = scale[triadChordNotes[i]];
            }
            return triadChordNotes;
        }
    }

    public static void fillEmpty() {
        //fill array with 'empty values'
        for (int i = 0; i < pianoRoll.length; i++) {
            for (int j = 0; j < pianoRoll[0].length; j++) {
                pianoRoll[i][j] = EMPTYFIELD;
            }
        }
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
        musicLoader();

        //isEmpty()
        int instrument = 0;
        while (pianoRoll[0][0] != 128 || pianoRoll[0][1] != -1) {

            //while (pianoRoll[0][0] != 128 || pianoRoll[0][1] != -1) {
            chan.noteOn((pianoRoll[0][0]), (pianoRoll[0][1]));
            ///////////////////////////////////////////////////////////////////////chan.programChange(1024, 118); drum
            chan.noteOn((drumRoll[0][0]), (drumRoll[0][1]));
            //Ready next note
            //SHIFT ALL ELEMENTS ONE CLOSER THE THE FIRST ELEMENT

            //minute divided by beat tempo = second interval
            // wait for 3 seconds
            try {
                TimeUnit.SECONDS.sleep(60 / TEMPO);
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
        for (int i = 0; i < pianoRoll.length; i++) {
            //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (pianoRoll[i][0] == EMPTYFIELD && pianoRoll[i][1] == EMPTYFIELD) {
                pianoRoll[i][0] = (randomComposer() + (12 * OCTAVE));    //(randomComposer().ordinal)????
                pianoRoll[i][1] = 100;
            } else {
                System.out.println("Piano music queue full.");
            }
        }
        for (int i = 0; i < drumRoll.length; i++) {
            //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (drumRoll[i][0] == EMPTYFIELD && drumRoll[i][1] == EMPTYFIELD) {
                drumRoll[i][0] = ((Note.B.ordinal()) + (12 * OCTAVE));
                pianoRoll[i][1] = 100;
            } else {
                System.out.println("Piano music queue full.");
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public static Key getKey(int chosenKeyID)
    {
        currentKey.intialiseKeys();
        Key keys [] = Key.getKeyArray();

        System.out.println(chosenKeyID);
        System.out.println(keys[3].ID);

        for (int i = 0; i <= keys.length; i++) {
            if (keys[i].ID == chosenKeyID) {
                //Does this make the keys not initialise... They are null? Why?
                System.out.println(keys[i]);
                currentKey = keys[i];
                return keys[i];
            }
        }
        System.out.println("Not a valid ID, no key returned.");
        return null;
    }





    //loop feeding ints.. [note & velocity...]
    public static int randomComposer()
    {
        int pattern[] = new int[4];
        //Time signature..
        //* (max - min)) + min);
        int barsToRepeatFor = ThreadLocalRandom.current().nextInt(0, 4 + 1);
        int barsLoaded = 0;
        currentNote = 0;
        int i = 0;
        while (barsToRepeatFor != barsLoaded)
        {
            pattern[i] = melodyPatterns(ThreadLocalRandom.current().nextInt(0, 2 + 1), 4)[i];
            System.out.println(ThreadLocalRandom.current().nextInt(0, 2 + 1));
            if (4 == i) //ime sig top = 4 for now...
            {
                barsLoaded++;
                i = 0;
            }
            i++;
            return pattern[i - 1];
        };
        //BAD??
        return pattern[i];
        //if over eight just go up an octave...
    }

    public static int[] melodyPatterns(int randomPatternChosen, int timeSigTop){
        //CMajorScale.noteValues = scaleCalculator(CMajorScale.ID, Note.C);
        int[] noteArray = {0, 2, 4, 5, 7, 9, 11, 12};
        //int randomNote = CMajorScale.noteValues[ThreadLocalRandom.current().nextInt(0, 8 + 1)];//////////////////////////= currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 8 + 1)];
        //int randomNote2 = currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 8 + 1)];

        //int randomNote = noteArray[ThreadLocalRandom.current().nextInt(0, 7 + 1)];//////////////////////////= currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 8 + 1)];
        int randomNote = getKey(101).noteValues[ThreadLocalRandom.current().nextInt(0, 7 + 1)];//////////////////////////= currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 8 + 1)];
        //^ E minor
        int randomNote2 = getKey(101).noteValues[ThreadLocalRandom.current().nextInt(0, 7 + 1)];
        //int randomNote2 = noteArray[ThreadLocalRandom.current().nextInt(0, 7 + 1)];

        int [] returningPattern = new int[timeSigTop + 1];

        for (int i = 0; i <= timeSigTop; i++) {
            switch (randomPatternChosen) {
                //Straight same note in bar...
                case 0:
                    returningPattern[i] = randomNote;
                    break;
                //Random up/down note...
                case 1:
                    returningPattern[i] = randomNote;
                    int randomNoteInBar = ThreadLocalRandom.current().nextInt(0, timeSigTop + 1);
                    if (i == randomNoteInBar && randomNote < 8)
                    {
                        if (Math.random() <= 0.5)
                        {
                            returningPattern[i] = randomNote + 1;
                        }
                        else
                        {
                            returningPattern[i] = randomNote - 1;
                        }
                    }
                    break;
                //Note, up/down, note, random.
                case 2:
                    returningPattern[i] = randomNote;
                    if (i == 2 && randomNote < 8)
                    {
                        if (Math.random() <= 0.5)
                        {
                            returningPattern[i] = randomNote + 1;
                        }
                        else
                        {
                            returningPattern[i] = randomNote - 1;
                        }
                    }
                    if (i == 4 && randomNote < 8)
                    {
                        returningPattern[i] = randomNote2;
                    }
                    break;
                default:
                    return null;
            }
        }
        return returningPattern;
    }
}