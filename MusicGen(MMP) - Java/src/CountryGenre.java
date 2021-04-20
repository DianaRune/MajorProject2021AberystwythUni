//The class that is responsible for generating lead Notes for each section in the Country genre.
public class CountryGenre {
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
    public static void updateValuesInCountry()
    {
        baseNoteIndex = LeadPatterns.getNoteValuesRandomIndex();
        introLeadSeed = LeadPatterns.getIntroLeadSeed();
        verseLeadSeed = LeadPatterns.getVerseLeadSeed();
        chorusLeadSeed = LeadPatterns.getChorusLeadSeed();
        outroLeadSeed = LeadPatterns.getOutroLeadSeed();
    }

    //All patterns for the Intros for this genre.
    public static Note countryIntroMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (introLeadSeed) {
            //Pitch down one note every odd number of the bar...
            //- v - v
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue % 2 == 1 && (baseNoteIndex - 1) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 1];
                }
                break;
            //Pitch up one note every odd number of the bar...
            //- ^ - ^
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue % 2 == 1 && (baseNoteIndex + 1) <= 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 1];
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

    public static Note countryVerseMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (verseLeadSeed) {
            //The first note in the bar is two notes higher, every even note is the base note, every odd note is one note higher and the last note is two notes higher.
            //^^ - ^ ^^
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 0 && (baseNoteIndex + 2) < 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 2];
                }
                else if (indexValue % 2 == 0) {
                    pitchPattern[patternIndex] = baseRandomPitch;
                }
                if (indexValue % 2 == 1 && (baseNoteIndex + 1) <= 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 1];
                }
                if (indexValue == timeSigTop && (baseNoteIndex + 2) < 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 2];
                }
                break;
            //The first note in the bar is two notes lower, every even note is the base note, every odd note is one note lower and the last note is two notes lower.
            //vv - v vv
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 0 && (baseNoteIndex - 2) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 2];
                }
                else if (indexValue % 2 == 0) {
                    pitchPattern[patternIndex] = baseRandomPitch;
                }
                if (indexValue % 2 == 1 && (baseNoteIndex - 1) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 1];
                }
                if (indexValue == timeSigTop && (baseNoteIndex - 2) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 2];
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

    public static Note countryChorusMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (chorusLeadSeed) {
            //The first note in the bar is two notes higher, every even note is one note higher, every odd note is two notes higher and the last note is the base note.
            //^^ ^ ^^ -
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 0 && (baseNoteIndex + 2) < 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 2];
                }
                else if (indexValue % 2 == 0 && (baseNoteIndex + 1) <= 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 1];
                }
                if (indexValue % 2 == 1 && (baseNoteIndex + 2) < 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 2];
                }
                if (indexValue == timeSigTop) {
                    pitchPattern[patternIndex] = baseRandomPitch;
                }
                break;
            //The first note in the bar is two notes lower, every even note is one note lower, every odd note is two notes lower and the last note is the base note.
            //vv v vv -
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue <= 0 && (baseNoteIndex - 2) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 2];
                }
                else if (indexValue % 2 == 0 && (baseNoteIndex - 1) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 1];
                }
                if (indexValue % 2 == 1 && (baseNoteIndex - 2) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 2];
                }
                if (indexValue == timeSigTop) {
                    pitchPattern[patternIndex] = baseRandomPitch;
                }
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

    public static Note countryOutroMusic(int indexValue, int patternIndex, Key currentKey) {
        //Depending on the pattern entered. The following patterns could be loaded.
        int baseRandomPitch = currentKey.noteValues[baseNoteIndex];
        switch (outroLeadSeed) {
            //Pitch down one note every odd number of the bar...
            //- v - v
            case 0:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue % 2 == 1 && (baseNoteIndex - 1) >= 0) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex - 1];
                }
                durationPattern[patternIndex] = 1000;
                velocityPattern[patternIndex] = 25;
                break;
            //Pitch up one note every odd number of the bar...
            //- ^ - ^
            case 1:
                pitchPattern[patternIndex] = baseRandomPitch;
                if (indexValue % 2 == 1 && (baseNoteIndex + 1) <= 8) {
                    pitchPattern[patternIndex] = currentKey.noteValues[baseNoteIndex + 1];
                }
                durationPattern[patternIndex] = 1000;
                velocityPattern[patternIndex] = 25;
                break;
            //Straight same (base) note in bar, last note is silent.
            //- - - x
            case 2:
                pitchPattern[patternIndex] = baseRandomPitch;
                durationPattern[patternIndex] = 1000;
                velocityPattern[patternIndex] = 25;
                if (indexValue == timeSigTop) {
                    durationPattern[patternIndex] = 1500;
                    //Silent.
                    velocityPattern[patternIndex] = 0;
                }
                break;
            default:
                break;
        }
        return new Note(pitchPattern[patternIndex], velocityPattern[patternIndex], durationPattern[patternIndex]);
    }
}