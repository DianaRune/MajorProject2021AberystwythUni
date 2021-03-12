//These are the utility libraries for Hash mapping.
import java.util.IdentityHashMap;
import java.util.Map;

//The dictionary class, assigns Key-Value pairs, Key IDs for accompanying chord structures.
public class ChordDictionary {

    //The Map is initialised...
    public static Map<Integer, int[]> chord_dictionary = new IdentityHashMap<>();

    //And filled with values.
    public static void fillDictionary() {
        //The int[] value is the note structure for the chord and will be applied to the index for 'Key' 'noteValues'.
        chord_dictionary.put(1, (new int[]{0, 2, 4}));
        chord_dictionary.put(2, (new int[]{1, 3, 5}));
        chord_dictionary.put(3, (new int[]{2, 4, 6}));
        chord_dictionary.put(4, (new int[]{3, 5, 7}));
        chord_dictionary.put(5, (new int[]{4, 6, 8}));
        chord_dictionary.put(6, (new int[]{5, 7, 9}));
        //chord_dictionary.put(7, (new int[]{6, 8, 10}));
        //chord_dictionary.put(8, (new int[]{7, 9, 11}));
    }
}

//public static IdentityHashMap<Integer, int[]> chord_dictionary;
//Hashtable<String, int[]> chord_dictionary = new Hashtable<String, int[]>();
//Dictionary chord_Dictionary = new Hashtable();