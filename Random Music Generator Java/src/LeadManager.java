import java.util.concurrent.ThreadLocalRandom;

public class LeadManager extends TrackManager {

    public static int[][] lead1Roll = new int[40][2];
    public static int instrument = 0;

    public static int[][] playLead()
    {
        TrackManager.fillRollWithEmptyValues(lead1Roll);
        return TrackManager.loadAndPlayRoll(lead1Roll, rollPatterns(ThreadLocalRandom.current().nextInt(0, 2 + 1), MusicManager.timeSignatureTop, MusicManager.getKey()));
    }

    public static int[] rollPatterns(int randomPatternChosen, int timeSigTop, Key currentKey){

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
            System.out.println(returningPattern[i]);
        }
        return returningPattern;
    }



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
