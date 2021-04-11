//Allows for random number generation within a range.

import java.util.concurrent.ThreadLocalRandom;

//The leadManager of type 'Track', will manage and generate the lead instrument component of the music for playing.
public class LeadManager extends TrackManager {

    public static int leadTrackLength = TrackManager.trackLength;

    public static int leadPatternSeed;
    public static int noteValuesRandomIndex;

    //For example, lead1Roll[0][0] = note pitch and lead1Roll[0][1] = its note's velocity.
    public static Note[] lead1Roll = new Note[leadTrackLength];
    ///// public static int instrument = 0;

    //Called in the 'MusicManager', will generate and return the lead note array(s) to be played.
    public static Note[][] playLead() {
        //Fills the array with empty values in order to give it length and determine there isn't real notes under the index.
        ////////////TrackManager.fillRollWithEmptyValues(lead1Roll);
        lead1Roll = TrackManager.fillRollWithEmptyValues(leadTrackLength);
        //The lead track roll is are loaded into with their generated note values (current key and octave adjusted) and velocities.
        //Return all the leadRoll array(s) to the MusicManager for playing.
        Note[][] allLeadTracks = new Note[][]{TrackManager.loadRoll(lead1Roll, rollPatterns(TrackManager.getTimeSigTop(), TrackManager.getKey()))};
        //    System.out.println(allLeadTracks[0][0].pitch);
        //    System.out.println(allLeadTracks[0][1].pitch);
        //    System.out.println(allLeadTracks[0][2].pitch);
        //    System.out.println(allLeadTracks[0][3].pitch);
        //    System.out.println(allLeadTracks[0][4].pitch);
        //    System.out.println(allLeadTracks[0][5].pitch);
        return allLeadTracks;
    }

    //The method that generates the patterns and order of the notes to be played. It makes pattern around the number of beats/notes in the bar and the current key.
    public static Note[] rollPatterns(int timeSigTop, Key currentKey) {
        //The notes in the current key are retrieved and fed into an array to for access.
        int[] noteValues = currentKey.noteValues;
        //The array to be returned is declared and is the same length as the roll, to hold notes chosen from 'noteValues'.

        int[] pitchPattern = new int[leadTrackLength];
        int[] velocityPattern = new int[leadTrackLength];
        int[] durationPattern = new int[leadTrackLength];

        //The patternIndex is the index of the array being filled...
        int patternIndex = 0;
        //A random value is generated in the range of the 'noteValues' array index, this allows for a random note to be accessed from it.
        //These two values will be the starting point for pattern generation.
        //The initial random note is accessed.
        int baseRandomNote = noteValues[noteValuesRandomIndex];
        //The second random note is accessed like the first.
/////////////        int baseRandomNote2 = noteValues[ThreadLocalRandom.current().nextInt(0, 7)];//currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 7)];
        //################//This values is calculated and fed into the switch-case to determine what pattern should be loaded into the array next. (Bound is equal to the number of cases.)
        //################int randomPatternChosen = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        //This is cycled until filled.
        while (patternIndex < leadTrackLength) {
            //For the length of the number of 'beats in a bar', the returning array is filled with the select pattern.
            //(It is not desirable to fill with patterns that disrupt this structure as the system is not ready for that complexity.)
            for (int i = 0; i < timeSigTop; i++) {
                //Depending on the pattern entered. The following patterns could be loaded.
                switch (leadPatternSeed) {
                    //Straight same (base) note in bar...
                    case 0:
                        pitchPattern[patternIndex] = baseRandomNote;
                        break;
                    //Random up/down note...
                    case 1:
                        pitchPattern[patternIndex] = baseRandomNote;
                        if (i == 1 && (noteValuesRandomIndex - 1) >= 0) {
                            pitchPattern[patternIndex] = noteValues[noteValuesRandomIndex - 1];
                        }
                        break;
                    case 2:
                        pitchPattern[patternIndex] = baseRandomNote;
                        if (i == 1 && (noteValuesRandomIndex + 1) <= 8) {
                            pitchPattern[patternIndex] = noteValues[noteValuesRandomIndex + 1];
                        }
                        break;
                /*    //Base note, random up/down, base note, second random base note...
                    case 3:
                        returningPattern[patternIndex].pitch = baseRandomNote;
                        if (i == 2 && noteValuesRandomIndex < 8) {
                            if (Math.random() <= 0.5) {
                                returningPattern[patternIndex].pitch = noteValues[noteValuesRandomIndex + 1];
                            } else if (noteValuesRandomIndex > 0) {
                                returningPattern[patternIndex].pitch = noteValues[noteValuesRandomIndex - 1];
                            }
                        }
                        if ((patternIndex % 4 == 0) && noteValuesRandomIndex < 8) {
                            returningPattern[patternIndex].pitch = baseRandomNote2;
                        }
                        break;
                 */
                    default:
                        return null;
                }
                durationPattern[patternIndex] = 500;
                velocityPattern[patternIndex] = 25;
                if (patternIndex == timeSigTop) {
                    durationPattern[patternIndex] = 1500;
                    velocityPattern[patternIndex] = 50;
                }
                patternIndex++;
            }
        }
        //The array to be returned is declared and is the same length as the roll, to hold notes chosen from 'noteValues'.
        Note[] returningPattern = new Note[leadTrackLength];
        for (int i = 0; i < leadTrackLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }

        //These produce good vaLUES
        //    System.out.println(returningPattern[0].pitch);
        //    System.out.println(returningPattern[1].pitch);
        //    System.out.println(returningPattern[2].pitch);
        //    System.out.println(returningPattern[3].pitch);
        //    System.out.println(returningPattern[4].pitch);
        //    System.out.println(returningPattern[5].pitch);
        //    System.out.println(returningPattern[6].pitch);
        //    System.out.println(returningPattern[7].pitch);
        //    System.out.println(returningPattern[8].pitch);
        //    System.out.println(returningPattern[9].pitch);
        return returningPattern;
    }










    /*
    //The method that generates the patterns and order of the notes to be played. It makes pattern around the number of beats/notes in the bar and the current key.
    public static int[] rollPatterns(int timeSigTop, Key currentKey) {
        //The notes in the current key are retrieved and fed into an array to for access.
        int[] noteValues = currentKey.noteValues;
        //The array to be returned is declared and is the same length as the roll, to hold notes chosen from 'noteValues'.
        int[] returningPattern = new int[40];

        //The patternIndex is the index of the array being filled...
        int patternIndex = 0;
        //This is cycled until filled.
        while (patternIndex < 40) {
            //A random value is generated in the range of the 'noteValues' array index, this allows for a random note to be accessed from it.
            int noteValuesRandomIndex = ThreadLocalRandom.current().nextInt(0, 7);
            //These two values will be the starting point for pattern generation.
            //The initial random note is accessed.
            int baseRandomNote = noteValues[noteValuesRandomIndex];
            //The second random note is accessed like the first.
            int baseRandomNote2 = noteValues[ThreadLocalRandom.current().nextInt(0, 7)];//currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 7)];
            //This values is calculated and fed into the switch-case to determine what pattern should be loaded into the array next. (Bound is equal to the number of cases.)
            int randomPatternChosen = ThreadLocalRandom.current().nextInt(0, 2 + 1);

            //For the length of the number of 'beats in a bar', the returning array is filled with the select pattern.
            //(It is not desirable to fill with patterns that disrupt this structure as the system is not ready for that complexity.)
            for (int i = 0; i < timeSigTop; i++) {
                //Depending on the pattern entered. The following patterns could be loaded.
                switch (randomPatternChosen) {
                    //Straight same (base) note in bar...
                    case 0:
                        returningPattern[patternIndex] = baseRandomNote;
                        break;
                    //Random up/down note...
                    case 1:
                        returningPattern[patternIndex] = baseRandomNote;
                        //A random note in the bar is chosen to undergo a pitch up/down alteration by one note.
                        int randomNoteInBar = ThreadLocalRandom.current().nextInt(0, timeSigTop);
                        //If this random bar note index correlates to the current bar note index and is in the range of note values...
                        if (i == randomNoteInBar && noteValuesRandomIndex < 8) { //////////////////////////////////////////////////////////if (patternIndex == randomNoteInBar && noteValuesRandomIndex < 8) {
                            //There is a chance the note value will go up or down by a value.
                            if (Math.random() >= 0.5) {
                                returningPattern[patternIndex] = noteValues[noteValuesRandomIndex + 1];
                            } else if (noteValuesRandomIndex > 0) {
                                returningPattern[patternIndex] = noteValues[noteValuesRandomIndex - 1];
                            }
                        }
                        break;
                    //Base note, random up/down, base note, second random base note...
                    case 2:
                        returningPattern[patternIndex] = baseRandomNote;
                        if (i == 2 && noteValuesRandomIndex < 8) {
                            if (Math.random() <= 0.5) {
                                returningPattern[patternIndex] = noteValues[noteValuesRandomIndex + 1];
                            } else if (noteValuesRandomIndex > 0) {
                                returningPattern[patternIndex] = noteValues[noteValuesRandomIndex - 1];
                            }
                        }
                        if ((patternIndex % 4 == 0) && noteValuesRandomIndex < 8) {
                            returningPattern[patternIndex] = baseRandomNote2;
                        }
                        break;
                    default:
                        return null;
                }
                patternIndex++;
            }
        }
        return returningPattern;
    }
     */


    //#############################################################################################################
    //THIS IS LIKE TAKING THE PATTERN AND REPEATING IT FOR A SET NUMBER OF BARS. DON'T THINK I NEED IT... LATER.
    //#############################################################################################################

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
}