import java.util.concurrent.ThreadLocalRandom;

public class Chord {
    public static int[] chordStructure;

    public static Chord Ichord = new Chord(new int[]{0, 2, 4});
    public static Chord iichord = new Chord(new int[]{1, 3, 5});
    public static Chord iiichord = new Chord(new int[]{2, 4, 6});
    public static Chord ivchord = new Chord(new int[]{3, 5, 7});
    public static Chord vchord = new Chord(new int[]{4, 6, 8});
    public static Chord vichord = new Chord(new int[]{5, 7, 9});
    public static Chord viichord = new Chord(new int[]{6, 8, 10});
    public static Chord viiichord = new Chord(new int[]{7, 9, 11});

    ///////public int[] MAJORTRIAD = {0, 4, 7};
    ///////public int[] MINORTRIAD = {0, 3, 7};

    public Chord(int[] chordStruct) {
        chordStruct = chordStructure;
    }
}