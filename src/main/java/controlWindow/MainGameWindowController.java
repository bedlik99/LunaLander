package controlWindow;

import ConfigClasses.LevelModelClasses.LevelModel1;
import DataModelJSON.JsonData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

/**
 * Klasa Controller, która będzie odpowiedzialna za obsługę zdarzeń głównego okna programu
 */
public class MainGameWindowController {

    /**
     * Zmienna opisująca pole gry - Graficzny kontener obiektów interfejsu użytkownika
     */
    @FXML
    private BorderPane gamePane;
    /**
     * Zmienna opisująca pasek zmiany paliwa
     */
    @FXML
    private ProgressBar amountOfFuel;

    /**
     * Zmienna zawierająca się w polu gry, będąca również pewnym kontenerem obiektów użytkownika.
     * Jest "dzieckiem" zmiennej gamePane
     */
    @FXML
    private HBox gamePaneHBox;

    /**
     * Wielokąt opisujący powierzchnię planety
     */
    private Polygon surfaceShapePolygon;
    /**
     * Wielokąt opisujący statek kosmiczny
     */
    private Polygon spaceCraftPolygon;
    /**
     * Obiekt będący modelem konkretnego poziomu (poziom1,poziom2,...)
     * Nie tworze instancji przypisuję gotową instancję klasy będącej atrybutem klasy JsonData.
     */
    private LevelModel1 levelModel1 = JsonData.getLevelModel1();

    public void initialize() {
        surfaceShapePolygon = new Polygon();
        spaceCraftPolygon = new Polygon();

        gamePaneHBox.getChildren().add(spaceCraftPolygon);
        gamePane.setBottom(surfaceShapePolygon);
        levelModel1.paint_example_level_surface(surfaceShapePolygon,spaceCraftPolygon);

        gamePane.widthProperty().addListener(new ChangeListener<Number>() {
            private double basicSurfaceWidth = getBasicWidth(surfaceShapePolygon);
            private double basicSpaceCraftWidth = getBasicWidth(spaceCraftPolygon);

            /**
             * Funkcja ChangeListener'a, polega ona "słuchaniu" zmian wartości, we własnościach wcześniej zaznaczonych
             * Tutaj jest tym gamePane.widthProperty() czyli szerokość obszaru gry.
             *
             * Funkcja ma za zadanie odpowiednie skalowanie i przesuwanie elemementów gry: statku oraz powierzchni planety
             * jeżeli zmieni się szerokość obszaru z grą.
             *
             * <p> Koncepcja skalowania i przesuwania została stworzona. Wykorzystuje fakt ze skalowanie odbywa się:
             * "od" środka obiektu na obie strony i w danej osi - tutaj X (szerokość).  *.setScale() </p>
             *
             * <p>Po odpowiednim przeskalowaniu nalezy przesunąć obiekt o odpowiednią wartość żeby nie znikał z okna,
             *  ale tylko utrzymał swoją pozycję - w innej skali </p>
             * @param observableValue Szerokość pola gry
             * @param oldValue Stara szerokość
             * @param newValue Nowa Szerokosć
             */
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
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

            /**
             * Metoda patrzy na zmiany wysokości "okna" z grą.
             * Koncepcja taka sama jak w przypadku szerokości. Patrz gamePane.widthProperty()...
             * @param observableValue Wysokość okna
             * @param oldValue Stara szerokość
             * @param newValue Nowa Szerokość
             */
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
                    spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight * ratioMoreThan1 / 2 - basicSpaceCraftHeight / 2));
                }

            }
        });


    }

    /**
     * Funkcja wyliczająca podstawową szerokość wielokąta - powierzchni planety, statku kosmicznego
     * Wartość zwraca potrzebna do przeskalowania konkretnego elementu
     * @param polygon Wielokąt opisujący pole powierzchni/ statek kosmiczny
     * @return Wartość podstawowej szerokości
     */
    private double getBasicWidth(Polygon polygon){
         int points = polygon.getPoints().size();
       return  (polygon.getPoints().get(points - 2));
    }




}
