import javax.sound.midi.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Key {//implements Composer {  //extends?
    //Major and minor?
    public int ID;
    public int[] noteValues;

    public static int[] MAJORSCALEPATTERN = {0, 2, 4, 5, 7, 9, 11, 12};
    public static int[] MINORSCALEPATTERN = {0, 1, 3, 5, 7, 9, 10, 12};

    //keys
    //If number starts with 3 it's major that doesn't fit.
    //If number starts with 2, it's major and the number after is the order...
    //Minor is 3 digits starts with 1 and correlates with major.
    public static Key CMajorScale = new Key(200, scaleCalculator(200, Note.C));// = new Key(200, new int[]{2, 2});
    public static Key AMinorScale = new Key(100, scaleCalculator(100, Note.A));
    public static Key GMajorScale = new Key(201, scaleCalculator(201, Note.G));
    public static Key EMinorScale = new Key(101, scaleCalculator(101, Note.E));
    public static Key DMajorScale = new Key(202, scaleCalculator(202, Note.D));
    public static Key BMinorScale = new Key(102, scaleCalculator(102, Note.B));
    public static Key AMajorScale = new Key(203, scaleCalculator(203, Note.A));
    public static Key FSharpMinorScale = new Key(103, scaleCalculator(103, Note.FSHARP));
    public static Key EMajorScale = new Key(204, scaleCalculator(204, Note.E));
    public static Key CSharpMinorScale = new Key(104, scaleCalculator(104, Note.CSHARP));
    public static Key BMajorScale = new Key(205, scaleCalculator(205, Note.B));
    public static Key GSharpMinorScale = new Key(105, scaleCalculator(105, Note.GSHARP));

    //This is e-harmonic to the BMajorScale, so we have passed 'B's value' instead, because it is an existing note.
    public static Key CFlatMajorScale = new Key(306, scaleCalculator(306, Note.B));

    public static Key GFlatOrFSharpFlatMajorScale = new Key(207, scaleCalculator(207, Note.FSHARP));
    //SAME SCALE DESPITE MAJOR/MINOR
    public static Key GFlatOrFSharpMinorScale = new Key(107, scaleCalculator(107, Note.FSHARP));

    //CSharp and DFlat are the same scale...
    public static Key CSharpMajorScale = new Key(308, scaleCalculator(308, Note.CSHARP));
    //See? Just expressed differently.
    public static Key DFlatMajorScale = new Key(209, scaleCalculator(209, Note.CSHARP));

    public static Key BFlatMinorScale = new Key(109, scaleCalculator(109, Note.ASHARP));
    public static Key AFlatMajorScale = new Key(210, scaleCalculator(210, Note.GSHARP));
    public static Key FMinorScale = new Key(110, scaleCalculator(110, Note.F));
    public static Key EFlatMajorScale = new Key(211, scaleCalculator(211, Note.DSHARP));
    public static Key CMinorScale = new Key(111, scaleCalculator(111, Note.C));
    public static Key BFlatMajorScale = new Key(212, scaleCalculator(212, Note.ASHARP));
    public static Key GMinorScale = new Key(112, scaleCalculator(112, Note.G));
    public static Key FMajorScale = new Key(213, scaleCalculator(213, Note.F));
    public static Key DMinorScale = new Key(113, scaleCalculator(113, Note.D));

    public Key(int keyID, int[] keyNoteValues) {
        ID = keyID;
        noteValues = keyNoteValues;
    }

    public static int[] scaleCalculator(int keyID, Note rootNote) {
        int[] scale;// = new int[8];

        if (keyID - 100 > 100) {
            scale = MAJORSCALEPATTERN;
        } else {
            scale = MINORSCALEPATTERN;
        }

        for (int i = 0; i < 8; i++) {//scale.length; i++) {
            //Get value of Enum note and add it to the values in our scale to adapt to key.
            scale[i] = scale[i] + rootNote.ordinal();
        }
        return scale;
    }

    public static Key[] getKeyArray()
    {
        return new Key[]{CMajorScale, AMinorScale, GMajorScale, EMinorScale, DMajorScale, BMinorScale, AMajorScale, FSharpMinorScale, EMajorScale, CSharpMinorScale, BMajorScale, GSharpMinorScale, CFlatMajorScale, GFlatOrFSharpFlatMajorScale, GFlatOrFSharpMinorScale, CSharpMajorScale, DFlatMajorScale, BFlatMinorScale, AFlatMajorScale, FMinorScale, EFlatMajorScale, CMinorScale, BFlatMajorScale, GMinorScale, FMajorScale, DMinorScale};
    }
}