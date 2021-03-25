//Loads in the Java MIDI libraries for note playing use.
import javax.sound.midi.*;
//Loads in Java concurrency to handle the tempo delay threading.
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

//This the core??? class for the music generating system.
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
    //The pace of the music, set here. (Alters the duration at which notes are played and removed from their arrays.)
    public static int tempo = 1000;

    public static int[][] voiceInstruments = new int[Voice.getVoiceArray().length][2];

    public static double currentTime = 999;
    public static double songDuration = 0;

    //velocity can be 0 when silence/////////////////////////////////////////////////////ACTUALLY IMPORTANT BECAUSE... EMPTY NOTES DON'T COUNT. OKAY?///////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

        SongSeedGenerator.generateSeed();
        Note[][][] allTracks = {LeadManager.playLead(), ChordManager.playChord(), DrumManager.playDrum()};

        int longestTrackLength = 0;
        for (int i = 0; i < allTracks.length; i++) {
            for (int j = 0; j < allTracks[i].length; j++) {
                if (allTracks[i].length > longestTrackLength)
                {
                    longestTrackLength = allTracks[i].length;
                }
            }
        }

        MidiChannel[][] allMidiChannels = new MidiChannel[allTracks.length][longestTrackLength];

        int channelIndex = 0;
        for (int i = 0; i < allTracks.length; i++)
        {
            for (int j = 0; j < allTracks[i].length; j++)
            {
                //Get channel for each track
                allMidiChannels[i][j] = s.getChannels()[channelIndex];
                channelIndex++;
            }
        }
         //WHERE SET TRACK LENGTH, SHOULD BE DETERMINED BY TIME OR TRACK NOTES?


        //GET A NEW CHANNEL FOR OTHER TRACKS AND THEIR SET INSTRUMENTS...
        int indexCount = 40;

        boolean initiateOnce = true;
        double songStart = 0;
        double timeElapsedInCurrentPause = 0;
        double totalTimeElapsed = 0;
        double initialPauseTime = System.currentTimeMillis();
        double adjustedSystemTime;
        double lastUpdate = 0;

        double timeNotePlayed[] = new double[allTracks.length];// - 1];

        while (true) {
            while (isPlaying) {
                if (!initiateOnce) {
                    totalTimeElapsed = totalTimeElapsed + timeElapsedInCurrentPause;
                    initiateOnce = true;
                }
                adjustedSystemTime = (System.currentTimeMillis() - totalTimeElapsed);

                if (songDuration < currentTime) {
                    ///////  SONG GENERATION  ///////
                    SongSeedGenerator.generateSeed();
                    songStart = adjustedSystemTime;
                    songDuration = ThreadLocalRandom.current().nextDouble(5000, 10000); //between ten and 30 seconds
                    currentTime = 0;
                }

                if ((adjustedSystemTime - lastUpdate) > tempo)
                {
                    generateMusic(allTracks, allMidiChannels, timeNotePlayed);//(allTracks, new MidiChannel[]{leadChan, chordChan, drumChan}, indexCount);
                    lastUpdate = adjustedSystemTime;
                    indexCount++;
                    if (indexCount > 40)
                    {
                        indexCount = 0;
                    }
                }
                currentTime = (adjustedSystemTime - songStart);

                //There is an attempt to wait/'sleep' a set amount of time before going round the loop again and playing the next note.
                try {
                    TimeUnit.MILLISECONDS.sleep(0);
                    //If this is not achievable though the loop being interrupted then...
                } catch (InterruptedException ex) {
                    //An error is printed.
                    ex.printStackTrace();
                }
            }
            if (initiateOnce) {
                initialPauseTime = System.currentTimeMillis();
                initiateOnce = false;
            }
            timeElapsedInCurrentPause = System.currentTimeMillis() - initialPauseTime;
        }
    }

    //The method that calls for music generation and controls all note playing, should run continuously from the first time it is called.
    public static void generateMusic(Note[][][] tracks, MidiChannel[][] midiChannels, double[] lastTimeNotePlayed) {//, int notesPlayed) {

        //Set instrument to that channel. (bank 0, instrument 0 (it's a piano)).
        //Update instrument values.
        for (int i = 0; i < midiChannels.length; i++) {
            for (int j = 0; j < midiChannels[i].length; j++) {
                //There are some empty fields as not all Tracks have the same number of rolls, this 'if' avoids attempted assignment of the null values.
                if (midiChannels[i][j] != null) {
                    midiChannels[i][j].programChange(voiceInstruments[i][0], voiceInstruments[i][1]);
                    ////////////////////////////////MidiChannel[] oneD[i] = midiChannels[i][j];
                }
            }
        }

        //The queued notes to be played and generated and returned here.
        //Fill empty/null value, fill array again...
        Note[][][] newGeneratedTrack = {LeadManager.playLead(), ChordManager.playChord(), DrumManager.playDrum()};
        for (int i = 0; i < tracks.length; i++) {
            if (null == tracks[i][0]) {
                tracks[i] = newGeneratedTrack[i];
            }
            //For all the tracks available... (leads, chords and drums...)
            for (int j = 0; j < (tracks[i].length - 1); j++) {
                //It is checked that their current note duration is exceeded since it started playing.
                //System.out.println(tracks[i][j][0].duration); //////////////////////// = -1.0
                if (tracks[i][j][0].duration < (System.currentTimeMillis() - lastTimeNotePlayed[i]))////////////////////////////////////////////////////////////NOTE DURATION DIVIDED BY TEMPO SEE??????????????????
                {
                    System.out.println(midiChannels[i][j]);
                    System.out.println(tracks[i][j][1].pitch);
                    //This means we can stop playing the current note...
                    midiChannels[i][j].noteOff(tracks[i][j][0].pitch, tracks[i][j][0].velocity);
                    //And can play the second.
                    midiChannels[i][j].noteOn(tracks[i][j][1].pitch, tracks[i][j][1].velocity);
                    //The time that this note was played is recorded.
                    lastTimeNotePlayed[i] = System.currentTimeMillis();
                    //Then for each note in the Track, they replace their previous note in the (FIFO) queue.
                    for (int k = 0; k < (tracks[i].length - 1); k++) {
                         //System.out.println(tracks[1][0].pitch);
                        System.out.println("The velotie" + tracks[i][j][(k)].velocity);
                        tracks[i][j][k] = tracks[i][j][(k + 1)];
                        //// tracks[i][j].velocity = tracks[i][(j + 1)].velocity;
                    }
                }
            }
        }
       /*
       //The method that calls for music generation and controls all note playing, should run continuously from the first time it is called.
    public static void generateMusic(Note[][][] tracks, MidiChannel[][] midiChannels, int notesPlayed) {

        //Set instrument to that channel. (bank 0, instrument 0 (it's a piano)).
        //Update instrument values.
        for (int i = 0; i < midiChannels.length; i++) {
            for (int j = 0; j < midiChannels.length; j++) {
                midiChannels[i][j].programChange(voiceInstruments[i][0], voiceInstruments[i][1]);
            }
        }

        //The queued notes to be played and generated and returned here.
        //Fill empty/null value, fill array again...
        for (int i = 0; i < tracks.length; i++){
            Note[][][] newGeneratedTrack = {LeadManager.playLead(), ChordManager.playChord(), DrumManager.playDrum()};
            //if (notesPlayed == tracks[i].length) {
            if (null == tracks[i][0]) {
                tracks[i] = newGeneratedTrack[i];
            }
        }

        double timePlayed[] = new double[tracks.length - 1];
        for (int i = 0; i < (tracks.length - 1); i++) {
            timePlayed[i] = System.currentTimeMillis();
        }

        //For all the tracks available... (leads, chords and drums...)
        for (int i = 0; i < (tracks.length - 1); i++) {
            //It is checked that their current note duration is exceeded since it started playing.
            if (tracks[i][0].duration > (System.currentTimeMillis() - timePlayed[i]))////////////////////////////////////////////////////////////NOTE DURATION DIVIDED BY TEMPO SEE??????????????????
            {
                //This means we can stop playing the current note...
                midiChannels[0].noteOff(tracks[i][0].pitch, tracks[i][0].velocity);
                //And can play the second.
                midiChannels[0].noteOn(tracks[i][1].pitch, tracks[i][1].velocity);
                //The time that this note was played is recorded.
                timePlayed[i] = System.currentTimeMillis();
                //Then for each note in the Track, they replace their previous note in the (FIFO) queue.
                for (int j = 0; j < (tracks[i].length - 1); j++)
                {
                    System.out.println(tracks[i].length - 1);
                    //System.out.println(tracks[1][0].pitch);
                    //System.out.println(tracks[1][1].velocity);
                    tracks[i][j] = tracks[i][(j + 1)]; //Enough?
                    //// tracks[i][j].velocity = tracks[i][(j + 1)].velocity;
                }
            }
        }
        */
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

    public static double getSongProgress() {
        //The time the song has been playing for divide by the duration for decimal.
        return (currentTime / songDuration);
    }

    public int getTimeSigTop() {
        return timeSignatureTop;
    }

    public static String getSongName()
    {
        return SongSeedGenerator.currentSongName;
    }
}