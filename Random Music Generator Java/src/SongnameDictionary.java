import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

//Dictionary or Constructor
public class SongnameDictionary {
    public static String the = "The";
    public static String adjective1;
    public static String adjective2;
    public static String noun;

    public static Map<Integer, String> adjective_dictionary = new IdentityHashMap<>();

    public static SongnameDictionary currentSongName;

    //And filled with values.
    public static String fillAndGetAdjDictionary() {
        //The int[] value is the note structure for the chord and will be applied to the index for 'Key' 'noteValues'.
        adjective_dictionary.put(0, "Big");
        adjective_dictionary.put(1, "Small");
        adjective_dictionary.put(2, "Jelly");
        adjective_dictionary.put(3, "Gooey");
        adjective_dictionary.put(4, "Sad");
        adjective_dictionary.put(5, "Happy");
        int randomIndex = ThreadLocalRandom.current().nextInt(0, adjective_dictionary.size());
        String returnAdj = adjective_dictionary.get(randomIndex);
        return returnAdj;
    }

    public static Map<Integer, String> noun_dictionary = new IdentityHashMap<>();

    //And filled with values.
    public static String fillAndGetNounDictionary() {
        //The int[] value is the note structure for the chord and will be applied to the index for 'Key' 'noteValues'.
        noun_dictionary.put(0, "Cat");
        noun_dictionary.put(1, "Table");
        noun_dictionary.put(2, "Crocodile");
        noun_dictionary.put(3, "Cake");
        noun_dictionary.put(4, "Book");
        noun_dictionary.put(5, "Mug");
        int randomIndex = ThreadLocalRandom.current().nextInt(0, noun_dictionary.size());
        String returnNoun = noun_dictionary.get(randomIndex);
        return returnNoun;
    }

    public SongnameDictionary(String theWord, String firstAdj, String minimumTitle, String nounEnd) {
        if (Math.random() <= 0.25) {
            theWord = "";
        }
        if (Math.random() <= 0.25) {
            nounEnd = "";
            firstAdj = "";
        }
        if (Math.random() <= 0.25) {
            firstAdj = "";
        }

        Objects.requireNonNull(minimumTitle);
        the = theWord;
        adjective1 = firstAdj;
        adjective2 = minimumTitle;
        noun = nounEnd;
    }

        public static String generateSongName(){
        currentSongName = new SongnameDictionary(the, fillAndGetAdjDictionary(), fillAndGetAdjDictionary(), fillAndGetNounDictionary());

        String returnName = "";
        if (currentSongName.the != "")
        {
            returnName = returnName + currentSongName.the + " ";
        }
        if (currentSongName.adjective1 != "")
        {
            returnName = returnName + currentSongName.adjective1 + " ";
        }
        returnName = returnName + currentSongName.adjective2 + " ";
        returnName = returnName + currentSongName.noun;
        return returnName;
    }
}
