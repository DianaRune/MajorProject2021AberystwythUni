//The drumManager of type 'Track', will manage and generate the drum instrument component of the music for playing.
public class DrumManager extends TrackManager {

    //The length of all drum rolls to be filled.
    //It is a multiple of the current time signature so bars remain intact. Having it '10 bars long' allows for more than one bar to fill it at a time, allowing for more complex patterns.
    public static int drumRollLength = TrackManager.rollLength;

    //The seeds for this track. They determine the pattern the corresponding rolls should fill with. Default value is 0.
    public static int kickPatternSeed = 0;
    public static int snarePatternSeed = 0;
    public static int clapPatternSeed = 0;

    //The rolls that belong to this track. The kick, snare and clap of the drum.
    public static Note[] kickRoll = new Note[drumRollLength];
    public static Note[] snareRoll = new Note[drumRollLength];
    public static Note[] clapRoll = new Note[drumRollLength];

    //Called in the 'MusicManager', will generate and return the drum note array(s) to be played.
    public static Note[][] playDrum() {
        //Fills the array with empty values in order to give it length and determine there isn't real notes under the index.
        kickRoll = TrackManager.fillRollWithEmptyValues(drumRollLength);
        snareRoll = TrackManager.fillRollWithEmptyValues(drumRollLength);
        clapRoll = TrackManager.fillRollWithEmptyValues(drumRollLength);
        //The drum track roll(s) are loaded into with their generated Note values. (Octave adjusted via the TrackManager loading.)
        Note[][] allDrumTracks = new Note[][]{TrackManager.loadRoll(kickRoll, drumPatternsKick(TrackManager.getTimeSigTop(), TrackManager.getKey())), TrackManager.loadRoll(snareRoll, drumPatternsSnare(TrackManager.getTimeSigTop(), TrackManager.getKey())), TrackManager.loadRoll(clapRoll, drumPatternsClap(TrackManager.getTimeSigTop(), TrackManager.getKey()))};
        //Return all the drumRoll array(s) to the MusicManager for playing.
        return allDrumTracks;
    }

    //The patterns for the kick roll.
    public static Note[] drumPatternsKick(int timeSigTop, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        //The notes available for the current Key.
        int[] noteValues = currentKey.noteValues;

        //These int arrays are not static and are flexible towards individual notes being calculated and added. This arrays will be loaded into the static Note[] roll.
        int[] pitchPattern = new int[drumRollLength];
        int[] velocityPattern = new int[drumRollLength];
        int[] durationPattern = new int[drumRollLength];

        //Keep filling bars with pattern until full.
        int patternIndex = 0;
        while (patternIndex < drumRollLength) {

            int baseNote = noteValues[0];

            for (int i = 0; i < timeSigTop; i++) {
                switch (kickPatternSeed) {
                    //Straight same note in bar...
                    case 0:
                        pitchPattern[patternIndex] = baseNote;
                        break;
                    default:
                        return null;
                }
                velocityPattern[patternIndex] = 50;
                durationPattern[patternIndex] = 1000;
                patternIndex++;
            }
        }
        //Load the values in the Note[] and return.
        Note[] returningPattern = new Note[drumRollLength];
        for (int i = 0; i < drumRollLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }
        return returningPattern;
    }

    //The patterns for the snare roll.
    public static Note[] drumPatternsSnare(int timeSigTop, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        //The notes available for the current Key.
        int[] noteValues = currentKey.noteValues;

        //These int arrays are not static and are flexible towards individual notes being calculated and added. This arrays will be loaded into the static Note[] roll.
        int[] pitchPattern = new int[drumRollLength];
        int[] velocityPattern = new int[drumRollLength];
        int[] durationPattern = new int[drumRollLength];

        //Keep filling bars with pattern until full.
        int patternIndex = 0;
        while (patternIndex < drumRollLength) {

            int baseNote = noteValues[2];

            for (int i = 0; i < timeSigTop; i++) {
                switch (snarePatternSeed) {
                    //Snare on beat...
                    case 0:
                        if (i % 2 == 0) {
                            pitchPattern[patternIndex] = baseNote;
                            velocityPattern[patternIndex] = 30;
                            if (patternIndex + 1 < 40) {
                                velocityPattern[patternIndex + 1] = 0;
                            }
                        }
                        break;
                    case 1:
                        //Snare off beat...
                        if (i % 2 == 1) {
                            pitchPattern[patternIndex] = baseNote;
                            velocityPattern[patternIndex] = 30;
                            if (patternIndex + 1 < 40) {
                                velocityPattern[patternIndex + 1] = 0;
                            }
                        }
                        break;
                    default:
                        return null;
                }
                durationPattern[patternIndex] = 1500;
                patternIndex++;
            }
        }
        //Load the values in the Note[] and return.
        Note[] returningPattern = new Note[drumRollLength];
        for (int i = 0; i < drumRollLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }
        return returningPattern;
    }

    //The patterns for the clap roll.
    public static Note[] drumPatternsClap(int timeSigTop, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        //The notes available for the current Key.
        int[] noteValues = currentKey.noteValues;

        //These int arrays are not static and are flexible towards individual notes being calculated and added. This arrays will be loaded into the static Note[] roll.
        int[] pitchPattern = new int[drumRollLength];
        int[] velocityPattern = new int[drumRollLength];
        int[] durationPattern = new int[drumRollLength];

        //Keep filling bars with pattern until full.
        int patternIndex = 0;

        int baseNote = noteValues[3];

        while (patternIndex < drumRollLength) {

            for (int i = 0; i < timeSigTop; i++) {
                pitchPattern[patternIndex] = 12;
                velocityPattern[patternIndex] = 0;
                durationPattern[patternIndex] = 4500;

                switch (clapPatternSeed) {
                    //Clap at the start of a bar.
                    case 0:
                        if (i == 0) {
                            pitchPattern[patternIndex] = baseNote;
                            velocityPattern[patternIndex] = 20;
                            durationPattern[patternIndex] = 4500;
                        }
                        break;
                    default:
                        return null;
                }
                patternIndex++;
            }
        }
        //Load the values in the Note[] and return.
        Note[] returningPattern = new Note[drumRollLength];
        for (int i = 0; i < drumRollLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }
        return returningPattern;
    }
}