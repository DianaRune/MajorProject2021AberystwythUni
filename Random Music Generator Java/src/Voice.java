import javax.sound.midi.Instrument;

public class Voice {
    //It has an integer identifier.
    public int ID;
    //It has an array of integer values representing the notes in the scale for each respective Key.
    public int bank;
    public int program;
    public String name;

    //The int[] value is the note structure for the chord and will be applied to the index for 'Key' 'noteValues'.
    public static Voice piano = new Voice(0, 0, 0, "Piano");
    public static Voice cleanGuitar = new Voice(1, 0, 27, "Clean Guitar");
    public static Voice mutedGuitar = new Voice(2, 0, 28, "Muted Guitar");
    public static Voice pickedBass = new Voice(3, 0, 34, "Picked Bass");
    public static Voice fretlessBass = new Voice(4, 0, 35, "Fretless Bass");
    public static Voice squareWave = new Voice(5, 0, 80, "Square Wave");
    public static Voice sawWave = new Voice(6, 0, 81, "Saw Wave");
    public static Voice spaceVoice = new Voice(7, 0, 91, "Space Voice");
    public static Voice shamisen = new Voice(8, 0, 106, "Shamisen");
    public static Voice kalimba = new Voice(9, 0, 108, "Kalimba");
    public static Voice synthDrum = new Voice(10, 0, 118, "Synth Drum");

    public Voice(int voiceID, int voiceBank, int voiceProgram, String voiceName) {
        ID = voiceID;
        bank = voiceBank;
        program = voiceProgram;
        name = voiceName;
    }

    public static Voice[] getVoiceArray() {
        return new Voice[]{piano, cleanGuitar, mutedGuitar, pickedBass, fretlessBass, squareWave, sawWave, spaceVoice, shamisen, kalimba, synthDrum};
    }

}


//public static IdentityHashMap<Integer, int[]> chord_dictionary;
//Hashtable<String, int[]> chord_dictionary = new Hashtable<String, int[]>();
//Dictionary chord_Dictionary = new Hashtable();