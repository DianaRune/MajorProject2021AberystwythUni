//The chordManager of type 'Track', will manage and generate the chord instrument component of the music for playing.
public class ChordManager extends TrackManager {

    //The length of all chord rolls to be filled.
    //It is a multiple of the current time signature so bars remain intact. Having it '10 bars long' allows for more than one bar to fill it at a time, allowing for more complex patterns.
    public static int chordTrackLength = TrackManager.rollLength;

    //The seeds for this track. They determine the pattern the corresponding rolls should fill with. Default value is 0.
    public static int pathSeed1 = 0;
    public static int pathSeed2 = 0;
    public static int pathSeed3 = 0;
    public static int pathSeed4 = 0;
    public static int pathSeed5 = 0;

    //Note arrays which will contain generated, queued Notes to be played in a FIFO fashion, for these track rolls.
    //These arrays together contain the Notes required to play a chord when they share an index. When they're played together they make up the first chord queued.
    public static Note[] chord1Roll = new Note[chordTrackLength];
    public static Note[] chord2Roll = new Note[chordTrackLength];
    public static Note[] chord3Roll = new Note[chordTrackLength];

    //Called in the 'MusicManager', will generate and return the chord note arrays. These will be played simultaneously to make up a chord.
    public static Note[][] playChord() {
        //Fills the arrays with empty values in order to give them length and determine there aren't real notes under the index.
        chord1Roll = TrackManager.fillRollWithEmptyValues(chordTrackLength);
        chord2Roll = TrackManager.fillRollWithEmptyValues(chordTrackLength);
        chord3Roll = TrackManager.fillRollWithEmptyValues(chordTrackLength);

        //The chord track roll(s) are loaded into with their generated Note values.
        int[][][] generatedValues = generateChord(TrackManager.getKey());
        for (int i = 0; i < generatedValues.length; i++) {
            //Fill these arrays with the returned generated notes.
            //Each 'chord' will have its notes separated into the destined array.
            //Here the first/root note of the chord, 'i', is passed into the array that holds all the first/root notes and so forth.
            Note roll1Note = new Note(generatedValues[i][0][0], generatedValues[i][0][1], generatedValues[i][0][2]);
            chord1Roll[i] = roll1Note;
            Note roll2Note = new Note(generatedValues[i][1][0], generatedValues[i][1][1], generatedValues[i][1][2]);
            chord2Roll[i] = roll2Note;
            Note roll3Note = new Note(generatedValues[i][2][0], generatedValues[i][2][1], generatedValues[i][2][2]);
            chord3Roll[i] = roll3Note;
        }
        //Return all the chordRoll array(s) to the MusicManager for playing.
        Note[][] allRollsArray = {chord1Roll, chord2Roll, chord3Roll};
        return allRollsArray;
    }

    //This method generates chord notes in the selected key from the 'chordPatterns' string...
    public static int[][][] generateChord(Key chosenKey) {
        //The dictionary is filled with the chords that are set to be put there.
        ChordDictionary.fillDictionary();
        //The string returned, describing chord pattern and order, is broken by its "," delimiter.
        //The chord string is structured as so... 2,5,6,4,2,5,1,
        String[] progression = chordPatterns().split(",");
        //This array will be filled with the chord string values parsed as ints.
        int[] chordIndex = new int[8];
        //This array will contain the notes values for each chord...
        //Should have the index structure [number of chords][roll number][note/array number for chord]
        int[][][] chordNotes = new int[progression.length][3][3];
        //A loop will be called for each chord queued, they will be calculated into real note values, and returned.
        for (int i = 0; i < progression.length; i++) {
            //The individual chord string values are parsed as ints.
            chordIndex[i] = Integer.parseInt(progression[i]);
            //The respective dictionary value is accessed with the integer ID value.
            //Its length determines the loop times and it is used to calculate the note values...
            for (int j = 0; j < ChordDictionary.chord_dictionary.get(chordIndex[i]).length; j++) {
                //Adjust each note pitch value of the chord, according to key (by adding the root note value).
                chordNotes[i][j][0] = ChordDictionary.chord_dictionary.get(chordIndex[i])[j] + chosenKey.noteValues[0];
                //Velocity value set.
                chordNotes[i][j][1] = 20;
                //Duration value set.
                chordNotes[i][j][2] = 4000;
            }
        }
        //The chord notes are returned.
        return chordNotes;
    }

    //The method that generates the patterns and order of the chords to be played...
    public static String chordPatterns() {
        //start ii, and add the following strings representing chords.
        String chordProgressionPattern = "2,";
        //This value determines which chord should come next, depending on the previous value.
        int pathChosen = 0;

        //There is a 50% chance on what the next chord should be depending on the random decimal returned.
        if (pathSeed1 % 2 == 0) {
            //The chord value accessed is then added to be played...
            chordProgressionPattern = chordProgressionPattern + "5,";
            //And the potential next chord path is set.
            pathChosen = 5;
        } else {
            chordProgressionPattern = chordProgressionPattern + "3,";
            pathChosen = 3;
        }

        //Depending on the previous chord, either of these could be added next and determine what the future ones might be.
        if (pathChosen == 5) {
            if (pathSeed2 % 2 == 0) {
                chordProgressionPattern = chordProgressionPattern + "6,";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + "3,";
                pathChosen = 3;
            }
        }

        if (pathChosen == 3) {
            if (pathSeed3 % 2 == 0) {
                chordProgressionPattern = chordProgressionPattern + "6,";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + "4,";
                pathChosen = 4;
            }
        }

        if (pathChosen == 6) {
            if (pathSeed4 % 2 == 0) {
                chordProgressionPattern = chordProgressionPattern + "4,";
                pathChosen = 4;
            } else {
                chordProgressionPattern = chordProgressionPattern + "2,";
                chordProgressionPattern = chordProgressionPattern + "5,";
                chordProgressionPattern = chordProgressionPattern + "1,";
            }
        }

        if (pathChosen == 4) {
            if (pathSeed5 % 2 == 0) {
                chordProgressionPattern = chordProgressionPattern + "2,";
                chordProgressionPattern = chordProgressionPattern + "5,";
                chordProgressionPattern = chordProgressionPattern + "1,";
            } else {
                chordProgressionPattern = chordProgressionPattern + "5,";
                chordProgressionPattern = chordProgressionPattern + "1,";
            }
        }
        //The string is returned.
        return chordProgressionPattern;
    }
}