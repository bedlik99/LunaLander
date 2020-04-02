package controlWindow;

import ConfigClasses.Level;
import ConfigClasses.ModelClasses.LevelModel;
import DataModelJSON.JsonData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

public class MainGameWindowController {

    @FXML
    private BorderPane gamePane;

    @FXML
    private ProgressBar progressBar;

    private Polygon surfaceShapePolygon = new Polygon();

    private LevelModel levelModel = JsonData.getLevelModel();

    public void initialize(){

        gamePane.setBottom(surfaceShapePolygon);
        levelModel.create_example_level_surface(surfaceShapePolygon);
        int pointsNumber = surfaceShapePolygon.getPoints().size();
        double basicPolygonWidth = surfaceShapePolygon.getPoints().get(pointsNumber-2);



        gamePane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

            if(basicPolygonWidth < (double)newValue){
               // System.out.println("Rozszerzam");
                double ratioMoreThan1 =  (double)newValue / basicPolygonWidth;
               surfaceShapePolygon.setScaleX(ratioMoreThan1);
               surfaceShapePolygon.setTranslateX(basicPolygonWidth*ratioMoreThan1/2-basicPolygonWidth/2);

            }else {
                double ratioLessThan1 = (double)newValue / basicPolygonWidth;
                surfaceShapePolygon.setScaleX(ratioLessThan1);
                surfaceShapePolygon.setTranslateX((basicPolygonWidth/2 - (basicPolygonWidth/2)*ratioLessThan1)*(-1));
            }

            }
        });


        gamePane.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double basicGamePaneHeight = 670.0;
                double basicPolygonHeight = 205.0;

                if( basicGamePaneHeight < (double)newValue){
                    // System.out.println("Zwiekszam okno");
                    double ratioLessThan1 = (double)newValue/basicGamePaneHeight;
                    surfaceShapePolygon.setScaleY(ratioLessThan1);
                    surfaceShapePolygon.setTranslateY((basicPolygonHeight/2 -(basicPolygonHeight/2)*ratioLessThan1));
                }else {
                    //  System.out.println("Zmniejszam okno");
                    double ratioMoreThan1 = (double)newValue/basicGamePaneHeight;
                    surfaceShapePolygon.setScaleY(ratioMoreThan1);
                    surfaceShapePolygon.setTranslateY((basicPolygonHeight*ratioMoreThan1/2-basicPolygonHeight/2)*(-1));
                }

            }
        });



    }



}
