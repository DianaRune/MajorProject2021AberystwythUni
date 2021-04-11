//The chordManager of type 'Track', will manage and generate the chord instrument component of the music for playing.
public class ChordManager extends TrackManager {

    public static int chordTrackLength = TrackManager.trackLength;

    public static int pathSeed1;
    public static int pathSeed2;
    public static int pathSeed3;
    public static int pathSeed4;
    public static int pathSeed5;

    //These arrays together contain the notes and their respective velocities required to play a chord when they share an index.
    //For example, chord1Roll[0][0] = a required note pitch for chord one and chord1Roll[0][1] = its note's velocity.
    //For example, chord2Roll[0][0] = a required note pitch for chord one and chord2Roll[0][1] = its note's velocity.
    //For example, chord3Roll[0][0] = a required note pitch for chord one and chord3Roll[0][1] = its note's velocity.
    //When they're played together they make up the first chord queued.
    //chord1Roll[1][0] = is a required note of the next (second) chord, and so on...
    public static Note[] chord1Roll = new Note[chordTrackLength];
    public static Note[] chord2Roll = new Note[chordTrackLength];
    public static Note[] chord3Roll = new Note[chordTrackLength];

    //Called in the 'MusicManager', will generate and return the chord note arrays. These will be played simultaneously to make up a chord.
    public static Note[][] playChord() {
        //Fills the arrays with empty values in order to give them length and determine there aren't real notes under the index.
        chord1Roll = TrackManager.fillRollWithEmptyValues(chordTrackLength);
        chord2Roll = TrackManager.fillRollWithEmptyValues(chordTrackLength);
        chord3Roll = TrackManager.fillRollWithEmptyValues(chordTrackLength);

        //Generate and return the chord notes to be loaded, in the current key set.
        //Should have the index structure [queued chord number][note/array number for chord]
        int[][][] generatedValues = generateChord(TrackManager.getKey());
        for (int i = 0; i < generatedValues.length; i++) {
            //Fill these arrays with the returned generated notes.
            //Each 'chord' will have its notes separated into the destined array.
            //Here the first note of the chord, 'i', is passed into the array that holds all the first notes and so forth.
            Note roll1Note = new Note(generatedValues[i][0][0], generatedValues[i][0][1], generatedValues[i][0][2]);
            chord1Roll[i] = roll1Note;
            Note roll2Note = new Note(generatedValues[i][1][0], generatedValues[i][1][1], generatedValues[i][1][2]);
            chord2Roll[i] = roll2Note;
            Note roll3Note = new Note(generatedValues[i][2][0], generatedValues[i][2][1], generatedValues[i][2][2]);
            chord3Roll[i] = roll3Note;
        }

        //Create a new 3D array to hold all the chord arrays...
        //Will have the structure [arrayNumber][noteValue][velocityValue]
        Note[][] allRollsArray = {chord1Roll, chord2Roll, chord3Roll};
        //Return all the chord arrays to the MusicManager for playing.
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
        //Should have the index structure [queued chord number][note/array number for chord]
        int[][][] chordNotes = new int[progression.length][3][3];////////////////////////////////////////////////////////////////////////////////////////////////////////// int and not Note
        //A loop will be called for each chord queued, they will be calculated into real note values, and returned.
        for (int i = 0; i < progression.length; i++) {
            //The individual chord string values are parsed as ints.
            chordIndex[i] = Integer.parseInt(progression[i]);
            //The respective dictionary value is accessed with the integer ID value.
            //Its length determines the loop times and it is used to calculate the note values...
            for (int j = 0; j < ChordDictionary.chord_dictionary.get(chordIndex[i]).length; j++) {
                //Adjust each note value of the chord, according to key (by adding the root note value).
                chordNotes[i][j][0] = ChordDictionary.chord_dictionary.get(chordIndex[i])[j] + chosenKey.noteValues[0];
                ////System.out.println("The chord notes for loading: " + chordNotes[i][j]);
                chordNotes[i][j][1] = 10;
                chordNotes[i][j][2] = 1000;
            }
        }
        //The chord notes are returned.
        return chordNotes;
    }












/*
    //This method generates chord notes in the selected key from the 'chordPatterns' string...
    public static Note[][] generateChord(Key chosenKey) {
        //The dictionary is filled with the chords that are set to be put there.
        ChordDictionary.fillDictionary();
        //The string returned, describing chord pattern and order, is broken by its "," delimiter.
        //The chord string is structured as so... 2,5,6,4,2,5,1,
        String[] progression = chordProgressionPattern.split(",");//######################################################################################################### chordPatterns().split(",");
        //This array will be filled with the chord string values parsed as ints.
        int[] chordIndex = new int[8];
        //This array will contain the notes values for each chord...
        //Should have the index structure [queued chord number][note/array number for chord]
        Note[][] chordNotes = new Note[progression.length][3];
        //A loop will be called for each chord queued, they will be calculated into real note values, and returned.
        for (int i = 0; i < progression.length; i++) {
            //The individual chord string values are parsed as ints.
            chordIndex[i] = Integer.parseInt(progression[i]);
            //The respective dictionary value is accessed with the integer ID value.
            //Its length determines the loop times and it is used to calculate the note values...
            for (int j = 0; j < ChordDictionary.chord_dictionary.get(chordIndex[i]).length; j++) {
                //Adjust each note value of the chord, according to key (by adding the root note value).
                chordNotes[i][j].pitch = ChordDictionary.chord_dictionary.get(chordIndex[i])[j] + chosenKey.noteValues[0];
                ////System.out.println("The chord notes for loading: " + chordNotes[i][j]);
            }
        }
        //The chord notes are returned.
        return chordNotes;
    }
*/


    //COULD BE AN INT ARRAY ACTUALLY??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    //The method that generates the patterns and order of the chords to be played...
    public static String chordPatterns() {
        //start ii, and add the following strings representing chords.
        String chordProgressionPattern = "2,";
        //This value determines which chord should come next, depending on the previous value.
        int pathChosen = 0;

        //There is a 50% chance on what the next chord should be depending on the random decimal returned.
        if (pathSeed1 % 2 == 0) { //0 or 1
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
        ////System.out.println(chordProgressionPattern);
        //The string is returned.
        return chordProgressionPattern;
    }
}



/*
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
 */