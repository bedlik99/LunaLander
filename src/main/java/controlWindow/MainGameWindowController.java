package controlWindow;

import ConfigClasses.Level;
import ConfigClasses.ModelClasses.LevelModel;
import DataModelJSON.JsonData;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polygon;

public class MainGameWindowController {

    @FXML
    private BorderPane gamePane;

    @FXML
    private ProgressBar progressBar;

    private Polygon surfaceShapePolygon = new Polygon();

    private LevelModel levelModel = JsonData.getLevelModel();

    public void initialize(){

        ChangeListener<Number> paneSizeListener = (observable, oldValue, newValue) ->{
                System.out.println("Height: " + gamePane.getHeight() + " Width: " + gamePane.getWidth());
                // Wedlug mnie tutaj powinien byc kod bo:
                // Jezeli zostala wysluchana zmiana wysokosci/szerokosci Okna
                // to "ustaw" elementy (statek, pole do wyladowania) w odpowiednim miejscu
        };
             gamePane.widthProperty().addListener(paneSizeListener);
            gamePane.heightProperty().addListener(paneSizeListener);


            gamePane.setBottom(surfaceShapePolygon);
            levelModel.create_example_level_surface(surfaceShapePolygon);
            System.out.println(surfaceShapePolygon.getPoints());

    }


}
