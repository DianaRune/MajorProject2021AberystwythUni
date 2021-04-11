//Loads in the Java MIDI libraries for note playing use.
import javax.sound.midi.*;
//Loads in Java concurrency to handle threading. Allowing other processes to start their time-slices.
import java.util.concurrent.TimeUnit;

//This the main class for the music generating system.
//Here music is called and played from it's tracks that perform the generation. It also contains the getters and setters for the music 'flavour' determined by the user.
public class MusicManager implements MusicGen {

    //Boolean that is switched by playing/pausing. It prevents the track access and note playing if paused.
    //The value is 'volatile' (kept as one instance in RAM) so it can be seen and impacted by the other places (in this case the thread,) it is called from.
    public static volatile boolean isPlaying = true;
    //This is the currently used key that is accessed and set here.
    public static Key currentKey;
    //These integers describe the time signature set. (4/4 default.)
    public static int timeSignatureTop = 4;
    public static int timeSignatureBottom = 4;
    //The current octave chosen and set. This is the amount of times +12 is added to a note.
    public static int OCTAVE = 1;
    //The pace of the music, set here. (Alters the duration at which notes are played and removed from their arrays.) (Default 500ms/120 bpm).
    public static int tempo = 500;
    //This 2D int array holds the instrument values that represent a Voice from the Voice class for every value that represents a Track. It will be used to associate/assign a Voice to a Track.
    //The format is voiceInstruments[NumberOfTracks][2]. The second index holds the values for the Midi bank and program that should be accessed.
    public static int[][] voiceInstruments = new int[Voice.getVoiceArray().length][2];

    //The time for which the current song has been actively playing.
    public static double currentSongProgress = 999;
    //The length of the current song.
    public static double songDuration;
    //The time that the current song started.
    public static double songStart = 0;
    //The length of time for which the system has NOT been paused. (When the music is paused, this clock will stop adding time.)
    public static double adjustedSystemTime = 0;

    //A boolean that determines that the Tracks here need new notes. (Specifically used when going to a previous song.)
    public static boolean passNewNotes = true;
    //A value of the Note type. This represents a special value demonstrating the field in a track is 'empty' without throwing an error.
    //It is used to initialise the tracks with this 'empty' value and it also indicates that the track needs to fill with more values when the first Note index of a track is equal to the 'nullNote'.
    public static Note nullNote = new Note (-1, -1 , -1);

    //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //This method is called in a thread to run infinitely. It prepares and runs the music system. (It throws an exception if the Midi components can't be accessed.)
    //It manages time stamps to indicate the start and end of songs and queues and plays new music.
    public static void playMusic() throws MidiUnavailableException {

        /*
        Plan
        use reflections to find number of tracks then set that variable in track manager to be visible by music manager
        to use here...
        Note[][] allTracks = new Note[VALUE][];
         */

        //Get a synthesizer in order to access Java's MIDI channels and instruments.
        Synthesizer s = MidiSystem.getSynthesizer();
        //Open it.
        s.open();
        //Get the MIDI channels in the synthesizer (each channel supports one instrument).

        //A 3D array to hold all the existing tracks and their queued notes is created...
        //The format is allTracks[TrackNumber][RollNumber][NoteNumber]
        Note[][][] allTracks = {LeadManager.playLead(), ChordManager.playChord(), DrumManager.playDrum()};

        //All track rolls available are cycled through and the largest number of rolls in any one track is recorded...
        //This value is used to determine the required length of arrays in future.
        int longestTrackLength = 0;
        //For every value in the array...
        for (int i = 0; i < allTracks.length; i++) {
            for (int j = 0; j < allTracks[i].length; j++) {
                //If the new roll number is larger than the previous value...
                if (allTracks[i].length > longestTrackLength) {
                    //It is replaced.
                    longestTrackLength = allTracks[i].length;
                }
            }
        }

        //An array that holds the midi channels for the rolls in every track is created.
        MidiChannel[][] allMidiChannels = new MidiChannel[allTracks.length][longestTrackLength];
        //This array holds the length of time since the currently represented roll was last played.
        double timeNotePlayed[][] = new double[allTracks.length][longestTrackLength];

        //This variable determines the number channels required.
        int channelIndex = 0;
        //For every value in the array...
        for (int i = 0; i < allTracks.length; i++) {
            for (int j = 0; j < allTracks[i].length; j++) {
                for (int k = 0; k < allTracks[j].length; k++) {
                    //The null note value is assigned, to empty the arrays.
                    allTracks[i][j][k] = nullNote;
                }
                //Get and assign a Midi channel for every roll in each Track.
                allMidiChannels[i][j] = s.getChannels()[channelIndex];
                channelIndex++;
            }
        }


        //This boolean forces code to run once at the start of every pause/play. This allows for time stamps to be recorded once.
        boolean initiateOnce = true;
        //The system time of the initiation of the most recent pause.
        double initialPauseTime = System.currentTimeMillis();
        //Maintains the total length of time the music system has been in pause.
        double timeElapsedInPause = 0;
        //
        double lastUpdate = 0;

        //The following should be played on loop forever.
        while (true) {
            //While the system is 'not paused'...
            while (isPlaying) {
                //If the latch is satisfied... (The play button has just been pressed, after being in the pause status.)
                if (!initiateOnce) {
                    //The most recent length of time the system has been in pause for is added to the total time the system has been in this condition.
                    timeElapsedInPause = timeElapsedInPause + (System.currentTimeMillis() - initialPauseTime);
                    //And the latch is switched again to satisfy the initial pausing condition.
                    initiateOnce = true;
                }

                //The current length of time the song has been playing for is set. (The current system time minus the time the current song started playing.)
                currentSongProgress = (adjustedSystemTime - songStart);
                //If the current song duration is exceed by the length of time it has been playing for...
                if (songDuration < currentSongProgress) {
                    //The next song seed is generated and...
                    SongSeedGenerator.generateSeed();
                    //And the timestamps are adjusted ready for it to be played.
                    changingSong();
                }

                //The current system clock time minus the time elapsed in pause to get the time that music has been playing for.
                adjustedSystemTime = (System.currentTimeMillis() - timeElapsedInPause);
                //If this value minus the last time this method was called, is greater than the tempo length of time it is supposed to wait for...
                if ((adjustedSystemTime - lastUpdate) >= tempo) { /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //Then we can play the method again. (This will generate and play notes.)
                    generateMusic(allTracks, allMidiChannels, timeNotePlayed);
                    //The time is reset again until it is time for the next beat.
                    lastUpdate = adjustedSystemTime;
                }

                //There is an attempt to wait/'sleep' this thread momentarily, as it's good practice to allow other process have the opportunity to take their time-slices.
                try {
                    TimeUnit.MILLISECONDS.sleep(0);
                    //If this is not achievable though the loop being interrupted then...
                } catch (InterruptedException ex) {
                    //An error is printed.
                    ex.printStackTrace();
                }
            }
            //If the latch is satisfied... (The pause button has just been pressed, after being in the play status.)
            if (initiateOnce) {
                //Then the current time is recorded. This will allow the system to ignore the time passed whilst paused at any time.
                initialPauseTime = System.currentTimeMillis();
                //And the latch is switched again to satisfy the initial playing condition.
                initiateOnce = false;
            }
        }
    }

    //A thread devoted to playing the music created.
    static Thread musicThread = new Thread(() -> {
        //There is an attempt to call the method.
        try {
            MusicManager.playMusic();
            //An exception is caught if the Midi components aren't available.
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    });


    //The method that calls for music generation and controls all note playing, should run every time the tempo requires it to.
    public static void generateMusic(Note[][][] tracks, MidiChannel[][] midiChannels, double[][] lastTimeNotePlayed) {
        //Updating instrument values for each roll in a track...
        for (int i = 0; i < midiChannels.length; i++) {
            for (int j = 0; j < midiChannels[i].length; j++) {
                //There are some empty fields as not all Tracks have the same number of rolls, this 'if' avoids attempted assignment of the null values.
                if (midiChannels[i][j] != null) {
                    //Set the accessed roll's instrument to it's channel. (Example: bank 0, instrument 0 (it's a piano)).
                    midiChannels[i][j].programChange(voiceInstruments[i][0], voiceInstruments[i][1]);
                }
            }
        }

        //The notes to be queued are generated here...
        Note[][][] newGeneratedTrack = {LeadManager.playLead(), ChordManager.playChord(), DrumManager.playDrum()};
        //When any one track runs out of queued notes, the ones that have just been generated are assigned.
        for (int i = 0; i < tracks.length; i++) {
            //For all the rolls available... (The leads, chords and drums...)
            for (int j = 0; j < (tracks[i].length); j++) {
                //If a song is skipped forwards or to a previous track then this boolean is true...
                if (passNewNotes == true)
                {
                    //Stating that the all tracks should fill with the newly generated notes, they will be appropriate to the current song.
                    tracks = newGeneratedTrack;
                    //Then this boolean is set to false as this should not happen again, until the next time the condition is met.
                    passNewNotes = false;
                }
                //If the first note in the queue of a roll is null, then it is empty...
                if (tracks[i][j][0] == nullNote || tracks[i][j][0].duration == -1) {
                    //And should be filled with the new notes appropriate to the roll.
                    tracks[i] = newGeneratedTrack[i];
                    //The last note is set to null so it this indicator propagates when a note is played and 'removed' from the front of the queue in future.
                    tracks[i][j][(tracks[i][j].length - 1)] = nullNote;
                }
                //The default tempo (120bpm/500ms) is set here...
                double baseTempo120 = 500;
                //It is used to divide the current tempo... When this decimal is multiplied by the standard duration of a note. Then the duration is appropriately adjusted to suit the tempo.
                double tempoFactor = tempo/baseTempo120;
                //If this value is greater than the time the note was played last then...
                if ((tracks[i][j][0].duration * tempoFactor) <= (System.currentTimeMillis() - lastTimeNotePlayed[i][j]))
                {
                    //A new note can be played.
                    //This means we can stop playing the current note...
                    midiChannels[i][j].noteOff(tracks[i][j][0].pitch, tracks[i][j][0].velocity);
                    //And can play the second.
                    midiChannels[i][j].noteOn(tracks[i][j][1].pitch, tracks[i][j][1].velocity);
              /*      System.out.println("/////////////////////////////////////////////");
                        System.out.println("pitch" + tracks[2][0][0].pitch);
                        System.out.println("pitch" + tracks[2][0][1].pitch);
                        System.out.println("pitch" + tracks[2][0][2].pitch);
                        System.out.println("pitch" + tracks[2][0][3].pitch);
                        System.out.println("pitch" + tracks[2][0][4].pitch);
                        System.out.println(SongSeedGenerator.printSeed());
                        System.out.println("/////////////////////////////////////////////");
               */

                    //            System.out.println("This track: " + i + j + tracks[i][j][1].pitch);
                    //The time that this note was played is recorded.
                    lastTimeNotePlayed[i][j] = System.currentTimeMillis();
                    //Then for each note in the Track, their position is shifted one place to the left in the (FIFO) queue. (This includes the 'nullNote' indicator value in the max index.)
                    for (int k = 0; k < (tracks[i][j].length - 1); k++) {
                        //For example...
                        // 6, 7, 3, 5, null
                        //becomes
                        // 7, 3, 5, null, null
                        tracks[i][j][k] = tracks[i][j][(k + 1)];
                    }
/*
                    System.out.println(tracks[0][0][1].pitch);
                    System.out.println(tracks[0][0][2].pitch);
                    System.out.println(tracks[0][0][3].pitch);
                    System.out.println(tracks[0][0][4].pitch);
                    System.out.println(tracks[0][0][5].pitch);
                    System.out.println(tracks[0][0][6].pitch);
                    System.out.println(tracks[0][0][7].pitch);
                    System.out.println(tracks[0][0][8].pitch);
                    System.out.println(tracks[0][0][9].pitch);
                    System.out.println(tracks[0][0][10].pitch);
                    System.out.println(tracks[0][0][11].pitch);
                    System.out.println(tracks[0][0][12].pitch);
                    System.out.println(tracks[0][0][13].pitch);
                    System.out.println(tracks[0][0][14].pitch);
                    System.out.println(tracks[0][0][15].pitch);
                    System.out.println(tracks[0][0][16].pitch);
                    System.out.println(tracks[0][0][17].pitch);
                    System.out.println(tracks[0][0][18].pitch);
                    System.out.println(tracks[0][0][19].pitch);
                    System.out.println(tracks[0][0][20].pitch);
                    System.out.println(tracks[0][0][21].pitch);
                    System.out.println(tracks[0][0][22].pitch);
                    System.out.println(tracks[0][0][23].pitch);
                    System.out.println(tracks[0][0][24].pitch);
                    System.out.println(tracks[0][0][25].pitch);
                    System.out.println(tracks[0][0][39].pitch);
 */
                }
            }
        }
    }

    //This method sets the music's key...
    //It takes the value passed to it as a parameter and attempts to find and set the required key in the key class by searching for it via integer ID.
    public static void setKey(int chosenKeyID) {
        //Gets all keys in Key.
        Key keys[] = Key.getKeyArray();
        //Makes a boolean to determine when the method can stop searching and the key is found.
        boolean keyFound = false;
        //For every key there is...
        for (int i = 0; i < keys.length; i++) {
            //If the ID is correct...
            if (keys[i].ID == chosenKeyID) {
                //Then keyFound is true.
                keyFound = true;
                //The current Key is set to the desired Key in the Key class found.
                currentKey = keys[i];
                //return keys[i];
            }
        }
        //If the key is not found...
        if (keyFound == false) {
            //There should be an error printed.
            System.out.println("Not a valid ID, no key returned.");
        }
    }

    //This method pairs the selected voice values to their respective tracks, for calling, in an array.
    public static void setInstrument(int trackNumber, int bankValue, int programValue) {
        voiceInstruments[trackNumber][0] = bankValue;
        voiceInstruments[trackNumber][1] = programValue;
    }

    //This method pairs the selected voice values to their respective tracks, for calling, in an array.
    public static void setTimeSig(int timeSigTop, int timeSigBot) {
        timeSignatureTop = timeSigTop;
        timeSignatureBottom = timeSigBot;
    }

    //This method returns the decimal value of song progress.
    public static double getSongProgress() {
        //The time the song has been playing for divided by the duration for a decimal value.
        return (currentSongProgress / songDuration);
    }

    //This method returns the top part of the time signature.
    public int getTimeSigTop() {
        return timeSignatureTop;
    }

    //This method returns the String song name.
    public static String getSongName() {
        return SongSeedGenerator.getSongName();
    }

    //This method is called when the user wants to return to a previous song.
    public static void playPreviousSong() {
        //This requests for the previous seed to be accessed and it's notes to be generated.
        SongSeedGenerator.playPreviousSeed();
        //The time stamps are corrected.
        changingSong();
    }

    //This method is called when the user wants to skip to the next song.
    public static void playNextSong() {
        //This requests for a new seed to be stored and it's notes to be generated.
        SongSeedGenerator.playNextSeed();
        //The time stamps are corrected.
        changingSong();
    }

    //This method is called when the song changes...
    public static void changingSong()
    {
        //It adjusts the following time stamps...
        //The new time for the start of the song is set.
        songStart = adjustedSystemTime;
        //The new duration is accessed.
        songDuration = SongSeedGenerator.getSongDuration();
        //The progress is set to zero.
        currentSongProgress = 0;
        //And the boolean that determines that new roll notes are needed is set to true, to do so.
        passNewNotes = true;
    }
}