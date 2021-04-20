//Track Manager is the abstract parent class for 'Tracks' which extend from it...
public abstract class TrackManager {

    //The length of all rolls to be filled.
    //It is a multiple of the current time signature so bars remain intact. Having it '10 bars long' allows for more than one bar to fill it at a time, allowing for more complex patterns.
    public static int rollLength = (10 * getTimeSigTop());

    //Fills the arrays with empty values in order to give them length and determine there aren't real notes under the index.
    public static int EMPTYFIELD = -1;

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
                    trackRollInt[i][0] = (notesGenerated[i].pitch + 12 * MusicManager.octave);
                    trackRollInt[i][1] = (notesGenerated[i].velocity);
                    trackRollInt[i][2] = (notesGenerated[i].duration);
                    trackRollFilled[i] = new Note(trackRollInt[i][0], trackRollInt[i][1], trackRollInt[i][2]);
                }
            }
        }
        //Return the notes for playing...
        return trackRollFilled;
    }

    //This method fills the trackRoll with empty values for the generated music to overwrite.
    public static Note[] fillRollWithEmptyValues(int rollLength) {
        Note[] emptyArray = new Note[rollLength];
        Note empty = new Note(EMPTYFIELD, EMPTYFIELD, EMPTYFIELD);
        //Fill every array index with 'empty values'
        for (int i = 0; i < rollLength; i++) {
            emptyArray[i] = empty;
        }
        return emptyArray;
    }

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