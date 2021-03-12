import java.util.concurrent.ThreadLocalRandom;

public class DrumManager extends TrackManager {

    public static int[][] drum1Roll = new int[40][2];
    public static int instrument = 0;

    public static int[][] playDrum() {
        TrackManager.fillRollWithEmptyValues(drum1Roll);
        return TrackManager.loadAndPlayRoll(drum1Roll, drumPatterns(TrackManager.getTimeSigTop(), TrackManager.getKey()));
    }

    public static int[] drumPatterns(int timeSigTop, Key currentKey){

        int[] noteValues = currentKey.noteValues;

        int[] returningPattern = new int[40];

        int patternIndex = 0;
        while (patternIndex < 40) {

            int randomIndex = ThreadLocalRandom.current().nextInt(0, 7);
            int baseRandomNote = noteValues[randomIndex];
            int baseRandomNote2 = currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 7)];
            int randomPatternChosen = ThreadLocalRandom.current().nextInt(0, 2 + 1);

            for (int i = 0; i < timeSigTop; i++) {
                switch (randomPatternChosen) {
                    //Straight same note in bar...
                    case 0:
                        returningPattern[patternIndex] = baseRandomNote;
                        break;
                    //Random up/down note...
                    case 1:
                        returningPattern[patternIndex] = baseRandomNote;
                        int randomNoteInBar = ThreadLocalRandom.current().nextInt(0, timeSigTop);
                        if (patternIndex == randomNoteInBar && randomIndex < 8) {
                            if (Math.random() >= 0.5) {
                                returningPattern[patternIndex] = noteValues[randomIndex + 1];
                            } else if (randomIndex > 0) {
                                returningPattern[patternIndex] = noteValues[randomIndex - 1];
                            }
                        }
                        break;
                    //Note, up/down, note, random.
                    case 2:
                        returningPattern[patternIndex] = baseRandomNote;
                        if (i == 2 && randomIndex < 8) {
                            if (Math.random() <= 0.5) {
                                returningPattern[patternIndex] = noteValues[randomIndex + 1];
                            } else if (randomIndex > 0) {
                                returningPattern[patternIndex] = noteValues[randomIndex - 1];
                            }
                        }
                        if ((patternIndex % 4 == 0) && randomIndex < 8) {
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
}