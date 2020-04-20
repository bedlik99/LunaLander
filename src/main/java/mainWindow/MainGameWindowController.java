package mainWindow;

import ConfigClasses.LevelModelClass.LevelModel;
import DataModelJSON.JsonData;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import mainWindow.Controllers.StartGameWindowController;

import java.io.IOException;
import java.util.Optional;

/**
 * Klasa Controller, która będzie odpowiedzialna za obsługę zdarzeń głównego okna programu
 */
public class MainGameWindowController {

    @FXML
    private Button startGameButton;
    /**
     * 'Pane' głównego okna gry
     */
    @FXML
    private BorderPane mainWindowPane;
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
    private Polygon surfaceShapePolygon = new Polygon();

    /**
     * Wielokąt opisujący statek kosmiczny
     */
    private Polygon spaceCraftPolygon = new Polygon();

    /**
     * Obiekt klasy opisującej model poziomu 1 będący modelem konkretnego poziomu (poziom1,poziom2,...)
     * Nie tworze instancji, przypisuję gotową instancję klasy będącej atrybutem klasy JsonData.
     */
    private final LevelModel levelModel1 = JsonData.getLevelModel1();
    private final LevelModel levelModel2 = JsonData.getLevelModel2();
    private final LevelModel levelModel3 = JsonData.getLevelModel3();

    private static double rate = 1;
    /**
     * Zmienna obiektowa klasy pozwalajacej na tworzenie animacji
     */
    private final TranslateTransition gravitationTransition = new TranslateTransition();
    private final TranslateTransition playerTransition = new TranslateTransition();
    private boolean isSreleased;

    public void initialize() {


        spaceCraftPolygon.translateYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldN, Number newN) {

            }
        });

        mainWindowPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode().equals(KeyCode.S)) {
                    isSreleased = true;
                   rate =1;
                   gravitationTransition.setRate(rate);


                }

            }
        });

        mainWindowPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode().equals(KeyCode.P)) {
                    if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                        System.out.println("Kliknales: " + keyEvent.getCode() + " -> P_auza gry ;)");
                        gravitationTransition.pause();
                    }
                }
                if (keyEvent.getCode().equals(KeyCode.O)) {
                    if (gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                        System.out.println("Kliknales: " + keyEvent.getCode() + " -> O_dpazuzowujesz! ;)");
                        gravitationTransition.play();
                    }
                }

                if (keyEvent.getCode().equals(KeyCode.S)) {
                    isSreleased = false;
                    accelerateUP();
                }else{
                    isSreleased = true;
                }

                if (keyEvent.getCode().equals(KeyCode.A)) {
                    accelerateToRight();

                }
                if (keyEvent.getCode().equals(KeyCode.D)) {
                    accelerateToLeft();
                }

            }
        });


        gamePane.widthProperty().addListener(new ChangeListener<Number>() {
            private final double basicSurfaceWidth = levelModel1.getBasicSurfaceWidth();
            private final double basicSpaceCraftWidth = levelModel1.getBasicSpaceCraftWidth();

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
                    spaceCraftPolygon.setTranslateX((basicSpaceCraftWidth / 2 - (basicSpaceCraftWidth / 2) * ratioLessThan1) * (-1));

                }

            }
        });


        gamePane.heightProperty().addListener(new ChangeListener<Number>() {
            private final double basicGamePaneHeight = gamePane.getPrefHeight();
            private final double basicPolygonHeight = levelModel1.getBasicSurfaceHeight();
            private final double basicSpaceCraftHeight = levelModel1.getBasicSpaceCraftHeight();

            /**
             * Metoda patrzy na zmiany wysokości "okna" z grą.
             * Koncepcja taka sama jak w przypadku szerokości. Patrz gamePane.widthProperty()...
             * @param observableValue Wysokość okna
             * @param oldValue Stara szerokość
             * @param newValue Nowa Szerokość
             */
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

                if (basicGamePaneHeight < (double) newValue) {
                    // System.out.println("Zwiekszam okno");
                    // System.out.println(gamePane.getHeight()); 671px
                    double ratioMoreThan1 = (double) newValue / basicGamePaneHeight;
                    surfaceShapePolygon.setScaleY(ratioMoreThan1);
                    surfaceShapePolygon.setTranslateY(((basicPolygonHeight + 3) / 2 - (basicPolygonHeight / 2) * ratioMoreThan1));

                    spaceCraftPolygon.setScaleY(ratioMoreThan1);
                    spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight / 2 - (basicSpaceCraftHeight / 2) * ratioMoreThan1) * (-1));

                    if (gravitationTransition != null) {
                        gravitationTransition.stop();
                        gravitationTransition.setToY(512 * ratioMoreThan1 + (50.0 * ratioMoreThan1 - 50.0));
                        gravitationTransition.play();
                    }
                } else {

                    //  System.out.println("Zmniejszam okno");
                    double ratioLessThan1 = (double) newValue / basicGamePaneHeight;
                    surfaceShapePolygon.setScaleY(ratioLessThan1);
                    surfaceShapePolygon.setTranslateY((basicPolygonHeight * ratioLessThan1 / 2 - basicPolygonHeight / 2) * (-1));

                    spaceCraftPolygon.setScaleY(ratioLessThan1);
                    spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight * ratioLessThan1 / 2 - basicSpaceCraftHeight / 2));

                    // .setRate() - pozwala na zmniejszenie predkosci dzialania animacji - gdy zmniejszamy okno predkosc musi sie skalowac
                    if (gravitationTransition != null) {
                        gravitationTransition.stop();
                        gravitationTransition.setToY(512 * ratioLessThan1 - (25.0 - 25.0 * ratioLessThan1));
                        //  System.out.println(gravitationTransition.getToY());
                        gravitationTransition.play();
                    }


                }
            }
        });


    }


    public void animateGravitation(Polygon spaceCraft) {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {

            gravitationTransition.setDelay(Duration.seconds(0.5));
            gravitationTransition.setDuration(Duration.seconds(6)); // w jakim czasie ma opasc statek - zmienna konfiguracyjna (grawitacja)
            gravitationTransition.setToY(512); // Na do jakiego miejsca ma spasc - dopoki nie uderzy w ziemie! - Piękna funkcja!
            gravitationTransition.setCycleCount(Animation.INDEFINITE);  // musi zostac zastopowana
            gravitationTransition.setNode(spaceCraft);
            gravitationTransition.play();
        }
    }


    public void accelerateToRight() {
        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            playerTransition.setDuration(Duration.seconds(0.02));
            playerTransition.setToX(spaceCraftPolygon.translateXProperty().getValue() + 2.2);
            playerTransition.setNode(spaceCraftPolygon);
            playerTransition.play();
        }
    }


    public void accelerateToLeft() {
        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            playerTransition.setDuration(Duration.seconds(0.02));
            playerTransition.setToX(spaceCraftPolygon.translateXProperty().getValue() - 2.2);
            playerTransition.setNode(spaceCraftPolygon);
            playerTransition.play();
        }
    }

    public void accelerateUP() {
        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                if (rate > 0) {
                    rate = rate - 0.03;
                    gravitationTransition.setRate(rate);
                }else if(rate <= 0 && rate > -0.15){
                        rate = rate - 0.01;
                        gravitationTransition.setRate(rate);
                }else if( rate > -0.8){    // TEGO TRZEBA WYSKALOWAC
                    rate = rate - 0.05;
                    gravitationTransition.setRate(rate);
                }

            }


        }
    }


    /**
     * Funkcja wczytująca wybrany level
     *
     * @param levelNumber Numer poziomu do wczytania
     */
    public void loadLevel(int levelNumber) {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            this.gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
            this.gamePane.getChildren().removeAll(surfaceShapePolygon);
        }
        this.surfaceShapePolygon = new Polygon();
        this.spaceCraftPolygon = new Polygon();
        this.gamePaneHBox.getChildren().add(spaceCraftPolygon);
        this.gamePane.setBottom(surfaceShapePolygon);

        if (levelNumber == 1) {
            this.levelModel1.paintLevel(surfaceShapePolygon, spaceCraftPolygon);
        } else if (levelNumber == 2) {
            this.levelModel2.paintLevel(surfaceShapePolygon, spaceCraftPolygon);
        } else if (levelNumber == 3) {
            this.levelModel3.paintLevel(surfaceShapePolygon, spaceCraftPolygon);
        }

        spaceCraftPolygon.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {

            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {
                Shape intersect = Shape.intersect(spaceCraftPolygon, surfaceShapePolygon);
                if (intersect.getBoundsInLocal().getWidth() > -1) {
                    //  System.out.println("Kolizja");
                    //  System.out.println("y: "+Math.floor(spaceCraftPolygon.translateYProperty().getValue()));
                    //   System.out.println("x: "+Math.floor(spaceCraftPolygon.translateXProperty().getValue()));
                    // kod gdy sie stykna - ale jeszcze nie rozrozniam gdy stykna sie w dobrym miejscu i gdy w zlym
                } else {
//                    System.out.println("y: "+Math.floor(spaceCraftPolygon.translateYProperty().getValue()));
//                    System.out.println("x: "+Math.floor(spaceCraftPolygon.translateXProperty().getValue()));
                    // System.out.println("Brak kolizji");
                }
            }
        });





        animateGravitation(spaceCraftPolygon);

    }


    @FXML
    public void showStartGameWindow() {
        if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
            gravitationTransition.pause();
        }

        StartGameWindowController controller = new StartGameWindowController();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        dialog.setTitle("Typ gry");
        dialog.setHeaderText("Uzyj tego okna do wyboru gry offline/online");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Windows_fxml/StartGameWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        controller = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            controller.processResults();
            if (controller.getOfflineCheckBox().isSelected()) {
                gravitationTransition.stop();
                loadLevel(1);
            } else if (!controller.getAdressIP().getText().trim().isEmpty() && !controller.getPort().getText().trim().isEmpty()) {
                gravitationTransition.stop();
                loadLevel(1);
            }

        }

    }


    @FXML
    public void showOptionWindow() {
        if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
            gravitationTransition.pause();
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        dialog.setTitle("Opcje");
        dialog.setHeaderText("Instrukcja && Zasady gry");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Windows_fxml/RulesWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

        }
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
    }
}
