//Track Manager is the abstract parent class for 'Tracks' which extend from it...
public abstract class TrackManager {

    //IMPLEMETS TRACK IN MUSCICMANAGER TRACK=NEWTRACK("TYPE") EVENTUALLY?

    //Use 'reflections' to see how many classes extend this class.............................################################################################################################################################################
    //For setting instruments.

    public static int trackLength = (10 * getTimeSigTop());

    //Fills the arrays with empty values in order to give them length and determine there aren't real notes under the index.
    public static int EMPTYFIELD = -1;

    //An int[][] which will contain generated queued notes to be played in a FIFO fashion.
    //The first index is just a sensible length, generally 10 bars and the second represents the two values to be read for each index/note, 'pitch' and 'velocity'.
    //For example, trackRoll[0][0] = first note's pitch and trackRoll[0][1] = first note's velocity.
    public static Note[] trackRoll;

    //This method queues the generated notes (passed a parameter) into the empty fields of the trackRoll, and returns it.
    public static Note[] loadRoll(Note[] trackRoll, Note[] notesGenerated) {
        //Will queue for the length of trackRoll...
        Note trackRollFilled[] = new Note[trackRoll.length];
        int trackRollInt[][] = new int[trackRoll.length][3];
        for (int i = 0; i < trackRoll.length; i++) {
            //If there is an EMPTYFIELD available in the location...
            if (trackRoll[i].pitch == EMPTYFIELD && trackRoll[i].velocity == EMPTYFIELD && trackRoll[i].duration == EMPTYFIELD) {
                //And the generated melody length is not exceeded.
                if (i < notesGenerated.length) {
                    //The index's pitch value is equal to the base pitch and the current octave value added.
                    trackRollInt[i][0] = (notesGenerated[i].pitch + 12 * MusicManager.OCTAVE);
                    trackRollInt[i][1] = (notesGenerated[i].velocity);
                    trackRollInt[i][2] = (notesGenerated[i].duration);
                    trackRollFilled[i] = new Note(trackRollInt[i][0], trackRollInt[i][1], trackRollInt[i][2]);
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// System.out.println(trackRollFilled[i].pitch);
                    //The index's velocity value is 100.
                    //////////////trackRoll[i].velocity = 100;///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                }
            }
        }
        //Return the notes for playing...
        return trackRollFilled;
    }

    //This method fills the trackRoll with empty values for the generated music to overwrite.
    public static Note[] fillRollWithEmptyValues(int trackLength) {
        Note[] emptyArray = new Note[trackLength];
        Note empty = new Note(EMPTYFIELD, EMPTYFIELD, EMPTYFIELD);
        //Fill every array index with 'empty values'
        for (int i = 0; i < trackLength; i++) {
            emptyArray[i] = empty;
            // emptyArray[i].pitch = EMPTYFIELD;
            // emptyArray[i].velocity = EMPTYFIELD;
            // emptyArray[i].duration = EMPTYFIELD;
        }
        return emptyArray;
    }

/*
    //This method fills the trackRoll with empty values for the generated music to overwrite.
    public static void fillRollWithEmptyValues(Note[] trackRoll)
    {
        Note[] emptyArray = new Note[trackLength];
        //Fill every array index with 'empty values'
        for (int i = 0; i < trackRoll.length; i++)
        {
            trackRoll[i].pitch = EMPTYFIELD;
            trackRoll[i].velocity = EMPTYFIELD;
            trackRoll[i].duration = EMPTYFIELD;
        }
        return new Note;
    }
 */

    //This method gets the music's key...
    public static Key getKey() {
        //When this is called the current key is returned at that place.
        return MusicManager.currentKey;
    }

    //This method gets the music's time signature (top)...
    public static int getTimeSigTop() {
        //When this is called the time signature (top) is returned at that place.
        return MusicManager.timeSignatureTop;
    }
}













/*
import java.util.Arrays;

//Track Manager is the abstract parent class for 'Tracks' which extend from it...
public abstract class TrackManager {

    //IMPLEMETS TRACK IN MUSCICMANAGER TRACK=NEWTRACK("TYPE") EVENTUALLY?

    //Use 'reflections' to see how many classes extend this class.............................################################################################################################################################################
    //For setting instruments.

    //Fills the arrays with empty values in order to give them length and determine there aren't real notes under the index.
    public static int EMPTYFIELD = -1;

    //An int[][] which will contain generated queued notes to be played in a FIFO fashion.
    //The first index is just a sensible length, generally 10 bars and the second represents the two values to be read for each index/note, 'pitch' and 'velocity'.
    //For example, trackRoll[0][0] = first note's pitch and trackRoll[0][1] = first note's velocity.
    public static Note trackRoll;

    //The instrument/voice the track will have that will be applied to the channel it is played in.
    ////I'M NOT SURE THIS SHOULD BE HERE
    public static int instrument;

    //This method queues the generated notes (passed a parameter) into the empty fields of the trackRoll, and returns it.
    public static int[][] loadRoll(int[][] trackRoll, int[] notesGenerated) {
        //Will queue for the length of trackRoll...
        for (int i = 0; i < trackRoll.length; i++) {                                                                             //////////  trackRoll.notes.length
            //If there is an EMPTYFIELD available in the location...
            if (trackRoll[i][0] == EMPTYFIELD && trackRoll[i][1] == EMPTYFIELD) {
                //And the generated melody length is not exceeded.
                if (i < notesGenerated.length) {
                    //The index's pitch value is equal to the base pitch and the current octave value added.
                    trackRoll[i][0] = (notesGenerated[i] + 12 * MusicManager.OCTAVE);
                    //The index's velocity value is 100.
                    trackRoll[i][1] = 100;
                }
            }
        }
        //Return the notes for playing...
        return trackRoll;
    }

    //This method fills the trackRoll with empty values for the generated music to overwrite.
    public static void fillRollWithEmptyValues(int[][] trackRoll)
    {
        //Fill every array index with 'empty values'
        for (int i = 0; i < trackRoll.length; i++) {
            for (int j = 0; j < trackRoll[0].length; j++) {
                trackRoll[i][j] = EMPTYFIELD;
            }
        }
    }

    public static void setVelocityForTrack()
    {

    }

    public static void setDurationForTrack()
    {

    }

    //This method gets the music's key...
    public static Key getKey()
    {
        //When this is called the current key is returned at that place.
        return MusicManager.currentKey;
    }

    //This method gets the music's time signature (top)...
    public static int getTimeSigTop()
    {
        //When this is called the time signature (top) is returned at that place.
        return MusicManager.timeSignatureTop;
    }

}
 */