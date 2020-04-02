package controlWindow;

import ConfigClasses.ModelClasses.LevelModel;
import DataModelJSON.JsonData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

public class MainGameWindowController {

    @FXML
    private BorderPane gamePane;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private HBox gamePaneHBox;

    private Polygon surfaceShapePolygon;
    private Polygon spaceCraftPolygon;
    private LevelModel levelModel = JsonData.getLevelModel();

    public void initialize() {
        surfaceShapePolygon = new Polygon();
        spaceCraftPolygon = new Polygon();

        gamePaneHBox.getChildren().add(spaceCraftPolygon);
        gamePane.setBottom(surfaceShapePolygon);
        levelModel.paint_example_level_surface(surfaceShapePolygon,spaceCraftPolygon);

        gamePane.widthProperty().addListener(new ChangeListener<Number>() {
            private double basicSurfaceWidth = getBasicWidth(surfaceShapePolygon);
            private double basicSpaceCraftWidth = getBasicWidth(spaceCraftPolygon);

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                System.out.println(gamePane.getWidth());
                if (basicSurfaceWidth < (double) newValue) {
                    // System.out.println("Rozszerzam");
                    double ratioMoreThan1 = (double) newValue / basicSurfaceWidth;
                    surfaceShapePolygon.setScaleX(ratioMoreThan1);
                    surfaceShapePolygon.setTranslateX(basicSurfaceWidth * ratioMoreThan1 / 2 - basicSurfaceWidth / 2);

                    spaceCraftPolygon.setScaleX(ratioMoreThan1);
                    spaceCraftPolygon.setTranslateX(basicSpaceCraftWidth * ratioMoreThan1 / 2 - basicSpaceCraftWidth / 2);

                } else {
                    // System.out.println("Zwezam");
                    double ratioLessThan1 = (double) newValue / basicSurfaceWidth;
                    surfaceShapePolygon.setScaleX(ratioLessThan1);
                    surfaceShapePolygon.setTranslateX((basicSurfaceWidth / 2 - (basicSurfaceWidth / 2) * ratioLessThan1) * (-1));

                    spaceCraftPolygon.setScaleX(ratioLessThan1);
                    spaceCraftPolygon.setTranslateX((basicSpaceCraftWidth / 2 - (basicSpaceCraftWidth / 2) * ratioLessThan1)*(-1));

                }

            }
        });

        gamePane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double basicGamePaneHeight = gamePane.getPrefHeight();
                double basicPolygonHeight = 205.0; // wartosc y_max
                double basicSpaceCraftHeight = 50.0;

                if (basicGamePaneHeight < (double) newValue) {
                    // System.out.println("Zwiekszam okno");
                    double ratioLessThan1 = (double) newValue / basicGamePaneHeight;
                    surfaceShapePolygon.setScaleY(ratioLessThan1);
                    surfaceShapePolygon.setTranslateY((basicPolygonHeight / 2 - (basicPolygonHeight / 2) * ratioLessThan1));

                    spaceCraftPolygon.setScaleY(ratioLessThan1);
                    spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight / 2 - (basicSpaceCraftHeight / 2) * ratioLessThan1)*(-1));
                } else {
                    //  System.out.println("Zmniejszam okno");
                    double ratioMoreThan1 = (double) newValue / basicGamePaneHeight;
                    surfaceShapePolygon.setScaleY(ratioMoreThan1);
                    surfaceShapePolygon.setTranslateY((basicPolygonHeight * ratioMoreThan1 / 2 - basicPolygonHeight / 2) * (-1));

                    spaceCraftPolygon.setScaleY(ratioMoreThan1);
                    spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight * ratioMoreThan1 / 2 - basicSpaceCraftHeight / 2) * (-1));
                }

            }
        });


    }

    private double getBasicWidth(Polygon polygon){
         int points = polygon.getPoints().size();
       return  (polygon.getPoints().get(points - 2));
    }




}
