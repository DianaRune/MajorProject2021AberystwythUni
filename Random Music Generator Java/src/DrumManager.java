import java.util.concurrent.ThreadLocalRandom;

public class DrumManager extends TrackManager {

    public static int kickPatternSeed = 0;
    public static int snarePatternSeed = 0;
    public static int clapPatternSeed = 0;

    public static int TRACKLENGTH = 40;
    public static Note[] kickRoll = new Note[TRACKLENGTH];
    public static Note[] snareRoll = new Note[TRACKLENGTH];
    public static Note[] clapRoll = new Note[TRACKLENGTH];

    public static Note[][] playDrum() {
        TrackManager.fillRollWithEmptyValues(kickRoll);
        TrackManager.loadRoll(kickRoll, drumPatternsKick(TrackManager.getTimeSigTop(), TrackManager.getKey()));
        TrackManager.loadRoll(snareRoll, drumPatternsSnare(TrackManager.getTimeSigTop(), TrackManager.getKey()));
        TrackManager.loadRoll(clapRoll, drumPatternsClap(TrackManager.getTimeSigTop(), TrackManager.getKey()));
        Note[][] allDrumTracks = {kickRoll, snareRoll, clapRoll};
        return allDrumTracks;
    }

public static Note[] drumPatternsKick(int timeSigTop, Key currentKey){

        int[] noteValues = currentKey.noteValues;

        Note[] returningPattern = new Note[TRACKLENGTH];

        int patternIndex = 0;
        while (patternIndex < TRACKLENGTH) {

            int randomIndex = ThreadLocalRandom.current().nextInt(0, 7);///////////////KICK
            int baseRandomNote = noteValues[randomIndex];

            for (int i = 0; i < timeSigTop; i++) {
                switch (kickPatternSeed) {
                    //Straight same note in bar...
                    case 0:
                        returningPattern[patternIndex].pitch = baseRandomNote;
                        break;
                    default:
                        return null;
                }
                returningPattern[patternIndex].duration = 500;
                patternIndex++;
            }
        }
        return returningPattern;
    }

public static Note[] drumPatternsSnare(int timeSigTop, Key currentKey){

        int[] noteValues = currentKey.noteValues;

        Note[] returningPattern = new Note[TRACKLENGTH];

        int patternIndex = 0;
        while (patternIndex < TRACKLENGTH) {

            int randomIndex = ThreadLocalRandom.current().nextInt(0, 7);///////////////KICK
            int baseRandomNote = noteValues[randomIndex];

            for (int i = 0; i < timeSigTop; i++) {
                switch (snarePatternSeed) {
                    //Straight same note in bar...
                    case 0:
                        if (i % 2 == 0){
                                returningPattern[patternIndex].pitch = baseRandomNote;
                            }
                        break;
                    case 1:
                        if (i % 2 == 1){
                                returningPattern[patternIndex + 1].pitch = baseRandomNote;  ///////////////////////////////////////////// Yeah???
                            }
                        break;
                    default:
                        return null;
                }
                returningPattern[patternIndex].duration = 300;
                patternIndex++;
            }
        }
        return returningPattern;
    }

public static Note[] drumPatternsClap(int timeSigTop, Key currentKey){

        int[] noteValues = currentKey.noteValues;

        Note[] returningPattern = new Note[TRACKLENGTH];

        int patternIndex = 0;

        //DECLARE BEFORE HERE FOR CONSISTENCY
        //SEEEEEEEEEEEEE?????????


            int randomIndex = ThreadLocalRandom.current().nextInt(0, 7);
            int baseRandomNote = noteValues[randomIndex];
            int randomNoteInBar = ThreadLocalRandom.current().nextInt(0, timeSigTop);

        while (patternIndex < TRACKLENGTH) {

            for (int i = 0; i < timeSigTop; i++) {
                switch (clapPatternSeed) {
                    //Straight same note in bar...
                    case 0:
                        if (patternIndex == randomNoteInBar){ // && randomIndex < 8) {
                            returningPattern[patternIndex].pitch = baseRandomNote;
                        }
                        break;
                    default:
                        return null;
                }
                returningPattern[patternIndex].duration = 750;
                patternIndex++;
            }
        }
        return returningPattern;
    }
}