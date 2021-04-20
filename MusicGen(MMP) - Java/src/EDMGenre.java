//The class that is responsible for generating lead Notes for each section in the EDM genre.
public class EDMGenre {
    //The seeds for each section.
    public static byte introLeadSeed = 0;
    public static byte verseLeadSeed = 0;
    public static byte chorusLeadSeed = 0;
    public static byte outroLeadSeed = 0;
    //The base value for the key.
    public static byte baseNoteIndex = 0;

    //Integer[] that are easier to manipulate that Note[], Notes are identified by these attributes shared index.
    public static int[] pitchPattern = new int[LeadPatterns.leadTrackLength];
    public static int[] velocityPattern = new int[LeadPatterns.leadTrackLength];
    public static int[] durationPattern = new int[LeadPatterns.leadTrackLength];
    //The number of notes in a bar.
    public static int timeSigTop = LeadPatterns.timeSigTop;

    //This method called in LeadPatterns updates the seed values here.
    public static void updateValuesInEDM()
    {
        baseNoteIndex = LeadPatterns.getNoteValuesRandomIndex();
        introLeadSeed = LeadPatterns.getIntroLeadSeed();
        verseLeadSeed = LeadPatterns.getVerseLeadSeed();
        chorusLeadSeed = LeadPatterns.getChorusLeadSeed();
        outroLeadSeed = LeadPatterns.getOutroLeadSeed();
    }

    //All patterns for the Intros for this genre.
    public static Note edmIntroMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (introLeadSeed) {
            //Straight same (base) note in bar...
            //- - - -
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                break;
            //Pitch down one note in the second note of the bar...
            //- v - -
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue == 1 && (baseNoteIndex - 1) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 1];
                }
                break;
            //Pitch up one note in the second note of the bar...
            //- ^ - -
            case 2:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue == 1 && (baseNoteIndex + 1) <= 8) {
                    pitchPattern[patternIndex] =  currentKey.noteValues[baseNoteIndex + 1];
                }
                break;
            default:
                break;
        }
        //The pitch and duration for every note.
        durationPattern[patternIndex] = 1000;
        velocityPattern[patternIndex] = 25;
        //The last on in the bar is longer and louder.
        if (indexValue == timeSigTop) {
            durationPattern[patternIndex] = 1500;
            velocityPattern[patternIndex] = 50;
        }
        //These values are returned to make up a Note.
        return new Note(pitchPattern[patternIndex], velocityPattern[patternIndex], durationPattern[patternIndex]);
    }

    public static Note edmVerseMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (verseLeadSeed) {
            //Last two notes in the bar are a note lower...
            //- - v v
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 3 && (baseNoteIndex - 1) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 1];
                }
                break;
            //Last two notes in the bar are a note higher...
            //- - ^ ^
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 3 && (baseNoteIndex + 1) <= 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 1];
                }
                break;
            default:
                break;
        }
        durationPattern[patternIndex] = 1000;
        velocityPattern[patternIndex] = 25;
        if (indexValue == timeSigTop) {
            durationPattern[patternIndex] = 1500;
            velocityPattern[patternIndex] = 50;
        }
        return new Note(pitchPattern[patternIndex], velocityPattern[patternIndex], durationPattern[patternIndex]);
    }

    public static Note edmChorusMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (chorusLeadSeed) {
            //Last two notes in the bar are 2 notes lower...
            //- - vv vv
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 3 && (baseNoteIndex - 2) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 2];
                }
                break;
            //Last two notes in the bar are 2 notes higher...
            //- - - ^^
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 3 && (baseNoteIndex + 2) < 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 2];
                }
                break;
            default:
                break;
        }
        durationPattern[patternIndex] = 1000;
        velocityPattern[patternIndex] = 25;
        if (indexValue == timeSigTop) {
            durationPattern[patternIndex] = 1500;
            velocityPattern[patternIndex] = 50;
        }
        return new Note(pitchPattern[patternIndex], velocityPattern[patternIndex], durationPattern[patternIndex]);
    }

    public static Note edmOutroMusic(int indexValue, int patternIndex, Key currentKey) {
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        //Depending on the pattern entered. The following patterns could be loaded.
        switch (outroLeadSeed) {
            ///Straight same (base) note in bar...
            //- - - -
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
            break;
        }
        durationPattern[patternIndex] = 1000;
        velocityPattern[patternIndex] = 25;
        if (indexValue == timeSigTop) {
            durationPattern[patternIndex] = 1500;
            velocityPattern[patternIndex] = 50;
        }
        return new Note(pitchPattern[patternIndex], velocityPattern[patternIndex], durationPattern[patternIndex]);
    }
}