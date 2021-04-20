//Constructor class for type 'Note', to describe a Note in a roll, from a Track, with these 3 int values...
public class Note {

    public int pitch;
    public int velocity;
    public int duration;

    //The constructor method for the Note type.
    public Note(int pitchVal, int velocityVal, int durationVal) {
        //(-1 is included as being a valid value for all these attributes of 'Note'. This is because it is frequently used within the system to indicate an empty field.)
        //If the value is not within the range of available pitches...
        if (pitchVal < -1 || pitchVal > 127) {
            //The user is notified of the error.
            throw new IllegalArgumentException("The Note pitch must be within the range of available MIDI notes (-1 to 127). The offending value is " + pitchVal);
        }
        //If the value is not within the range of available velocities...
        if (velocityVal < -1 || velocityVal > 200) {
            //The user is notified of the error.
            throw new IllegalArgumentException("The Note velocity must be in the range of sensible values (-1 to 200) " + velocityVal);
        }
        //If the value is not within the range of available durations...
        if (durationVal < -1 || durationVal > 10000) {
            //The user is notified of the error.
            throw new IllegalArgumentException("The Note duration must be in the range of sensible values (-1 to 10000 milliseconds) " + durationVal);
        }

        //Otherwise, if valid, these values can be set to the 'Note' construct.
        pitch = pitchVal;
        velocity = velocityVal;
        duration = durationVal;
    }
}