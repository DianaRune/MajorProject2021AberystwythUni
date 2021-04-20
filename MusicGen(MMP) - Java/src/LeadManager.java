//The leadManager of type 'Track', will manage and generate the lead instrument component of the music for playing.
public class LeadManager extends TrackManager {

    //The length of all lead rolls to be filled.
    //It is a multiple of the current time signature so bars remain intact. Having it '10 bars long' allows for more than one bar to fill it at a time, allowing for more complex patterns.
    public static int leadRollLength = TrackManager.rollLength;
    //A Note[] which will contain generated, queued Notes to be played in a FIFO fashion, for this track roll.
    public static Note[] lead1Roll = new Note[leadRollLength];

    //Called in the 'MusicManager', will generate and return the lead note array(s) to be played.
    public static Note[][] playLead() {
        //Fills the array with empty values in order to give it length and determine there isn't real notes under the index.
        lead1Roll = TrackManager.fillRollWithEmptyValues(leadRollLength);
        //The lead track roll(s) are loaded into with their generated Note values. (Octave adjusted via the TrackManager loading.)
        Note[][] allLeadTracks = new Note[][]{TrackManager.loadRoll(lead1Roll, LeadPatterns.leadSections(TrackManager.getKey()))};
        //Return all the leadRoll array(s) to the MusicManager for playing.
        return allLeadTracks;
    }
}