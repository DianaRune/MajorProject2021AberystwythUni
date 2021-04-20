//This is a constructor class for the type 'Song'.
public class Song {
    //A song's attributes are...
    //The unique seed array which means the song can be identically replicated when these seeds are passed to their generators in future.
    public byte[] seeds;
    //The song also has a name to be displayed on the UI.
    public String name;
    //And the duration for how long the song played for.
    public double duration;

    //The constructor method for the Song type.
    public Song(byte[] songSeeds, String songName, double songDuration) {
        seeds = songSeeds;
        name = songName;
        duration = songDuration;
    }
}