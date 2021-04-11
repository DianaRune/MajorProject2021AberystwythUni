/*
public class Genre {
   switch (leadPatternSeed) {
        //Straight same (base) note in bar...
        case 0:
            returningPattern[patternIndex].pitch = baseRandomNote;
            break;
        //Random up/down note...
        case 1:
            returningPattern[patternIndex].pitch = baseRandomNote;
            returningPattern[patternIndex].pitch = noteValues[noteValuesRandomIndex - 1];
            break;
        case 2:
            returningPattern[patternIndex].pitch = baseRandomNote;
            returningPattern[patternIndex].pitch = noteValues[noteValuesRandomIndex + 1];
            break;
        //Base note, random up/down, base note, second random base note...
        case 2:
            returningPattern[patternIndex].pitch = baseRandomNote;
            if (i == 2 && noteValuesRandomIndex < 8) {
                if (Math.random() <= 0.5) {
                    returningPattern[patternIndex].pitch = noteValues[noteValuesRandomIndex + 1];
                } else if (noteValuesRandomIndex > 0) {
                    returningPattern[patternIndex].pitch = noteValues[noteValuesRandomIndex - 1];
                }
            }
            if ((patternIndex % 4 == 0) && noteValuesRandomIndex < 8) {
                returningPattern[patternIndex].pitch = baseRandomNote2;
            }
            break;
        default:
            return null;
    }
    returningPattern[patternIndex].duration = 500;
    returningPattern[patternIndex].velocity = 25;
                if (patternIndex == timeSigTop) {
        returningPattern[patternIndex].duration = 1500;
        returningPattern[patternIndex].velocity = 50;
    }
    patternIndex++;
}
        }
                return returningPattern;
                }
}

 */