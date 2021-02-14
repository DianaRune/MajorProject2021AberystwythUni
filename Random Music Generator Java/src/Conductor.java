    import javax.sound.midi.*;
    import java.util.concurrent.TimeUnit;


    //  source for a lot of this stuff: https://docs.oracle.com/javase/tutorial/sound/MIDI-synth.html



    //extend or (implements < doesn't work?)////////////////////////////////////////////////////////////////////////////////
    public class Conductor {

        //SHOULD J INDEX BE 1?
        public static int[][] pianoRoll = new int[40][2];
        public static int[][] chord1Roll = new int[40][2];
        public static int[][] chord2Roll = new int[40][2];
        public static int[][] chord3Roll = new int[40][2];
        public static int[][] drumRoll = new int[40][2];
        public static int[][] drum2Roll = new int[40][2];
        //velocity can be 0 when silence
        public static int EMPTYFIELD = -1;

        public static int OCTAVE = 3;
        //This is the amount of times +12 is added to a note...

        public static int TEMPO = 120; //bpm
        //second
        //60/30
        //minute divided by beat tempo = second interval

        public static int currentNote;


        public static void main(String args[]) throws MidiUnavailableException {

                MusicManager.fillEmpty();
                //musicLoader();    this Class' method.

                //isEmpty()
                //////////////////////////////////////////musicCreator(TEMPO); //time sig too?    this Class' method.
                MusicManager.musicPlayer(TEMPO);

                //if whatever,setkey...


                //MidiSystem.getSequence()


                //while piano roll not empty or Got weird un-lpayable note in it...
                System.out.println("UGHHH EXCUSE ME... THIS ISN'T A PLAYABLE NOTE, THIS IS THE EMPTY PLACEHOLDER.");
                //We want to fix the pianoroll... This should never happen? Adapt improvise overcome lol?
        }

        public static void musicLoader() {
            for (int i = 0; i < pianoRoll.length; i++) {
                //velocity can be 0 when silence
                if (pianoRoll[i][0] == EMPTYFIELD && pianoRoll[i][1] == EMPTYFIELD) {
                    if (i % 2 == 0) //remainder
                    {
                        pianoRoll[i][0] = ((Note.B.ordinal()) + (12 * OCTAVE));
                        pianoRoll[i][1] = 100;
                    }
                    else
                    {
                        pianoRoll[i][0] = ((Note.B.ordinal()) + (12 * OCTAVE));
                        pianoRoll[i][1] = 0;
                    }
                } else {
                    System.out.println("Piano music queue full.");
                }
            }
            for (int i = 0; i < drumRoll.length; i++) {
                //velocity can be 0 when silence
                if (drumRoll[i][0] == EMPTYFIELD && drumRoll[i][1] == EMPTYFIELD) {
                    if (i % 2 == 0)
                    {
                        drumRoll[i][0] = ((Note.B.ordinal()) + (12 * OCTAVE));
                        drumRoll[i][1] = 100;
                    }
                    else
                    {
                        drum2Roll[i][0] = ((Note.B.ordinal()) + (12 * OCTAVE));
                        drum2Roll[i][1] = 100;
                    }
                } else {
                    System.out.println("Piano music queue full.");
                }
            }
        }

        public void chordComposition()
        {
            int i = 0;
            int[] tempArray = new int[3];
            tempArray = MusicManager.triadChordCalculator(100, (Note.B));
            chord1Roll[chord1Roll.length][0] = tempArray[0];
            chord2Roll[chord2Roll.length][0] = tempArray[1];
            chord3Roll[chord3Roll.length][0] = tempArray[2];
        }

        public static void musicCreator(int tempo) throws MidiUnavailableException {
            // get a synthesizer
            while (pianoRoll[0][0] != 128 || pianoRoll[0][1] != -1) {
            Synthesizer s = MidiSystem.getSynthesizer();
            // open it (I forgot to do this, the tutorial forgot to mention it!)
            s.open();
            // get the first MIDI channel in the synth (each channel supports one instrument)
            MidiChannel chan = s.getChannels()[0];
            // set bank 0, instrument 0 (it's a piano)
            //chan.programChange(0, 0);
            chan.programChange(1024, 118);
            // play a C/G perfect fifth - this is asynchronous; the notes will start playing
            // while your code can do something else.
            //while (drumRoll[0][0] != 128 || drumRoll[0][1] != -1) {
                chan.noteOn((pianoRoll[0][0]), (pianoRoll[0][1]));
                //chan.noteOn((drum2Roll[0][0]), (drum2Roll[0][1]));
                //chan.noteOn((drumRoll[0][0]), (drumRoll[0][1]));
                //Ready next note
                //SHIFT ALL ELEMENTS ONE CLOSER THE THE FIRST ELEMENT
            MidiChannel chan1 = s.getChannels()[0];
                //minute divided by beat tempo = second interval
                // wait for 3 seconds
                try {
                    TimeUnit.SECONDS.sleep(60 / (tempo / 2));
                    chan.noteOff(pianoRoll[0][0]);/////////////////////////////////////// umm?
                    chan.noteOff(drumRoll[0][0]);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            //Make melodies in Condutor...
        }
    }