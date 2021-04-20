//This is a constructor class for the type 'Voice'. (Designed for the programChange(bank, program) function from the javax.sound.midi.*; library.)
public class Voice {
    //It has an integer identifier attribute.
    public int ID;
    //The attribute values, accessed through the ID, define the MIDI instrument parameters that should be set to a track's rolls.
    //The bank of the desired instrument's location.
    public int bank;
    //The program of the bank. (The desired instrument's location.)
    public int program;
    //The name of the instrument that will be displayed in a dropdown menu on the UI.
    public String name;

    //Some Voices are defined for access.
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

    //The constructor method for the Voice type.
    public Voice(int voiceID, int voiceBank, int voiceProgram, String voiceName) {
        ID = voiceID;
        bank = voiceBank;
        program = voiceProgram;
        name = voiceName;
    }

    //Get and return every Voice in an array...
    public static Voice[] getVoiceArray() {
        return new Voice[]{piano, cleanGuitar, mutedGuitar, pickedBass, fretlessBass, squareWave, sawWave, spaceVoice, shamisen, kalimba, synthDrum};
    }
}