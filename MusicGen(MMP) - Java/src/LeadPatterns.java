//The class that manages the genre specific Note generation and loading for the Lead Track.
public class LeadPatterns {
    //The length of the Lead Track roll to fill.
    public static int leadTrackLength = LeadManager.leadRollLength;
    //The number of notes in a bar.
    public static int timeSigTop = LeadManager.getTimeSigTop();

    //The seeds for this track. They determine the pattern the corresponding rolls should fill with. Default values are 0.
    //The genre seed determines the type of patterns, the roll should fill with.
    public static byte genrePatternSeed = 0;
    //This is a seed to determine the random 'base note' of the current key in the octave. It is an index for the notes available.
    //Integers will be added or deducted from this value in order to create melodies.
    public static byte noteValuesRandomIndex = 0;

    //These 'section' seeds determine the pattern for this roll at different parts of the song's structure.
    public static byte introLeadSeed = 0;
    public static byte verseLeadSeed = 0;
    public static byte chorusLeadSeed = 0;
    public static byte outroLeadSeed = 0;
    //The length of these sections.
    static double[] sectionDurations = {1000, 1000, 1000, 1000};
    //The current section being accessed and generated.
    private static int currentSection = 0;
    //The time the current section started playing in regards to the current song time.
    private static double initialCurrentSectionTime = 0;

    //This method called in LeadManager has it's seeds as the parameters. It returns generated notes and updates the section times.
    public static Note[] leadSections(Key currentKey) {
        //Updates the seeds and values used in all genres.
        genreValueUpdate();
        //The time the current song has been PLAYING for. (0 = song start)
        double currentSongTime = MusicManager.currentSongProgress;
        //The time the current section has been playing for. (The section time is the length of time it has been running for.)
        double currentSectionTime = currentSongTime - initialCurrentSectionTime;
        //If the current section time has been running for longer than it should...
        if (currentSectionTime >= sectionDurations[currentSection]) {
            //The music should move to the next section...
            currentSection++;
            //And the time at when this started is recorded so the length of time the section has been running for can be calculated.
            initialCurrentSectionTime = currentSongTime;
        }
        //The pattern generated for this section is returned.
        return fillLeadTrack(currentKey);
    }

    //The pattern is filled according to it's seeds and section here.
    public static Note[] fillLeadTrack(Key currentKey) {
        //The array to be returned is declared and is the same length as the roll, to hold notes chosen from 'noteValues'.
        Note[] returningPattern = new Note[leadTrackLength];
        //Each section's style of music fills it's corresponding array and it is later determined which one should be returned.
        Note[] introPattern = new Note[leadTrackLength];
        Note[] versePattern = new Note[leadTrackLength];
        Note[] chorusPattern = new Note[leadTrackLength];
        Note[] outroPattern = new Note[leadTrackLength];

        //The patternIndex is the index of the array being filled...
        int patternIndex = 0;
        //While the array length is not yet full, values are repeatedly returned...
        while (patternIndex < leadTrackLength) {
            //For the length of the number of 'beats in a bar', the returning array is filled with the select pattern.
            //(It is not desirable to fill with patterns that disrupt this structure as the system is not ready for that complexity.)
            for (int i = 0; i < timeSigTop; i++) {
                //Depending on the genre entered. The following patterns could be loaded.
                switch (genrePatternSeed) {
                    //For EDM genre music...
                    case 0:
                        introPattern[patternIndex] = EDMGenre.edmIntroMusic(i, patternIndex, currentKey);
                        versePattern[patternIndex] = EDMGenre.edmVerseMusic(i, patternIndex, currentKey);
                        chorusPattern[patternIndex] = EDMGenre.edmChorusMusic(i, patternIndex, currentKey);
                        outroPattern[patternIndex] = EDMGenre.edmOutroMusic(i, patternIndex, currentKey);
                        break;
                        //For Country genre music...
                    case 1:
                        introPattern[patternIndex] = CountryGenre.countryIntroMusic(i, patternIndex, currentKey);
                        versePattern[patternIndex] = CountryGenre.countryVerseMusic(i, patternIndex, currentKey);
                        chorusPattern[patternIndex] = CountryGenre.countryChorusMusic(i, patternIndex, currentKey);
                        outroPattern[patternIndex] = CountryGenre.countryOutroMusic(i, patternIndex, currentKey);
                        break;
                    default:
                        break;
                }
                //Another note has been returned and filled the array, the index is incremented.
                patternIndex++;
            }
        }
        //If in the first section...
        if (currentSection == 0) {
            //Load intro pieces.
            returningPattern = introPattern;
            //If the section is an odd value...
        } else if (currentSection % 2 == 1) {
            //Load verse pieces.
            returningPattern = versePattern;
            //If the section is an even value...
        } else if (currentSection % 2 == 0) {
            //Load chorus pieces.
            returningPattern = chorusPattern;
            //If in the last section...
        } else if (currentSection == sectionDurations[sectionDurations.length - 1]) {
            //Lead ending pieces.
            returningPattern = outroPattern;
        }
        //The genre and section appropriate Note[] is returned.
        return returningPattern;
    }

    //Getters for the seeds in this class to be accessed by the different genre classes.
    public static byte getNoteValuesRandomIndex()
    {
        return noteValuesRandomIndex;
    }
    public static byte getIntroLeadSeed()
    {
        return introLeadSeed;
    }
    public static byte getVerseLeadSeed()
    {
        return verseLeadSeed;
    }
    public static byte getChorusLeadSeed()
    {
        return chorusLeadSeed;
    }
    public static byte getOutroLeadSeed()
    {
        return outroLeadSeed;
    }

    //Updates the seeds and values used in all genres.
    public static void genreValueUpdate()
    {
        EDMGenre.updateValuesInEDM();
        CountryGenre.updateValuesInCountry();
    }
}