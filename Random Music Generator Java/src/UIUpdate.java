import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class UIUpdate extends Thread {

    public static void main(String args[]){
        Thread updateUI = new Thread(uiUpdate);
        //UIUpdate thread = new UIUpdate();
        updateUI.start();
        System.out.println("THREAD PLAYING");
    }

    //This is an infinite thread called a 'Task' to simulate graphical motion by updating the icon group's position and rotation.
    static Task<String> uiUpdate = new Task<>() {
        @Override
        protected String call() throws Exception {
            //The timeline that controls thread duration for our 'animation'.
            Timeline timeline = new Timeline(
                    //We define the thing that should happen, (the 'event')...
                    new KeyFrame(
                            //for how long.
                            Duration.seconds(0.05),
                            (event ->
                            {
                                //Controller.songNameTxt.setText(MusicManager.currentSongName);
                                //Controller.progressBar.setProgress(MusicManager.getSongProgress());
                            }
                            )
                    )
            );
            //This event will continue forever as desired.
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            return null;
        }
    };
}