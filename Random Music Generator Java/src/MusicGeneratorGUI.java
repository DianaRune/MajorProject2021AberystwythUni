import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class MusicGeneratorGUI extends Application { // implements ActionListener

    public PerspectiveCamera theCamera;

    //You don't put this shit here.
    //@FXML private Sphere icon;
    //@FXML private Sphere icon1;
    //@FXML private Sphere icon2;
    //@FXML private Sphere icon3;
    //@FXML private Sphere icon4;
    //@FXML private Sphere icon5;

    static Color[] hexColors = {Color.web("#a1ffd5"), Color.web("#a1ffeb"), Color.valueOf("#a1fffc"), Color.valueOf("#a1ecff"), Color.valueOf("#a1d5ff"), Color.valueOf("#a1c0ff"), Color.valueOf("#a1acff"), Color.valueOf("#b1a1ff"), Color.valueOf("#c3a1ff"), Color.valueOf("#d6a1ff"), Color.valueOf("#f2a1ff"), Color.valueOf("#ffa1f2")};
    Image[] illuminationMaps = {new Image(getClass().getResourceAsStream("Sparkle1.jpg")), new Image(getClass().getResourceAsStream("Sparkle2.jpg")), new Image(getClass().getResourceAsStream("Sparkle3.jpg"))};
    static PhongMaterial phongMaterial = new PhongMaterial();

    static boolean reverse;// = false;
    static int currentColor;// = 0;
    static int currentMap;
    static int angle = 0;
    static int totalAngle = 0;
    static boolean upwardsRotation = false;
    static int yTranslateAmount;
    static int DEFAULTKEYID = 100;

    private void initialize(){

    }

    @Override
    public void start(Stage mainStage) throws IOException {
        Key.intialiseKeys();
        MusicManager.getKey(DEFAULTKEYID);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Main main = loader.getController();

        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setTitle("APPLICATION_NAME");
        mainStage.show();

        //What   do these do? Might be USEFUL.
        scene.setCamera(theCamera);
        //scene.setCursor(0);

        Group iconGroup = new Group();
        iconGroup.getChildren().add(main.icon);
        iconGroup.getChildren().add(main.icon1);
        iconGroup.getChildren().add(main.icon2);
        iconGroup.getChildren().add(main.icon3);
        iconGroup.getChildren().add(main.icon4);
        iconGroup.getChildren().add(main.icon5);
        main.panelRoot.getChildren().add(iconGroup);

        Group iconFace = new Group();
        iconFace.getChildren().add(main.icon1);
        iconFace.getChildren().add(main.icon2);
        iconFace.getChildren().add(main.icon3);
        iconFace.getChildren().add(main.icon4);
        iconFace.getChildren().add(main.icon5);
        main.panelRoot.getChildren().add(iconFace);

        Task<String> iconColour = new Task<String>() {
            @Override
            protected String call() throws Exception {
                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.seconds(0.3), //0.5
                                (event ->
                                {
                                    phongMaterial.setDiffuseColor(hexColors[currentColor]);
                                    phongMaterial.setSelfIlluminationMap(illuminationMaps[currentMap]);
                                    //Color.web();
                                    main.icon.setMaterial(phongMaterial);
                                    currentMap++;
                                    if (currentMap >= illuminationMaps.length) {
                                        currentMap = 0;
                                    }
                                    //System.out.println(phongMaterial);
                                    //PhongMaterial[diffuseColor=0xa1ffebff, specularColor=null, specularPower=32.0, diffuseMap=null, specularMap=null, bumpMap=null, selfIlluminationMap=null]
                                    if (currentColor >= (hexColors.length - 1)) {
                                        reverse = true;
                                    } else if (currentColor == 0) {
                                        reverse = false;
                                    }
                                    if (reverse) {
                                        currentColor--;
                                    } else {
                                        currentColor++;
                                    }
                                }
                                )
                        )
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

                if (currentColor >= (hexColors.length - 1)) {
                    reverse = true;
                } else if (currentColor == 0) {
                    reverse = false;
                }
                if (reverse) {
                    currentColor--;
                } else {
                    currentColor++;
                }
                return null;
            }
        };
        new Thread(iconColour).start();

        Task<String> iconMotion = new Task<String>() {
            @Override
            protected String call() throws Exception {
                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.seconds(0.05),
                                (event ->
                                {
                                    Rotate rotate = new Rotate(angle, new Point3D(1, 0, 0));
                                    rotate.setPivotX(150);
                                    rotate.setPivotY(225);
                                    iconGroup.getTransforms().add(rotate);
                                    iconFace.getTransforms().add(rotate);
                                    totalAngle = totalAngle + angle;
                                    System.out.println(totalAngle);

                                    Translate translate = new Translate();
                                    translate.setY(yTranslateAmount);
                                    iconFace.getTransforms().add(translate);

                                    //Translate Just face....

                                    if (upwardsRotation == true)
                                    {
                                        angle = 2;
                                        //amount?
                                        yTranslateAmount = 5;
                                    }
                                    else
                                    {
                                        angle = -2;
                                        yTranslateAmount = -5;
                                    }
                                    if (totalAngle >= 20) {
                                        upwardsRotation = false;
                                    } else if (totalAngle <= -20) {
                                        System.out.println("UMMM");
                                        upwardsRotation = true;
                                    }
                                }
                                )
                        )
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

                ///////////////////////////
                return null;
            }
        };
        new Thread(iconMotion).start();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public void tempoGrow() {//Event arg0) {
        //for (int i = 0; i < 500; i++) {
        //PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        //phongMaterial.setDiffuseColor(Color.web(hexColors[currentColor]));
        //pauseTransition.setOnFinished(event ->
        //{
        //icon.setMaterial(phongMaterial);
        //pauseTransition.play();
        //}
        //);
    }
}







//






/*
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class MusicGeneratorGUI extends Application {//Canvas { // implements ActionListener

    private JPanel mainPanel;

    public void paint(Graphics g)//, Stage mainStage)
    {
        //super.paintComponents(g);

        g.setColor(Color.CYAN);
        g.fillOval(100,100, 200, 200);
        //g.fillRect(x, 30, 50, 30);


    }

    //public void actionPerformed(ActionEvent actionEvent)
    //{
        //x = x + velX;
        //repaint();
    //}

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("APPLICATION_NAME");
        //MusicGeneratorGUI guiCanvas = new MusicGeneratorGUI();
        mainFrame.setSize(500,500);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.add(guiCanvas);
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        Sphere sphere = new Sphere(100);
        Group group = new Group();
        group.getChildren().add(sphere);

        Scene scene = new Scene(group, 200, 200);

        //W T H   do these do? Might be USEFUL.
        //scene.setCamera(camera);
        //scene.setCursor(0);

        mainStage.setScene(scene);
        mainStage.setTitle("MEOW");
        mainStage.show();
    }
}

 */