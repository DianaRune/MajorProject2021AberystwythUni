import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Sean {
/*
    Scanner in = new Scanner(System.in);
    boolean alarmActive;
    time currentTime;
    time alarmTime;

    //Loops the user alarm menu 5 times.
    public void forLoopMenu() {
        for (int i = 0; i < 5; i++) {
            userAlarmMenu();
        }
    }

    //Loops the user alarm menu infinitely.
    public void whileLoopMenu() {
        do {
            userAlarmMenu();
        } while (true);
    }

    //The user interface we call.
    public void userAlarmMenu(){
        //Provides the user with initial menu options.
        System.out.println("Hello user!! Enter...");
        System.out.println("A : To change the current time.");
        System.out.println("B : To change the alarm time.");
        System.out.println("C : To set/unset the alarm.");
        String userInput = in.next();
        //Depending on user input we allow the user to change these values with input.
        switch (userInput)
        {
            case "A":
                System.out.println("You are changing the current time.");
                System.out.println("Please enter the new time in this format... hours/minutes/seconds");
                System.out.println("For example 05/23/40");
                //The user inputs the time in the format and it is converted to 'time' in the method, returned and set.
                currentTime = stringToTime(userInput = in.next());
                break;
            case "B":
                System.out.println("You are changing the alarm time.");
                System.out.println("Please enter the new time in this format... hours/minutes/seconds");
                System.out.println("For example 05/23/40");
                currentTime = stringToTime(userInput = in.next());
                break;
            case "C":
                System.out.println("You are setting the alarm on/off.");
                System.out.println("Please enter the new state as... 'on' or 'off'");
                userInput = in.next();
                if (userInput == "on")
                {
                    alarmActive = true;
                }
                else if (userInput == "off")
                {
                    alarmActive = true;
                }
                else
                {
                    System.out.println("Not a valid input.");
                }
                break;
            default:
                System.out.println("Not a valid input. Please try again with an option in the list.");
                break;
        }
    }

    //This method converts 'string' type to 'time' type and returns it.
    public time stringToTime(String stringTimeInput) {
        int hours;
        int minutes;
        int seconds;
        //Broken by the delimiter...
        String[] inputTimeArray = stringTimeInput.split("/");
        //The strings are parsed as ints, added to a time type and returned.
        hours = parseInt(inputTimeArray[0]);
        minutes = parseInt(inputTimeArray[1]);
        seconds = parseInt(inputTimeArray[2]);
        if (validateTime(hours, minutes, seconds))
        {
            time returnTime = new time(hours, minutes, seconds);
            System.out.println("The time is...");
            System.out.println(stringFormatTime(hours, minutes));
        }
        else
        {
            System.out.println("Error! Invalid time values... Time is now 0,0,0.");
            time returnTime = new time(0,0,0);
        }
        return returnTime;
    }

    public boolean validateTime(int hours, int minutes, int seconds)
    {
        if (0 >= hours && hours <= 23)
        {
            if (0 >= minutes && minutes <= 60)
            {
                if (0 >= seconds && seconds <= 60)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public String stringFormatTime(int hours, int minutes){
        return String.format("%02d:%02d", hours, minutes);
    }
 */
}