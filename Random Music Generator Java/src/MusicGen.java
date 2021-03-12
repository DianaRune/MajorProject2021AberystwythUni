import javax.sound.midi.MidiUnavailableException;

interface MusicGen {


    // declare constant fields
    // declare methods that abstract
    // by default.

    //void musicPlayer(int tempo) throws MidiUnavailableException;

    //static void setKey(int chosenKeyID);

    Key getKey();

    //static void setInstrument(int trackNumber, int bankValue, int programValue) {

    //}

    //static void setInstrument(int trackNumber, int bankValue, int programValue);

    void playMusic();

    int getTimeSigTop();

    //setInstrument();
    //playMusic();
}