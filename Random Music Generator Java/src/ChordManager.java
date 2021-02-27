import java.util.concurrent.ThreadLocalRandom;

public class ChordManager extends TrackManager {

    public static int[][] chord1Roll = new int[40][2];
    public static int[][] chord2Roll = new int[40][2];
    public static int[][] chord3Roll = new int[40][2];
    public static int instrument = 230;

    public static int[][][] playChord() {
        TrackManager.fillRollWithEmptyValues(chord1Roll);
        TrackManager.fillRollWithEmptyValues(chord2Roll);
        TrackManager.fillRollWithEmptyValues(chord3Roll);
        loadChord();
        //System.out.println("1 in chord Manager: " + chord1Roll[0][0]);
        //System.out.println("2 in chord Manager: " + chord2Roll[0][0]);
        //System.out.println("3 in chord Manager: " + chord3Roll[0][0]);
        int[][][] allRollsArray = {chord1Roll, chord2Roll, chord3Roll};
        return allRollsArray;
    }

    public static int[][] generateChord(Key chosenKey) {
        ChordDictionary.fillDictionary();
        String[] progression = chordPatterns().split(",", ChordDictionary.chord_dictionary.size());
        int[] chordIndex = new int[8];
        int[][] chordNotes = new int[8][3];
        for (int i = 0; i < progression.length; i++) {
            chordIndex[i] = Integer.parseInt(progression[i]);
            //Adjust each note of the chord, according to key...
            for (int j = 0; j < ChordDictionary.chord_dictionary.get(chordIndex[i]).length; j++) {
                System.out.println("These are the added note values: " + chosenKey.noteValues[0]);
                chordNotes[i][j] = ChordDictionary.chord_dictionary.get(chordIndex[i])[j] + chosenKey.noteValues[0]; // root note
                //System.out.println("The chord notes for loading: " + chordNotes[i][j]);/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        }
        return chordNotes;
    }

    public static String chordPatterns() {
        //start ii, choose one or the other... then chose that path...
        String chordProgressionPattern = "2";
        int pathChosen = 0;

        if (Math.random() >= 0.5) {
            chordProgressionPattern = chordProgressionPattern + ",5";
            pathChosen = 5;
        } else {
            chordProgressionPattern = chordProgressionPattern + ",3";
            pathChosen = 3;
        }

        if (pathChosen == 5) {
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + ",6";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + ",3";
                pathChosen = 3;
            }
        }

        if (pathChosen == 3) {
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + ",6";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + ",4";
                pathChosen = 4;
            }
        }

        if (pathChosen == 6) {
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + ",4";
                pathChosen = 4;
            } else {
                chordProgressionPattern = chordProgressionPattern + ",2";
                chordProgressionPattern = chordProgressionPattern + ",5";
                chordProgressionPattern = chordProgressionPattern + ",1";
            }
        }

        if (pathChosen == 4) {
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + ",2";
                chordProgressionPattern = chordProgressionPattern + ",5";
                chordProgressionPattern = chordProgressionPattern + ",1";
            } else {
                chordProgressionPattern = chordProgressionPattern + ",5";
                chordProgressionPattern = chordProgressionPattern + ",1";
            }
        }
        System.out.println(chordProgressionPattern);
        return chordProgressionPattern;
    }

    public static void loadChord() {
        int[][] chordNotesGenerated = generateChord(MusicManager.getKey());
        for (int i = 0; i < chord1Roll.length; i++) {
            if (chord1Roll[i][0] == MusicManager.EMPTYFIELD && chord1Roll[i][1] == MusicManager.EMPTYFIELD) {
                if (i < chordNotesGenerated.length) {
                    chord1Roll[i][0] = (chordNotesGenerated[i][0] + 12 * MusicManager.OCTAVE);
                    chord2Roll[i][0] = (chordNotesGenerated[i][1] + 12 * MusicManager.OCTAVE);
                    chord3Roll[i][0] = (chordNotesGenerated[i][2] + 12 * MusicManager.OCTAVE);
                    chord1Roll[i][1] = 50;
                    chord2Roll[i][1] = 50;
                    chord3Roll[i][1] = 50;
                }
            }
        }
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