//These are the utility libraries for Hash mapping.
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
//Allows for random number generation within a range.
import java.util.concurrent.ThreadLocalRandom;

//A dictionary and constructor class. It assigns Key-Value pairs, These are IDs for accompanying words.
public class SongnameDictionary {
    //These are the 'POTENTIAL' attributes of a song name. (Not all require a value.)
    //Together they make up a name.
    public static String the = "The";
    public static String adjective1;
    public static String adjective2;
    public static String noun;

    //The current SongnameDictionary type. (Contains words to be assembled into the returning String.)
    public static SongnameDictionary currentSongName;

    //The constructor method for the SongnameDictionary type. This constructs the new name for a 'Song' type.
    //Random values from the appropriate dictionaries are passed to this constructing method...
    public SongnameDictionary(String theWord, String firstAdj, String minimumTitle, String nounEnd) {
        //There is a chance that some of these words will not be assigned to their attributes, allowing for a different style and length of title.
        //25% chance there will not be 'The' at the start of the song.
        if (Math.random() <= 0.25) {
            theWord = "";
        }
        //25% chance there will not be a noun and one adjective in the song.
        if (Math.random() <= 0.25) {
            nounEnd = "";
            firstAdj = "";
        }
        //25% chance there will not be one adjective in the song.
        if (Math.random() <= 0.25) {
            firstAdj = "";
        }
        //A title must consist of at least one adjective, also known as the minimum title. This is ensured that the value is not null here.
        Objects.requireNonNull(minimumTitle);
        //The attributes are then assigned.
        the = theWord;
        adjective1 = firstAdj;
        adjective2 = minimumTitle;
        noun = nounEnd;
    }

    //The adjective Map (for both the adjective attributes) is initialised...
    public static Map<Integer, String> adjective_dictionary = new IdentityHashMap<>();
    //And filled with values.
    public static String fillAndGetAdjDictionary() {
        //The String word 'Value' is assigned to the index 'Key'.
        adjective_dictionary.put(0, "Big");
        adjective_dictionary.put(1, "Small");
        adjective_dictionary.put(2, "Jelly");
        adjective_dictionary.put(3, "Gooey");
        adjective_dictionary.put(4, "Sad");
        adjective_dictionary.put(5, "Happy");
        //A random string value is chosen from the list and returned.
        int randomIndex = ThreadLocalRandom.current().nextInt(0, adjective_dictionary.size());
        String returnAdj = adjective_dictionary.get(randomIndex);
        return returnAdj;
    }

    //The noun Map (for the noun attribute) is initialised...
    public static Map<Integer, String> noun_dictionary = new IdentityHashMap<>();
    //And filled with values.
    public static String fillAndGetNounDictionary() {
        //The String word 'Value' is assigned to the index 'Key'.
        noun_dictionary.put(0, "Cat");
        noun_dictionary.put(1, "Table");
        noun_dictionary.put(2, "Crocodile");
        noun_dictionary.put(3, "Cake");
        noun_dictionary.put(4, "Book");
        noun_dictionary.put(5, "Mug");
        //A random string value is chosen from the list and returned.
        int randomIndex = ThreadLocalRandom.current().nextInt(0, noun_dictionary.size());
        String returnNoun = noun_dictionary.get(randomIndex);
        return returnNoun;
    }

    //This method constructs the words into a String 'song name', with the appropriate spaces.
    public static String generateSongName() {
        //The words are generated and set.
        currentSongName = new SongnameDictionary(the, fillAndGetAdjDictionary(), fillAndGetAdjDictionary(), fillAndGetNounDictionary());
        String returnName = "";
        //Depending on whether some attributes are empty, spaces are assigned appropriately. (It is ensured that there is one space between every word.)
        //If 'The' is not empty...
        if (currentSongName.the != "") {
            //Then a space is added after the existing "The" word.
            returnName = returnName + currentSongName.the + " ";
        }
        if (currentSongName.adjective1 != "") {
            returnName = returnName + currentSongName.adjective1 + " ";
        }
        returnName = returnName + currentSongName.adjective2 + " ";
        returnName = returnName + currentSongName.noun;
        //The String is returned for use. (Storing and display.)
        return returnName;
    }
}
