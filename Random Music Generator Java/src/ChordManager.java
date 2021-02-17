import java.util.concurrent.ThreadLocalRandom;

public class ChordManager {

    public static int[][] chord1Roll = new int[40][2];
    public static int[][] chord2Roll = new int[40][2];
    public static int[][] chord3Roll = new int[40][2];

    public static Chord[] generateChord(Key chosenKey) {
        String[] progression = chordPatterns().split(",");
        Chord[] allChords = null;
        for (int i = 0; i < progression.length; i++) {
            if (progression[i] == "1") {
                allChords[i] = Chord.Ichord;
            } else if (progression[i] == "2") {
                allChords[i] = Chord.iichord;
            } else if (progression[i] == "3") {
                allChords[i] = Chord.iiichord;
            } else if (progression[i] == "4") {
                allChords[i] = Chord.ivchord;
            } else if (progression[i] == "5") {
                allChords[i] = Chord.vchord;
            } else if (progression[i] == "6") {
                allChords[i] = Chord.vichord;
            } else if (progression[i] == "7") {
                allChords[i] = Chord.viichord;
            } else if (progression[i] == "8") {
                allChords[i] = Chord.viiichord;
            }
            //Adjust each note of the chord, according to key...
            for (int j = 0; j < allChords[i].chordStructure.length; j++) {
                allChords[i].chordStructure[j] = allChords[i].chordStructure[j] + chosenKey.noteValues[0]; // root note
            }
        }
        return allChords;
    }

        public static String chordPatterns() {
            String chordProgressionPattern = "";
            //start ii, choose one or the other... then chose that path...
            chordProgressionPattern = "2";
            int pathChosen = 0;

            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + ",5";
                pathChosen = 5;
            } else {
                chordProgressionPattern = chordProgressionPattern + ",3";
                pathChosen = 3;
            }

            if (pathChosen == 5) {
                chordProgressionPattern = chordProgressionPattern + ",6";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + ",3";
                pathChosen = 3;
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
                    pathChosen = 2;
                }
            }

            if (pathChosen == 4) {
                if (Math.random() >= 0.5) {
                    chordProgressionPattern = chordProgressionPattern + ",2";
                    pathChosen = 2;
                } else {
                    chordProgressionPattern = chordProgressionPattern + ",5";
                    pathChosen = 5;
                }
            }

            if (pathChosen == 2) {
                chordProgressionPattern = chordProgressionPattern + ",5";
                pathChosen = 5;
            }

            if (pathChosen == 5) {
                chordProgressionPattern = chordProgressionPattern + ",1";
            }

            return chordProgressionPattern;
        }

        public static void loadChord () {
            Chord[] chordNotesGenerated = generateChord(MusicManager.getKey());
            for (int i = 0; i < chord1Roll.length; i++) {
                if (chord1Roll[i][0] == MusicManager.EMPTYFIELD && chord1Roll[i][1] == MusicManager.EMPTYFIELD) {
                    if (i < chordNotesGenerated.length) {
                        chord1Roll[i][0] = (chordNotesGenerated[i].chordStructure[0] + 12 * MusicManager.OCTAVE);
                        chord2Roll[i][0] = (chordNotesGenerated[i].chordStructure[1] + 12 * MusicManager.OCTAVE);
                        chord3Roll[i][0] = (chordNotesGenerated[i].chordStructure[2] + 12 * MusicManager.OCTAVE);
                        chord1Roll[i][1] = 100;
                        chord2Roll[i][1] = 100;
                        chord3Roll[i][1] = 100;
                    }
                }
            }
        }

        public static void playChord ()
        {
            chord1Roll = MusicManager.fillEmpty(chord1Roll);
            chord2Roll = MusicManager.fillEmpty(chord2Roll);
            chord3Roll = MusicManager.fillEmpty(chord3Roll);
            while (chord1Roll[0][0] != 128 || chord1Roll[0][1] != -1) {
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //Today we want to pass our chords to note on in music manager
            }
    }
}
