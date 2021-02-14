import javax.sound.midi.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Key implements Composer {  //extends?
    //Major and minor?
    public int ID;
    public int[] noteValues;

    public static int[] MAJORSCALEPATTERN = {0, 2, 4, 5, 7, 9, 11, 12};
    public static int[] MINORSCALEPATTERN = {0, 1, 3, 5, 7, 9, 10, 12};

    //keys
    public static Key CMajorScale;// = new Key(200, new int[]{2, 2});
    public static Key AMinorScale;
    public static Key GMajorScale;
    public static Key EMinorScale;
    public static Key DMajorScale;
    public static Key BMinorScale;
    public static Key AMajorScale;
    public static Key FSharpMinorScale;
    public static Key EMajorScale;
    public static Key CSharpMinorScale;
    public static Key BMajorScale;
    public static Key GSharpMinorScale;
    public static Key CFlatMajorScale;
    public static Key GFlatOrFSharpFlatMajorScale;
    public static Key GFlatOrFSharpMinorScale;
    public static Key CSharpMajorScale;
    public static Key DFlatMajorScale;
    public static Key BFlatMinorScale;
    public static Key AFlatMajorScale;
    public static Key FMinorScale;
    public static Key EFlatMajorScale;
    public static Key CMinorScale;
    public static Key BFlatMajorScale;
    public static Key GMinorScale;
    public static Key FMajorScale;
    public static Key DMinorScale;

    public Key(int keyID, int[] keyNoteValues) {
        ID = keyID;
        noteValues = keyNoteValues;
    }

    public static void intialiseKeys() {

        //public void keys() {
        //If number starts with 3 it's major that doesn't fit.
        //If number starts with 2, it's major and the number after is the order...
        //Minor is 3 digits starts with 1 and correlates with major.

        CMajorScale = new Key(200, scaleCalculator(CMajorScale.ID, Note.C));

        AMinorScale = new Key(100, scaleCalculator(AMinorScale.ID, Note.A));

        GMajorScale = new Key(201, scaleCalculator(GMajorScale.ID, Note.G));

        EMinorScale = new Key(101, scaleCalculator(EMinorScale.ID, Note.E));

        DMajorScale = new Key(202, scaleCalculator(DMajorScale.ID, Note.D));

        BMinorScale = new Key(102, scaleCalculator(BMinorScale.ID, Note.B));

        AMajorScale = new Key(203, scaleCalculator(AMajorScale.ID, Note.A));

        FSharpMinorScale = new Key(103, scaleCalculator(FSharpMinorScale.ID, Note.FSHARP));

        EMajorScale = new Key(204, scaleCalculator(EMajorScale.ID, Note.E));

        CSharpMinorScale = new Key(104, scaleCalculator(CSharpMinorScale.ID, Note.CSHARP));

        BMajorScale = new Key(205, scaleCalculator(BMajorScale.ID, Note.B));

        GSharpMinorScale = new Key(105, scaleCalculator(GSharpMinorScale.ID, Note.GSHARP));

        //This is e-harmonic to the BMajorScale, so we have passed 'B's value' instead, because it is an existing note.
        CFlatMajorScale = new Key(306, scaleCalculator(CFlatMajorScale.ID, Note.B));

        GFlatOrFSharpFlatMajorScale = new Key(207, scaleCalculator(GFlatOrFSharpFlatMajorScale.ID, Note.FSHARP));
        //SAME SCALE DESPITE MAJOR/MINOR
        GFlatOrFSharpMinorScale = new Key(107, scaleCalculator(GFlatOrFSharpMinorScale.ID, Note.FSHARP));

        //CSharp and DFlat are the same scale...
        CSharpMajorScale = new Key(308, scaleCalculator(CSharpMajorScale.ID, Note.CSHARP));
        //See? Just expressed differently.
        DFlatMajorScale = new Key(209, scaleCalculator(DFlatMajorScale.ID, Note.CSHARP));

        BFlatMinorScale = new Key(109, scaleCalculator(BFlatMinorScale.ID, Note.ASHARP));

        AFlatMajorScale = new Key(210, scaleCalculator(AFlatMajorScale.ID, Note.GSHARP));

        FMinorScale = new Key(110, scaleCalculator(FMinorScale.ID, Note.F));

        EFlatMajorScale = new Key(211, scaleCalculator(EFlatMajorScale.ID, Note.DSHARP));

        CMinorScale = new Key(111, scaleCalculator(CMinorScale.ID, Note.C));

        BFlatMajorScale = new Key(212, scaleCalculator(BFlatMajorScale.ID, Note.ASHARP));

        GMinorScale = new Key(112, scaleCalculator(GMinorScale.ID, Note.G));

        FMajorScale = new Key(213, scaleCalculator(FMajorScale.ID, Note.F));

        DMinorScale = new Key(113, scaleCalculator(DMinorScale.ID, Note.D));
    }

    public static int[] scaleCalculator(int keyID, Note rootNote) { //// Key key
        int[] scale;// = new int[8];

        if (keyID - 100 > 100) {
            scale = MAJORSCALEPATTERN;
            System.out.println("1");
        } else {
            scale = MINORSCALEPATTERN;
            System.out.println("2");
        }

        for (int i = 0; i < 8; i++) {//scale.length; i++) {
            //Get value of Enum note and add it to the values in our scale to adapt to key.
            scale[i] = scale[i] + rootNote.ordinal();
            System.out.println(scale[i]);
        }
        return scale;
    }

    public static Key[] getKeyArray()
    {
        return new Key[]{CMajorScale, AMinorScale, GMajorScale, EMinorScale, DMajorScale, BMinorScale, AMajorScale, FSharpMinorScale, EMajorScale, CSharpMinorScale, BMajorScale, GSharpMinorScale, CFlatMajorScale, GFlatOrFSharpFlatMajorScale, GFlatOrFSharpMinorScale, CSharpMajorScale, DFlatMajorScale, BFlatMinorScale, AFlatMajorScale, FMinorScale, EFlatMajorScale, CMinorScale, BFlatMajorScale, GMinorScale, FMajorScale, DMinorScale};
    }
}