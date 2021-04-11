public class Note {

    public int pitch;
    public int velocity;
    public int duration;

    //The constructor method for the Note type.
    public Note(int pitchVal, int velocityVal, int durationVal) {
        if (pitchVal < -1 || pitchVal > 127) {
            throw new IllegalArgumentException("The Note pitch must be within the range of available MIDI notes (-1 to 127). The offending value is " + pitchVal);
        }
        if (velocityVal < -1 || velocityVal > 200) { ////////////////////////Is 200 a sensible value???
            throw new IllegalArgumentException("The Note velocity must be in the range of sensible values (-1 to 200) " + velocityVal);
        }
        if (durationVal < -1 || durationVal > 10000) {
            throw new IllegalArgumentException("The Note duration must be in the range of sensible values (-1 to 10000 milliseconds) " + durationVal);
        }

        pitch = pitchVal;
        velocity = velocityVal;
        duration = durationVal;
    }
}