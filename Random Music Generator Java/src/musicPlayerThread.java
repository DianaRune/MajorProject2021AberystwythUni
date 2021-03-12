import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.concurrent.TimeUnit;

public class musicPlayerThread extends Thread{

    public static synchronized void run(int tempo) throws MidiUnavailableException { //musicPlayer(int tempo) throws MidiUnavailableException {
        int[][] leadNoteToPlay1 = new int[40][2];
        //THIS ISN'T GOING TO STAY HERE.
        MusicManager.setKey(109); //109 & 210
        //Get a synthesizer in order to access Java's MIDI channels and instruments.
        Synthesizer s = MidiSystem.getSynthesizer();
        //Open it.
        s.open();
        //Get the first MIDI channel in the synthesizer (each channel supports one instrument).
        MidiChannel chan = s.getChannels()[0];
        //Set instrument to that channel. (bank 0, instrument 0 (it's a piano)).
        chan.programChange(0, 0);
        System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

        //GET A NEW CHANNEL FOR OTHER TRACKS AND THEIR SET INSTRUMENTS...
        int[][][] AllChordRolls = ChordManager.playChord();
        int indexCount = 40;
        int instrument = 0;
        //While the music is not paused, the music should play continuously.
        while (MusicManager.isPlaying) {
            //The queued notes to be played and generated and returned here.
            //Fill empty/null value, fill array again...
            if (indexCount == 40) {
                indexCount = 0;
                leadNoteToPlay1 = LeadManager.playLead();
            }
            //They are then played with the respective note pitch and velocity.
            chan.noteOn(leadNoteToPlay1[0][0], leadNoteToPlay1[0][1]);
            for (int i = 0; i < (leadNoteToPlay1.length - 1); i++) {
                leadNoteToPlay1[i][0] = leadNoteToPlay1[(i + 1)][0];
                leadNoteToPlay1[i][1] = leadNoteToPlay1[(i + 1)][1];
            }
            //There is an attempt to wait/'sleep' a set amount of time before going round the loop again and playing the next note.
            try {
                TimeUnit.SECONDS.sleep(60 / (tempo * 2));
                //If this is not achievable though the loop being interrupted then...
            } catch (InterruptedException ex) {
                //An error is printed.
                ex.printStackTrace();
            }
            indexCount++;
            AllChordRolls = ChordManager.playChord();

            chan.noteOn(AllChordRolls[0][0][0], AllChordRolls[0][0][1]);
            //System.out.println("Chord 1 in music Manager: " + AllChordRolls[0][0][0]);
            chan.noteOn(AllChordRolls[1][0][0], AllChordRolls[1][0][1]);
            //System.out.println("Chord 2 in music Manager: " + AllChordRolls[1][0][0]);
            chan.noteOn(AllChordRolls[2][0][0], AllChordRolls[2][0][1]);
            //System.out.println("Chord 3 in music Manager: " + AllChordRolls[2][0][0]);

            for (int i = 0; i < AllChordRolls.length; i++) {
                AllChordRolls[0][i][0] = AllChordRolls[0][(i + 1)][0];
                AllChordRolls[0][i][1] = AllChordRolls[0][(i + 1)][1];
                AllChordRolls[1][i][0] = AllChordRolls[1][(i + 1)][0];
                AllChordRolls[1][i][1] = AllChordRolls[1][(i + 1)][1];
                AllChordRolls[2][i][0] = AllChordRolls[2][(i + 1)][0];
                AllChordRolls[2][i][1] = AllChordRolls[2][(i + 1)][1];
            }

            instrument++;

            try {
                TimeUnit.SECONDS.sleep(60 / tempo);          //////////////////////////////////////// divide by two because omg
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        ////while piano roll not empty or Got weird un-playable note in it...
        System.out.println("UGHHH EXCUSE ME... THIS ISN'T A PLAYABLE NOTE, THIS IS THE EMPTY PLACEHOLDER.");
    }

    public static void main(String args[]){
        musicPlayerThread thread = new musicPlayerThread();
        thread.start();
        System.out.println("THREAD PLAYING");
    }

    /*public static void runThread(){
        System.out.println("THREAD PLAYING");
    }
    public static void main(String args[]){
        musicPlayerThread thread = new musicPlayerThread();
        thread.start();
        System.out.println("THREAD PLAYING");
    }

     */
}