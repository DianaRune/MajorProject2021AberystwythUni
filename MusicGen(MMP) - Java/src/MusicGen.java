//The interface that represents the MusicManager and encapsulated music system as a whole.
//THe user can access the music's parameters and features here without seeing the rest of the system. (An object of the system is created in the 'controller' class.)
interface MusicGen {

    //This method starts the thread devoted to playing the music.
    void startMusicThread();

    //This method allows the system to switch between the music playing and music paused states.
    boolean switchPlayPause();

    //############################################################# SETTERS ##################################################################################################
    //This method sets the music's key...
    //It takes the value passed to it as a parameter and attempts to find and set the required key in the key class by searching for it via integer ID.
    void setKey(int chosenKeyID);

    //This method pairs the selected voice values to their respective tracks, for calling, in an array.
    void setInstrument(int trackNumber, int bankValue, int programValue);

    //This method sets the top and bottom half of the time signature, will alter future music.
    void setTimeSig(int timeSigTop, int timeSigBot);

    //This method sets the octave for the pitch to play in, will alter future music.
    void setOctave(int octaveValue);

    //This method sets the system's current tempo.
    void setTempo(int tempoValue);
    //#########################################################################################################################################################################

    //############################################################# GETTERS ##################################################################################################
    //This method returns the decimal value of song progress.
    double getSongProgress();

    //This method returns the String song name.
    String getSongName();
    //#########################################################################################################################################################################

    //This method is called when the user wants to return to a previous song.
    void playPreviousSong();

    //This method is called when the user wants to skip to the next song.
    void playNextSong();
}