import java.util.concurrent.ThreadLocalRandom;

public class SongSeedGenerator {

    public static int[] currentSongSeed;
    public static String currentSongName;
    //FIFO style queue...
    public static int[][] previousSongs = new int[10][9];
    public static int currentPlaceInIndex = 0;

    public static void generateSeed()
    {
        currentSongName = SongnameDictionary.generateSongName();

        //This values is calculated and fed into the switch-case to determine what pattern should be loaded into the array next. (Bound is equal to the number of cases.)
        int leadSeed = ThreadLocalRandom.current().nextInt(0, 3);
        String chordSeed = chordPatterns();
        int chordSeedPath1 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath2 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath3 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath4 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath5 = ThreadLocalRandom.current().nextInt(0, 1);
        int kickSeed = 0;//ThreadLocalRandom.current().nextInt(0, 0);
        int snareSeed = ThreadLocalRandom.current().nextInt(0, 1);
        int clapSeed = 0;//ThreadLocalRandom.current().nextInt(0, 0);

        currentSongSeed = new int[]{leadSeed, chordSeedPath1, chordSeedPath2, chordSeedPath3, chordSeedPath4, chordSeedPath5, kickSeed, snareSeed, clapSeed};
        setPatterns(leadSeed, chordSeedPath1, chordSeedPath2, chordSeedPath3, chordSeedPath4, chordSeedPath5, kickSeed, snareSeed, clapSeed);

        //If the seed is constructed successfully then it's temporarily stored as a played song.
        if (currentSongSeed != null)
        {
            //All the previous songs are shifted (one index to the right) towards the end to make room for the new first value.
            for (int i = 0; i < previousSongs.length - 1; i++) {
                previousSongs[i + 1] = previousSongs[i];
            }
            //Then the first field is filled which could be the next field value. (FIFO)
            previousSongs[0] = currentSongSeed;
        }
            //Otherwise when the seed didn't generate properly an error is thrown...
        else {
            System.out.println("currentSongSeed is null.");
        }
    }

    public void playPreviousSeed()
    {
        currentPlaceInIndex++;
        currentSongSeed = previousSongs[currentPlaceInIndex];
        String[] seedChar = currentSongName.split("#");
        setPatterns(currentSongSeed[0], currentSongSeed[1], currentSongSeed[2], currentSongSeed[3], currentSongSeed[4], currentSongSeed[5], currentSongSeed[6], currentSongSeed[7], currentSongSeed[8]);
        //RESET TIME IN MUSICMANAGER
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public static void setPatterns(int leadSeed, int chordSeed1, int chordSeed2, int chordSeed3, int chordSeed4, int chordSeed5, int kickSeed, int snareSeed, int clapSeed)
    {
        LeadManager.leadPatternSeed = leadSeed;

        ChordManager.pathSeed1 = chordSeed1;
        ChordManager.pathSeed2 = chordSeed2;
        ChordManager.pathSeed3 = chordSeed3;
        ChordManager.pathSeed4 = chordSeed4;
        ChordManager.pathSeed5 = chordSeed5;

        DrumManager.kickPatternSeed = kickSeed;
        DrumManager.snarePatternSeed = snareSeed;
        DrumManager.clapPatternSeed = clapSeed;
    }

        /*
        //If the seed is constructed successfully then it's temporarily stored as a played song.
        if (currentSongSeed != null)
        {
            //If the played songs array hasn't yet been filled and the maximum index reached...
            if (previousSongsIndex < 10)
            {
                //Then the next empty field is filled...
                previousSongs[previousSongsIndex] = currentSongSeed;
                //And the desired queuing index increased.
                previousSongsIndex++;
            }
            //Otherwise when the array is full...
            else
            {
                //For each element
                for (int i = 0; i < previousSongsIndex; i++)
                {
                    //We want to remove the oldest song
                    previousSongs[i] = previousSongs[i + 1];
                }
                //And the generated seed is always stored at the last element to satisfy to comply to the FIFO method.
                previousSongs[previousSongsIndex] = currentSongSeed;
            }
        }
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
        if (Math.random() >= 0.5) {
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
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + "6,";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + "3,";
                pathChosen = 3;
            }
        }

        if (pathChosen == 3) {
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + "6,";
                pathChosen = 6;
            } else {
                chordProgressionPattern = chordProgressionPattern + "4,";
                pathChosen = 4;
            }
        }

        if (pathChosen == 6) {
            if (Math.random() >= 0.5) {
                chordProgressionPattern = chordProgressionPattern + "4,";
                pathChosen = 4;
            } else {
                chordProgressionPattern = chordProgressionPattern + "2,";
                chordProgressionPattern = chordProgressionPattern + "5,";
                chordProgressionPattern = chordProgressionPattern + "1,";
            }
        }

        if (pathChosen == 4) {
            if (Math.random() >= 0.5) {
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