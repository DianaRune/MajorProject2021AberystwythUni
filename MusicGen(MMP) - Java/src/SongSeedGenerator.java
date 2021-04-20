//Allows for random number generation within a range.
import java.util.concurrent.ThreadLocalRandom;

//This class generates seeds that define a unique song. Seeds are temporarily 'saved' and can be accessed here.
public class SongSeedGenerator {

    //An 'empty' Song value. These attributes determines when there is no song in a location (of the 'previousSongQueue' array).
    public static Song emptySong = new Song(new byte[]{0}, "", 0);
    //The current song being generated.
    public static Song currentSong = emptySong;
    //FIFO style queue... (size: 10)
    //Previously generated songs are 'saved' here, for the length of ten songs, for the purpose of the 'previous song' feature.
    public static Song[] previousSongQueue = {emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong, emptySong};
    //Determines the current (previous) song being accessed. (Is an index for the 'previousSongQueue' array.)
    public static int currentPlaceInIndex = 0;
    //The number of 'sections' in the song structure. A typical song structure NUMOFSECTIONS = 6 could generate... (intro, verse, chorus, verse, chorus, outro).
    public static int NUMOFSECTIONS = 4;
    //This double array, the length of the number of sections, holds the duration of time in which each one should last.
    public static double[] segmentDurations = new double[NUMOFSECTIONS];

    //Generates a collection of random integers to be converted to bytes, these will be seeds that a new song consists of.
    public static void generateSeed() {
        //Seeds are generated...
        //A random song name is constructed from the dictionary.
        String name = SongnameDictionary.generateSongName();
        //These byte values are calculated and fed into switch-case statements to determine what pattern should be loaded into the array next. (Bound is equal to the number of cases.)
        byte genreSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte baseRandomNote = (byte) ThreadLocalRandom.current().nextInt(0, 7);
        byte[] leadSeeds = creatingLeadSeed(genreSeed);
        byte chordSeedPath1 = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte chordSeedPath2 = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte chordSeedPath3 = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte chordSeedPath4 = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte chordSeedPath5 = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte kickSeed = 0;//ThreadLocalRandom.current().nextInt(0, 0);
        byte snareSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
        byte clapSeed = 0;//ThreadLocalRandom.current().nextInt(0, 0);

        //The song length (between 1 and 2 minutes) is generated.
        double duration = ThreadLocalRandom.current().nextDouble(60000, 120000);
        //These doubles are the ratios the duration are broken up into. Different sections have different seeds assigned to generate different patterns for that section.
        double introRatio = ThreadLocalRandom.current().nextDouble(0.15, 0.25);
        double verseRatio = ThreadLocalRandom.current().nextDouble(0.2, 0.3);
        double chorusRatio = ThreadLocalRandom.current().nextDouble(0.2, 0.3);
        double outroRatio = (1 - introRatio - verseRatio - chorusRatio);
        //These are multiplied by the duration to get the allotted length in time. These are passed to the classes that require them.
        segmentDurations = new double[]{introRatio * duration, verseRatio * duration, chorusRatio * duration, outroRatio * duration};

        //The seeds are assembled into an array for storing in the new song...
        byte[] noteSeeds = new byte[]{genreSeed, baseRandomNote, leadSeeds[0], leadSeeds[1], leadSeeds[2], leadSeeds[3], chordSeedPath1, chordSeedPath2, chordSeedPath3, chordSeedPath4, chordSeedPath5, kickSeed, snareSeed, clapSeed};
        //Along with the name and duration.
        currentSong = new Song(noteSeeds, name, duration);
        //The pattern is set.
        setPatterns(currentSong.seeds, segmentDurations);
        //All the previous songs are shifted (one index to the right) towards the end to make room for the new first value.
        for (int i = 8; i >= 0; i--) {
            previousSongQueue[i + 1] = previousSongQueue[i];
        }
        //The song is temporarily stored as a previously played song in the first field value. (FIFO)
        previousSongQueue[0] = currentSong;
    }

    //Specifically the seeds for the sections (used in the Lead Track) are defined here.
    public static byte[] creatingLeadSeed(int genreSeed){
        byte introLeadSeed = 0;
        byte verseLeadSeed = 0;
        byte chorusLeadSeed = 0;
        byte outroLeadSeed = 0;

        //Depending on the number of patterns available for the genre, these bytes vary.
        if (genreSeed == 0) //EDM
        {
            introLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 3);
            verseLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
            chorusLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
            outroLeadSeed = (byte) 0;
        }
        if (genreSeed == 1) //Country
        {
            introLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
            verseLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
            chorusLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 2);
            outroLeadSeed = (byte) ThreadLocalRandom.current().nextInt(0, 3);
        }
        //The array is returned for use.
        return new byte[] {introLeadSeed, verseLeadSeed, chorusLeadSeed, outroLeadSeed};
    }

    //This method is accessed by the UI'S 'next song' button. It forces the generation of a new song or skips to the next song available in the previous song queue.
    static boolean playNextSeed() {
        //If the song is the first index and there is no 'next' previous song, then a new song is generated.
        if (currentPlaceInIndex == 0) {
            //Force new song generation.
            generateSeed();
        } else {
            //The position in the previous song queue is decreased by one, as the next song will now be the current song.
            currentPlaceInIndex--;
            //The current song is assigned.
            currentSong = previousSongQueue[currentPlaceInIndex];
            //The retrieved seeds and are applied to the system.
            setPatterns(currentSong.seeds, segmentDurations);
        }
        //Returns that the system should update.
        return true;
    }

    //This method is accessed by the UI'S 'previous song' button. It returns the last song on the previous song queue.
    static boolean playPreviousSeed() {
        //Boolean that determines whether the system should update. (Should occur if there is a previous song to retrieve.)
        boolean successfulSkip;
        //If the index exceeds the maximum length of the saved songs queue or the previous song is empty...
        if (currentPlaceInIndex >= previousSongQueue.length || (previousSongQueue[currentPlaceInIndex + 1] == emptySong)) {
            //Then a message of error should be printed.
            System.out.println("There are no more previously saved songs. (A maximum of 10 songs are saved in the 'previous song' queue at anytime.)");
            //And the system will not alter for the 'empty' song.
            successfulSkip = false;
            //Otherwise, there is a song to retrieve...
        } else {
            //The position in the previous song queue is increased by one, as the previous song will now be the current song.
            currentPlaceInIndex++;
            //The current song is assigned.
            currentSong = previousSongQueue[currentPlaceInIndex];
            //The retrieved seeds and are applied to the system.
            setPatterns(currentSong.seeds, segmentDurations);
            //Allows update as there is a previous song available.
            successfulSkip = true;
        }
        //Return whether the system should update.
        return successfulSkip;
    }

    //A setter method for the current song's seeds.
    public static void setPatterns(byte[] trackSeeds, double[] segmentLengths) {
        //The seeds are passed to their destined classes for appropriate Note generation.
        LeadPatterns.genrePatternSeed = trackSeeds[0];

        LeadPatterns.noteValuesRandomIndex = trackSeeds[1];

        LeadPatterns.introLeadSeed = trackSeeds[2];
        LeadPatterns.verseLeadSeed = trackSeeds[3];
        LeadPatterns.chorusLeadSeed = trackSeeds[4];
        LeadPatterns.outroLeadSeed = trackSeeds[5];

        ChordManager.pathSeed1 = trackSeeds[6];
        ChordManager.pathSeed2 = trackSeeds[7];
        ChordManager.pathSeed3 = trackSeeds[8];
        ChordManager.pathSeed4 = trackSeeds[9];
        ChordManager.pathSeed5 = trackSeeds[10];

        DrumManager.kickPatternSeed = trackSeeds[11];
        DrumManager.snarePatternSeed = trackSeeds[12];
        DrumManager.clapPatternSeed = trackSeeds[13];

        LeadPatterns.sectionDurations = segmentLengths;
    }

    //A getter method for the current song's name.
    public static String getSongName() {
        return currentSong.name;
    }

    //A getter method for the current song's duration.
    public static double getSongDuration() {
        return currentSong.duration;
    }
}