import java.util.concurrent.ThreadLocalRandom;

public class DrumManager extends TrackManager {

    public static int kickPatternSeed = 0;
    public static int snarePatternSeed = 0;
    public static int clapPatternSeed = 0;

    public static int drumTrackLength = TrackManager.trackLength;
    public static Note[] kickRoll = new Note[drumTrackLength];
    public static Note[] snareRoll = new Note[drumTrackLength];
    public static Note[] clapRoll = new Note[drumTrackLength];

    public static Note[][] playDrum() {
        kickRoll = TrackManager.fillRollWithEmptyValues(drumTrackLength);
        snareRoll = TrackManager.fillRollWithEmptyValues(drumTrackLength);
        clapRoll = TrackManager.fillRollWithEmptyValues(drumTrackLength);

        Note[][] allDrumTracks = new Note[][]{TrackManager.loadRoll(kickRoll, drumPatternsKick(TrackManager.getTimeSigTop(), TrackManager.getKey())), TrackManager.loadRoll(snareRoll, drumPatternsSnare(TrackManager.getTimeSigTop(), TrackManager.getKey())), TrackManager.loadRoll(clapRoll, drumPatternsClap(TrackManager.getTimeSigTop(), TrackManager.getKey()))};
        return allDrumTracks;
    }

    public static Note[] drumPatternsKick(int timeSigTop, Key currentKey) {

        int[] noteValues = currentKey.noteValues;

        int[] pitchPattern = new int[drumTrackLength];
        int[] velocityPattern = new int[drumTrackLength];
        int[] durationPattern = new int[drumTrackLength];

        int patternIndex = 0;
        while (patternIndex < drumTrackLength) {

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
                durationPattern[patternIndex] = 500;
                patternIndex++;
            }
        }
        Note[] returningPattern = new Note[drumTrackLength];
        for (int i = 0; i < drumTrackLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }
        return returningPattern;
    }

    public static Note[] drumPatternsSnare(int timeSigTop, Key currentKey) {

        int[] noteValues = currentKey.noteValues;

        int[] pitchPattern = new int[drumTrackLength];
        int[] velocityPattern = new int[drumTrackLength];
        int[] durationPattern = new int[drumTrackLength];

        int patternIndex = 0;
        while (patternIndex < drumTrackLength) {

            int baseNote = noteValues[2];

            for (int i = 0; i < timeSigTop; i++) {
                switch (snarePatternSeed) {
                    //Straight same note in bar...
                    case 0:
                        if (i % 2 == 0) {
                            pitchPattern[patternIndex] = baseNote;
                            velocityPattern[patternIndex] = 30;
                            velocityPattern[patternIndex + 1] = 0;
                        }
                        break;
                    case 1:
                        if (i % 2 == 1) {
                            pitchPattern[patternIndex + 1] = baseNote;
                            velocityPattern[patternIndex + 1] = 30;
                            velocityPattern[patternIndex] = 0;
                        }
                        break;
                    default:
                        return null;
                }
                durationPattern[patternIndex] = 300;
                patternIndex++;
            }
        }
        Note[] returningPattern = new Note[drumTrackLength];
        for (int i = 0; i < drumTrackLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }
        return returningPattern;
    }

    public static Note[] drumPatternsClap(int timeSigTop, Key currentKey) {

        int[] noteValues = currentKey.noteValues;

        int[] pitchPattern = new int[drumTrackLength];
        int[] velocityPattern = new int[drumTrackLength];
        int[] durationPattern = new int[drumTrackLength];

        int patternIndex = 0;

        //DECLARE BEFORE HERE FOR CONSISTENCY
        //SEEEEEEEEEEEEE?????????

        int baseNote = noteValues[3];

        while (patternIndex < drumTrackLength) {

            for (int i = 0; i < timeSigTop; i++) {               // =
                velocityPattern[patternIndex] = 0;
                durationPattern[patternIndex] = 750;

                switch (clapPatternSeed) {
                    //Straight same note in bar...
                    case 0:
                        if (i == 0) { // && randomIndex < 8) {
                            pitchPattern[patternIndex] = baseNote;
                            velocityPattern[patternIndex] = 20;
                        }
                        break;
                    default:
                        return null;
                }
                patternIndex++;
            }
        }
        Note[] returningPattern = new Note[drumTrackLength];
        for (int i = 0; i < drumTrackLength; i++) {
            returningPattern[i] = new Note(pitchPattern[i], velocityPattern[i], durationPattern[i]);
        }
        return returningPattern;
    }
}