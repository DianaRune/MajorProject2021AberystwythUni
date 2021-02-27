import java.util.concurrent.ThreadLocalRandom;

public class DrumManager extends TrackManager {

    public static int[][] drum1Roll = new int[40][2];
    public static int instrument = 0;


    public static int[] drumPatterns(int randomPatternChosen, int timeSigTop, Key currentKey){

        int[] noteValues = currentKey.noteValues;
        int randomIndex = ThreadLocalRandom.current().nextInt(0, 7);
        int baseRandomNote = noteValues[randomIndex];
        int baseRandomNote2 = currentKey.noteValues[ThreadLocalRandom.current().nextInt(0, 7)];

        int [] returningPattern = new int[timeSigTop];

        for (int i = 0; i < timeSigTop; i++) {
            switch (randomPatternChosen) {
                //Straight same note in bar...
                case 0:
                    returningPattern[i] = baseRandomNote;
                    break;
                //Random up/down note...
                case 1:
                    returningPattern[i] = baseRandomNote;
                    int randomNoteInBar = ThreadLocalRandom.current().nextInt(0, timeSigTop);// + 1);
                    if (i == randomNoteInBar && randomIndex < 8)
                    {
                        if (Math.random() >= 0.5)
                        {
                            returningPattern[i] = noteValues[randomIndex + 1];
                        }
                        else if (randomIndex > 0)
                        {
                            returningPattern[i] = noteValues[randomIndex - 1];
                        }
                    }
                    break;
                //Note, up/down, note, random.
                case 2:
                    returningPattern[i] = baseRandomNote;
                    if (i == 2 && randomIndex < 8)
                    {
                        if (Math.random() <= 0.5)
                        {
                            returningPattern[i] = noteValues[randomIndex + 1];
                        }
                        else if (randomIndex > 0)
                        {
                            returningPattern[i] = noteValues[randomIndex - 1];
                        }
                    }
                    if (i == 4 && randomIndex < 8)
                    {
                        returningPattern[i] = baseRandomNote2;
                    }
                    break;
                default:
                    return null;
            }
        }
        return returningPattern;
    }
}