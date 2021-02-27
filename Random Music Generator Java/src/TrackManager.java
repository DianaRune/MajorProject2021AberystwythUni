//Track Manager is the abstract parent class for 'Tracks' which extend from it...
public abstract class TrackManager {

    //An int[][] which will contain generated queued notes to be played in a FIFO fashion.
    //The first index is just a sensible length, generally 10 bars and the second represents the two values to be read for each index/note, 'pitch' and 'velocity'.
    //For example, trackRoll[0][0] = first note's pitch and trackRoll[0][1] = first note's velocity.
    public static int[][] trackRoll = new int[40][2];
    //The instrument/voice the track will have that will be applied to the channel it is played in.
    ////I'M NOT SURE THIS SHOULD BE HERE
    public static int instrument;

    //This method queues the generated notes (passed a parameter) into the empty fields of the trackRoll, and returns it.
    public static int[][] loadAndPlayRoll(int[][] trackRoll, int[] notesGeneratedFromPattern) {
        //Will queue for the length of trackRoll...
        for (int i = 0; i < trackRoll.length; i++) {
            //If there is an EMPTYFIELD available in the location...
            if (trackRoll[i][0] == MusicManager.EMPTYFIELD && trackRoll[i][1] == MusicManager.EMPTYFIELD) {
                //And the generated melody length is not exceeded.
                if (i < notesGeneratedFromPattern.length) {
                    //The index's pitch value is equal to the base pitch and the current octave value added.
                    trackRoll[i][0] = (notesGeneratedFromPattern[i] + 12 * MusicManager.OCTAVE);
                    //The index's velocity value is 100.
                    trackRoll[i][1] = 100;
                }
            }
        }
        //Return the notes for playing...
        return trackRoll;
    }

    //This method fills the trackRoll with empty values for the generated music to overwrite.
    public static void fillRollWithEmptyValues(int[][] trackRoll){
        trackRoll = MusicManager.fillEmpty(trackRoll);
    }



    //THESE METHODS??

    ////public static void getInstrument(){ (set/get?)
    ////}

    ////public abstract int[] rollPatterns(int randomPatternChosen, int timeSigTop, Key currentKey);
}