package controlWindow;

import DataModelJSON.JsonData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import ConfigClasses.Config;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        Config config = JsonData.getConfig();
        Parent root = FXMLLoader.load(getClass().getResource("/MainGameWindow.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, config.getWindowWidth(), config.getWindowHeight()));
        primaryStage.show();


    }

    @Override
    public void init() throws Exception {

       JsonData.loadConfigFile();
/*
       Config config = JsonData.getConfig();
        Level level = config.getLevel();
        DifficultyLevel diffLevel = config.getLevel().getDifficultyLevel();

        // Checking if reading works:

        System.out.println("Window Title: " + config.getMainWindowTitle());
        System.out.println("Window Width: " + config.getWindowWidth());
        System.out.println("Window Height: " + config.getWindowHeight());
        System.out.println("Number of levels: " + config.getNumberOfLevels());
        System.out.println("Level names: ");
        for(int i =0; i<4;i++){
            System.out.println( level.getLevelName().get(i));
        }

        System.out.println("Levels File names: ");
        for(int i =0; i<4;i++){
            System.out.println( level.getLevelFileJson().get(i));
        }

        System.out.println("Basic points for difficulty lvl: easy -> medium -> hard");
        for(int i =0; i<3;i++){
            System.out.println( diffLevel.getBasicPointsForLevel().get(i));
        }

        System.out.println("Gravity Force for difficulty level: easy -> medium -> hard");
        for(int i =0; i<3;i++){
            System.out.println( diffLevel.getGravityForce().get(i));
        }

        System.out.println("Health points for difficulty level: easy -> medium -> hard");
        for(int i =0; i<3;i++){
            System.out.println( diffLevel.getHealthPoint().get(i));
        }

        System.out.println("Amount of fuel: for difficulty level: easy -> medium -> hard");
        for(int i =0; i<3;i++){
            System.out.println( diffLevel.getAmountOfFuel().get(i));
        }
*/


    }

    @Override
    public void stop() throws Exception {
        super.stop();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
