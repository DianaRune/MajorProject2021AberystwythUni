//This constructor class, Key, represents every musical key and it's respective notes.
public class Key {
    //It has an integer identifier.
    public int ID;
    //It has an array of integer values representing the notes in the scale for each respective Key.
    public int[] noteValues;
    //It has a boolean to determine whether the key is a major or minor one. (major == true, minor == false).
    public boolean isMajor;

    //These patterns are different for the two major/minor keys and represent the notes pressed in the scale, starting at the root.
    //This follows the skip 2, 2, 1, 2, 2, 2, 1 pattern...
    public static int[] MAJORSCALEPATTERN = {0, 2, 4, 5, 7, 9, 11, 12};
    //This follows the skip 1, 2, 2, 2, 2, 2, 1 pattern...
    public static int[] MINORSCALEPATTERN = {0, 1, 3, 5, 7, 9, 10, 12};

    //Here the keys are initialised...
    //If number starts with 3 it's major that doesn't fit.
    //If number starts with 2, it's major and the number after is the order...
    //Minor is 3 digits starts with 1 and correlates with major.
    public static Key CMajorScale = new Key(200, scaleCalculator(true, Pitch.C), true);
    public static Key AMinorScale = new Key(100, scaleCalculator(false, Pitch.A), false);
    public static Key GMajorScale = new Key(201, scaleCalculator(true, Pitch.G), true);
    public static Key EMinorScale = new Key(101, scaleCalculator(false, Pitch.E), false);
    public static Key DMajorScale = new Key(202, scaleCalculator(true, Pitch.D), true);
    public static Key BMinorScale = new Key(102, scaleCalculator(false, Pitch.B), false);
    public static Key AMajorScale = new Key(203, scaleCalculator(true, Pitch.A), true);
    public static Key FSharpMinorScale = new Key(103, scaleCalculator(false, Pitch.FSHARP), false);
    public static Key EMajorScale = new Key(204, scaleCalculator(true, Pitch.E), true);
    public static Key CSharpMinorScale = new Key(104, scaleCalculator(false, Pitch.CSHARP), false);
    public static Key BMajorScale = new Key(205, scaleCalculator(true, Pitch.B), true);
    public static Key GSharpMinorScale = new Key(105, scaleCalculator(false, Pitch.GSHARP), false);

    //This is e-harmonic to the BMajorScale, so 'B's value' are passed instead, because it is an existing pitch.
    public static Key CFlatMajorScale = new Key(306, scaleCalculator(true, Pitch.B), true);

    //GFlat and FSharp are the same scale...
    public static Key GFlatOrFSharpMajorScale = new Key(207, scaleCalculator(true, Pitch.FSHARP), true);
    //Just expressed differently. (But that does not matter here at present.)
    public static Key GFlatOrFSharpMinorScale = new Key(107, scaleCalculator(false, Pitch.FSHARP), false);

    //CSharp and DFlat are the same scale...
    public static Key CSharpMajorScale = new Key(308, scaleCalculator(true, Pitch.CSHARP), true);
    //Just expressed differently. (But that does not matter here at present.)
    public static Key DFlatMajorScale = new Key(209, scaleCalculator(true, Pitch.CSHARP), true);

    public static Key BFlatMinorScale = new Key(109, scaleCalculator(false, Pitch.ASHARP), false);
    public static Key AFlatMajorScale = new Key(210, scaleCalculator(true, Pitch.GSHARP), true);
    public static Key FMinorScale = new Key(110, scaleCalculator(false, Pitch.F), false);
    public static Key EFlatMajorScale = new Key(211, scaleCalculator(true, Pitch.DSHARP), true);
    public static Key CMinorScale = new Key(111, scaleCalculator(false, Pitch.C), false);
    public static Key BFlatMajorScale = new Key(212, scaleCalculator(true, Pitch.ASHARP), true);
    public static Key GMinorScale = new Key(112, scaleCalculator(false, Pitch.G), false);
    public static Key FMajorScale = new Key(213, scaleCalculator(true, Pitch.F), true);
    public static Key DMinorScale = new Key(113, scaleCalculator(false, Pitch.D), false);

    //The constructor method for the Key type.
    public Key(int keyID, int[] keyNoteValues, boolean isMajorBool) {
        //It has an integer identifier.
        ID = keyID;
        //It has an array of integer values representing the notes in the scale for each respective Key.
        noteValues = keyNoteValues;
        //It has a boolean to determine whether the key is a major or minor one. (major == true, minor == false).
        isMajor = isMajorBool;
    }

    //This method returns the int array for the Key's scale notes based off of the root note in the key.
    public static int[] scaleCalculator(boolean majorBool, Pitch rootNote) {

        //A new int[] is made to hold the notes calculated.
        int[] scale;

        //Depending on whether the key is major or minor as identified by it's 'keyID'.
        if (majorBool == true) {
            //The Major scale pattern is chosen...
            scale = MAJORSCALEPATTERN;
        } else {
            //Or the minor scale pattern is chosen.
            scale = MINORSCALEPATTERN;
        }

        //For each note in the 8 note scale...
        for (int i = 0; i < 8; i++) {
            //Get value of the Enum note and add it to the values in our scale to adapt to key.
            scale[i] = scale[i] + rootNote.ordinal();
        }
        //Return the note values.
        //As they are now, they will be interpreted with desired pitch by the noteOn() MIDI function.
        //They are the complete notes, ready for playing.
        return scale;
    }

    //Get and return every Key in an array...
    public static Key[] getKeyArray() {
        return new Key[]{CMajorScale, AMinorScale, GMajorScale, EMinorScale, DMajorScale, BMinorScale, AMajorScale, FSharpMinorScale, EMajorScale, CSharpMinorScale, BMajorScale, GSharpMinorScale, CFlatMajorScale, GFlatOrFSharpMajorScale, GFlatOrFSharpMinorScale, CSharpMajorScale, DFlatMajorScale, BFlatMinorScale, AFlatMajorScale, FMinorScale, EFlatMajorScale, CMinorScale, BFlatMajorScale, GMinorScale, FMajorScale, DMinorScale};
    }
}