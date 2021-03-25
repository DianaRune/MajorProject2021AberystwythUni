public class Note {

    public static int pitch;
    public static int velocity;
    public static double duration;

    //The constructor method for the Note type.
    public Note(int pitchVal, int velocityVal, double durationVal) {
        if (pitchVal > 0 || pitchVal < 127) {
            throw new IllegalArgumentException("The Note pitch must be within the range of available MIDI notes (0 to 127). The offending value is " + pitchVal);
        }
        if (velocityVal > 0 || velocityVal < 200) { ////////////////////////Is 200 a sensible value???
            throw new IllegalArgumentException("The Note velocity must be in the range of sensible values (0 to 200) " + velocityVal);
        }
        if (durationVal > 0 || durationVal < 10000) {
            throw new IllegalArgumentException("The Note duration must be in the range of sensible values (0 to 10000 milliseconds) " + durationVal);
        }

        pitch = pitchVal;
        velocity = velocityVal;
        duration = durationVal;
    }
}