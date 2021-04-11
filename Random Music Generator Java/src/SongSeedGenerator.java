import java.util.concurrent.ThreadLocalRandom;

public class SongSeedGenerator {

    //FIFO style queue...
    public static int[] seedsEmpty = {0};
    public static Song emptySong = new Song(seedsEmpty, "", 0);
    public static Song currentSong = emptySong;
    public static Song[] previousSongQueue = {emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong};
    //public static Song[] previousSongQueue = new Song[10];

    public static int currentPlaceInIndex = 0;

    public static void generateSeed() {

        //This values is calculated and fed into the switch-case to determine what pattern should be loaded into the array next. (Bound is equal to the number of cases.)
        int baseRandomNote = ThreadLocalRandom.current().nextInt(0, 7);

        int leadSeed = ThreadLocalRandom.current().nextInt(0, 3);
        int chordSeedPath1 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath2 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath3 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath4 = ThreadLocalRandom.current().nextInt(0, 1);
        int chordSeedPath5 = ThreadLocalRandom.current().nextInt(0, 1);
        int kickSeed = 0;//ThreadLocalRandom.current().nextInt(0, 0);
        int snareSeed = ThreadLocalRandom.current().nextInt(0, 1);
        int clapSeed = 0;//ThreadLocalRandom.current().nextInt(0, 0);

        double duration = ThreadLocalRandom.current().nextDouble(60000, 120000); //between 1 and 2 minutes
        String name = SongnameDictionary.generateSongName();
        int[] noteSeeds = new int[]{baseRandomNote, leadSeed, chordSeedPath1, chordSeedPath2, chordSeedPath3, chordSeedPath4, chordSeedPath5, kickSeed, snareSeed, clapSeed};
        currentSong = new Song(noteSeeds, name, duration);
        setPatterns(currentSong.seeds);

        //If the seed is constructed successfully then it's temporarily stored as a played song.
        //All the previous songs are shifted (one index to the right) towards the end to make room for the new first value.
        for (int i = 8; i >= 0; i--) {
            previousSongQueue[i + 1] = previousSongQueue[i];
        }
        //Then the first field is filled which could be the next field value. (FIFO)
        previousSongQueue[0] = currentSong;
    }
    //Otherwise when the seed didn't generate properly an error is thrown...

    public static void playPreviousSeed() {
        if (currentPlaceInIndex >= 9 || (previousSongQueue[currentPlaceInIndex + 1].seeds == seedsEmpty)) {
            System.out.println("There are no more previously saved songs. (A maximum of 10 songs are saved in the 'previous song' queue at anytime.)");
        } else {
            currentPlaceInIndex++;
            currentSong = previousSongQueue[currentPlaceInIndex];
            setPatterns(currentSong.seeds);
        }
    }

    public static void playNextSeed() {
        if (currentPlaceInIndex == 0) {
            //Force new song generation.
            generateSeed();
        } else {
            currentPlaceInIndex--;
            currentSong = previousSongQueue[currentPlaceInIndex];
            setPatterns(currentSong.seeds);
        }
    }

    public static void setPatterns(int[] trackSeeds) {
        LeadManager.noteValuesRandomIndex = trackSeeds[0];

        //(int leadSeed, int chordSeed1, int chordSeed2, int chordSeed3, int chordSeed4, int chordSeed5, int kickSeed, int snareSeed, int clapSeed)
        LeadManager.leadPatternSeed = trackSeeds[1];

        ChordManager.pathSeed1 = trackSeeds[2];
        ChordManager.pathSeed2 = trackSeeds[3];
        ChordManager.pathSeed3 = trackSeeds[4];
        ChordManager.pathSeed4 = trackSeeds[5];
        ChordManager.pathSeed5 = trackSeeds[6];

        DrumManager.kickPatternSeed = trackSeeds[7];
        DrumManager.snarePatternSeed = trackSeeds[8];
        DrumManager.clapPatternSeed = trackSeeds[9];
    }
    //'l#2#3#423#c#32#7#d#86#' = seed?

    public static String getSongName() {
        return currentSong.name;
    }

    public static double getSongDuration() {
        return currentSong.duration;
    }

    public static String printSeed()
    {
        String seed = ("The seed is = " + currentSong.seeds[0] + " " + currentSong.seeds[1]);
        return seed;
    }
}