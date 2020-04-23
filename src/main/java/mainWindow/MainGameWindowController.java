package mainWindow;

import ConfigClasses.Level;
import ConfigClasses.LevelModelClass.LevelModel;
import ConfigClasses.Player;
import ConfigClasses.StopWatch;
import DataModelJSON.Data;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import mainWindow.Controllers.SaveResultsWindowController;
import mainWindow.Controllers.StartGameWindowController;

import java.io.IOException;
import java.util.Optional;

/**
 * Klasa Controller, która będzie odpowiedzialna za obsługę zdarzeń głównego okna programu
 */
public class MainGameWindowController {

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
    private ProgressBar barOfFuel;

    /**
     * Zmienna zawierająca się w polu gry, będąca również pewnym kontenerem obiektów użytkownika.
     * Jest "dzieckiem" zmiennej gamePane
     */
    @FXML
    private HBox gamePaneHBox;

    @FXML
    private Label speedAcceptance;

    @FXML
    private Label durationOfPlay;

    @FXML
    private Label fuelToUse;

    @FXML
    private Label healthPoints;
    /**
     * Wielokąt opisujący powierzchnię planety
     */
    private final Polygon surfaceShapePolygon = new Polygon();

    /**
     * Wielokąt opisujący statek kosmiczny
     */
    private Polygon spaceCraftPolygon = new Polygon();

    /**
     * Obiekt klasy opisującej model poziomu 1 będący modelem konkretnego poziomu (poziom1,poziom2,...)
     * Nie tworze instancji, przypisuję gotową instancję klasy będącej atrybutem klasy JsonData.
     */
    private final LevelModel levelModel1 = Data.getLevelModel1();
    private final LevelModel levelModel2 = Data.getLevelModel2();
    private final LevelModel levelModel3 = Data.getLevelModel3();
    private final Level levelsDetails = Data.getConfig().getLevel();

    private static double rate = 1;
    /**
     * Zmienna obiektowa klasy pozwalajacej na tworzenie animacji
     */
    private final TranslateTransition gravitationTransition = new TranslateTransition();
    private final TranslateTransition playerTransition = new TranslateTransition();

    private StopWatch stopWatch;

    private Shape intersectWithSurface;
    private boolean is_S_released;
    private double locationForGravitation;

    private double widthRatio;


    private double basicGamePaneHeight;
    private double basicPolygonHeight;
    private double basicSpaceCraftHeight;
    private double basicSurfaceWidth;
    private double basicSpaceCraftWidth;

    private double newWidthValue;
    private double newHeightValue;
    private boolean isDestroyed = false;

    private static int levelToLoad = 1;
    private static double fuel = 1;
    private static int healthP;
    private static int difficultyId;
    private static boolean pauseWasMade = false;
    private static boolean leftPlanet;
    private Player player;


    public void initialize() {
        basicGamePaneHeight = gamePane.getPrefHeight();
        basicPolygonHeight = levelModel1.getBasicSurfaceHeight();
        basicSpaceCraftHeight = levelModel1.getBasicSpaceCraftHeight();
        basicSurfaceWidth = levelModel1.getBasicSurfaceWidth();
        basicSpaceCraftWidth = levelModel1.getBasicSpaceCraftWidth();
        barOfFuel.setProgress(fuel);

        gamePaneHBox.getChildren().add(spaceCraftPolygon);
        gamePane.setBottom(surfaceShapePolygon);


        barOfFuel.progressProperty().addListener(new ChangeListener<Number>() {
            private double actualFuel;

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldF, Number newF) {
                actualFuel = Math.abs((double) Math.round((double) newF * 100.0) / 100.0);

                if (actualFuel == 0.00) {
                    fuelToUse.setText("BRAK PALIWA!");
                    fuelToUse.setTextFill(Paint.valueOf("RED"));
                }
            }
        });

        gravitationTransition.rateProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldR, Number newR) {
                if ((double) newR <= 0.5) {
                    speedAcceptance.setText("DOBRA");
                    speedAcceptance.setTextFill(Paint.valueOf("GREEN"));
                } else {
                    speedAcceptance.setText("ZLA");
                    speedAcceptance.setTextFill(Paint.valueOf("RED"));
                }
            }
        });


        mainWindowPane.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {


                if (pauseWasMade && (keyEvent.getCode().equals(KeyCode.S) || keyEvent.getCode().equals(KeyCode.D) || keyEvent.getCode().equals(KeyCode.A))) {
                    // nie dotykac - naprawa bugu

                } else {

                    if (keyEvent.getCode().equals(KeyCode.S) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                        locationForGravitation = spaceCraftPolygon.translateYProperty().getValue();
                        is_S_released = true;
                        rate = 0.5;
                        gravitationTransition.setRate(rate);

                        spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftImage()));
                    }

                    if (keyEvent.getCode().equals(KeyCode.A) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                        spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftImage()));
                    }


                    if (keyEvent.getCode().equals(KeyCode.D) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                        spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftImage()));
                    }
                }
            }

        });


        mainWindowPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (pauseWasMade && (keyEvent.getCode().equals(KeyCode.S) || keyEvent.getCode().equals(KeyCode.D) || keyEvent.getCode().equals(KeyCode.A))) {
                    // nie dotykac - naprawa bugu
                } else {

                    if (keyEvent.getCode().equals(KeyCode.P) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                        if (!pauseWasMade) {
                            pauseWasMade = true;
                        }

                        System.out.println("Kliknales: " + keyEvent.getCode() + " -> P_auza gry ;)");
                        gravitationTransition.pause();

                        spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftImage()));

                    }


                    if (keyEvent.getCode().equals(KeyCode.O) && gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                        if (pauseWasMade) {
                            pauseWasMade = false;
                        }

                        is_S_released = true;
                        System.out.println("Kliknales: " + keyEvent.getCode() + " -> O_dpazuzowujesz! ;)");
                        gravitationTransition.play();
                        rate = 0.5;
                        gravitationTransition.setRate(rate);

                    }


                    if (keyEvent.getCode().equals(KeyCode.S) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {

                            is_S_released = false;
                            if (!isDestroyed && fuel > 0 && gravitationTransition.getStatus() != Animation.Status.PAUSED) {
                                accelerateUP();
                                spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftDownFireImage()));
                            }


                    }

                    if (keyEvent.getCode().equals(KeyCode.A) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {

                            if (!isDestroyed && fuel > 0 && gravitationTransition.getStatus() != Animation.Status.PAUSED) {
                                accelerateToRight();
                                spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftLeftFireImage()));
                            }


                    }

                    if (keyEvent.getCode().equals(KeyCode.D) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {

                            if (!isDestroyed && fuel > 0 && gravitationTransition.getStatus() != Animation.Status.PAUSED) {
                                accelerateToLeft();
                                spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftRightFireImage()));
                            }

                    }


                }


            }
        });


        gamePane.widthProperty().addListener(new ChangeListener<Number>() {

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
                newWidthValue = (double) newValue;
                widthGamePaneScaling((double) newValue);

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
                newHeightValue = (double) newValue;
                heightGamePaneScaling((double) newValue);
            }
        });


    } //initialize method


    public void widthGamePaneScaling(Double newValue) {

        if (basicSurfaceWidth < (double) newValue) {
            // System.out.println("Rozszerzam");
            double ratioMoreThan1 = (double) newValue / basicSurfaceWidth;
            widthRatio = ratioMoreThan1;
            surfaceShapePolygon.setScaleX(ratioMoreThan1);
            surfaceShapePolygon.setTranslateX(basicSurfaceWidth * ratioMoreThan1 / 2 - basicSurfaceWidth / 2);

            spaceCraftPolygon.setScaleX(ratioMoreThan1);
            spaceCraftPolygon.setTranslateX(basicSpaceCraftWidth * ratioMoreThan1 / 2 - basicSpaceCraftWidth / 2);

            playerTransition.setRate(ratioMoreThan1);

        } else {
            // System.out.println("Zwezam");
            double ratioLessThan1 = (double) newValue / basicSurfaceWidth;
            widthRatio = ratioLessThan1;
            surfaceShapePolygon.setScaleX(ratioLessThan1);
            surfaceShapePolygon.setTranslateX((basicSurfaceWidth / 2 - (basicSurfaceWidth / 2) * ratioLessThan1) * (-1));

            spaceCraftPolygon.setScaleX(ratioLessThan1);
            spaceCraftPolygon.setTranslateX((basicSpaceCraftWidth / 2 - (basicSpaceCraftWidth / 2) * ratioLessThan1) * (-1));

            playerTransition.setRate(ratioLessThan1);

        }

    }


    public void heightGamePaneScaling(Double newValue) {
        if (basicGamePaneHeight < (double) newValue) {
            // System.out.println("Zwiekszam okno");
            // System.out.println(gamePane.getHeight()); 671px
            double ratioMoreThan1 = (double) newValue / basicGamePaneHeight;
            surfaceShapePolygon.setScaleY(ratioMoreThan1);
            surfaceShapePolygon.setTranslateY(((basicPolygonHeight + 3) / 2 - (basicPolygonHeight / 2) * ratioMoreThan1));

            spaceCraftPolygon.setScaleY(ratioMoreThan1);
            spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight / 2 - (basicSpaceCraftHeight / 2) * ratioMoreThan1) * (-1));

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING || gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                gravitationTransition.stop();
                gravitationTransition.setToY(515 * ratioMoreThan1 + (50.0 * ratioMoreThan1 - 50.0));
                gravitationTransition.play();
            }

        } else {

            //  System.out.println("Zmniejszam okno");
            double ratioLessThan1 = (double) newValue / basicGamePaneHeight;
            surfaceShapePolygon.setScaleY(ratioLessThan1);
            surfaceShapePolygon.setTranslateY((basicPolygonHeight * ratioLessThan1 / 2 - basicPolygonHeight / 2) * (-1));

            spaceCraftPolygon.setScaleY(ratioLessThan1);
            spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight * ratioLessThan1 / 2 - basicSpaceCraftHeight / 2));

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING || gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                gravitationTransition.stop();
                gravitationTransition.setToY(515 * ratioLessThan1 - (25.0 - 25.0 * ratioLessThan1));
                gravitationTransition.play();
            }

        }

    }


    public void animateGravitation(Polygon spaceCraft) {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {

            gravitationTransition.setDelay(Duration.seconds(0.5));
            gravitationTransition.setDuration(Duration.seconds(6)); // w jakim czasie ma opasc statek - zmienna konfiguracyjna (grawitacja)
            gravitationTransition.setToY(515); // 512.8 Na do jakiego miejsca ma spasc - dopoki nie uderzy w ziemie! - Piękna funkcja!
            //gravitationTransition.setCycleCount(Animation.INDEFINITE);  // musi zostac zastopowana
            gravitationTransition.setNode(spaceCraft);
            gravitationTransition.play();

            gravitationTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (spaceCraft.getTranslateY() < 50) {
                        leftPlanet = true;
                        System.out.println("Wyleciales z planety!");
                        gravitationTransition.stop();

                        if (gamePaneHBox.getChildren().size() != 0) {
                            gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
                        }
                        spaceCraftPolygon = new Polygon();
                        gamePaneHBox.getChildren().add(spaceCraftPolygon);

                    }

                    if (gravitationTransition.getRate() <= 0.5 && !leftPlanet) {
                        System.out.println("Gratulacje, wylądowales dobrze!");

                        stopWatch.stop();
                        durationOfPlay.setText(stopWatch.toString());

                        double pointsForLevel = Data.getConfig().getLevel().getBasicPointsForLevel().get(difficultyId);
                        double pointsForFuelLeft = Math.ceil(fuel * 250);
                        double factorForTime = Math.floor(stopWatch.getLevelDurations().get(levelToLoad - 1) / 4); // nie da sie w 4 sekundy przejsc wiec nie ma problemu
                        double totalResult = Math.ceil((pointsForLevel + pointsForFuelLeft) / factorForTime);
                        player.setGamingResult(totalResult);
                        System.out.println("Punkt za:" + (levelToLoad) + " poziom: " + totalResult);


                        levelToLoad++;

                        if (levelToLoad <= 3 && !leftPlanet) {
                            loadLevel(levelToLoad);
                        }

                        if (levelToLoad == 4 ) {
                            System.out.println("Totalne punkty zdobyte: " + player.getGamingResult());
                            showSaveResultsWindow();
                        }

                    }else {
                        showSaveResultsWindow();
                    }

                }
            });

        }

    }

    public void useFuel() {
        if (fuel > 0) {
            Timeline timeline = new Timeline();
            fuel = fuel - 0.0025;
            KeyValue keyValue = new KeyValue(barOfFuel.progressProperty(), fuel);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.02), keyValue);
            timeline.getKeyFrames().add(keyFrame);

            timeline.play();
        }

    }


    public void accelerateToRight() {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            playerTransition.setDuration(Duration.seconds(0.02));
            playerTransition.setToX(spaceCraftPolygon.translateXProperty().getValue() + 2.2);
            playerTransition.setNode(spaceCraftPolygon);
            playerTransition.play();

            useFuel();
        }

    }


    public void accelerateToLeft() {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            playerTransition.setDuration(Duration.seconds(0.02));
            playerTransition.setToX(spaceCraftPolygon.translateXProperty().getValue() - 2.2);
            playerTransition.setNode(spaceCraftPolygon);
            playerTransition.play();

            useFuel();
        }

    }


    public void accelerateUP() {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                if (rate > 0) {
                    rate = rate - 0.03;
                    gravitationTransition.setRate(rate);
                    useFuel();
                } else if (rate <= 0 && rate > -0.15) {
                    rate = rate - 0.01;
                    gravitationTransition.setRate(rate);
                    useFuel();
                } else if (rate <= -0.15 && rate >= -1) {
                    rate = rate - 0.05;
                    gravitationTransition.setRate(rate);
                    useFuel();
                } else {
                    useFuel();
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

        if (gamePaneHBox.getChildren().size() != 0) {
            this.gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
        }

        this.spaceCraftPolygon = new Polygon();
        this.gamePaneHBox.getChildren().add(spaceCraftPolygon);

        if (levelNumber == 1) {
            this.levelModel1.paintLevel(surfaceShapePolygon, spaceCraftPolygon);
        } else if (levelNumber == 2) {
            this.levelModel2.paintLevel(surfaceShapePolygon, spaceCraftPolygon);
        } else if (levelNumber == 3) {
            this.levelModel3.paintLevel(surfaceShapePolygon, spaceCraftPolygon);
        }

        fuel = 1;
        barOfFuel.setProgress(fuel);


        spaceCraftPolygon.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            private double finish, predictedFinish, acceptableValue, finishSpeed;

            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {

                if (spaceCraftPolygon != null) {

                    intersectWithSurface = Shape.intersect(spaceCraftPolygon, surfaceShapePolygon);
                    finish = Math.floor(spaceCraftPolygon.translateYProperty().getValue());
                    predictedFinish = Math.floor(gravitationTransition.getToY());
                    acceptableValue = Math.abs(predictedFinish - finish);
                    finishSpeed = gravitationTransition.getRate();


                    if (intersectWithSurface.getBoundsInLocal().getWidth() > -1) {
                        //System.out.println("Kolizja");
                        if (acceptableValue >= 13.0 || finishSpeed > 0.5) {
                            //System.out.println("Rozbiles się! :(");
                            gravitationTransition.stop();

                            // animacja wybuchu !!!!
                            gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
                            spaceCraftPolygon = new Polygon();
                            gamePaneHBox.getChildren().add(spaceCraftPolygon);

                            // Odejmij zycie
                            healthP--;
                            healthPoints.setText("" + healthP);

                            //zaladuj level ponownie, jezeli gracz ma zycie
                            if (healthP != 0) {
                                loadLevel(levelToLoad);

                            } else {
                                System.out.println();
                                showSaveResultsWindow();
                                // Okienko z komunikatem o zapisanie wyniku gry i pokazaniem liczby zdobytych punktow
                            }

                        }

                    } else {
                        // Brak Kolizji - tutaj mozna wrzucic kod sprawdzajacy czy czlowiek "nie wylecial" statkiem poza plansze
                        // ale jest to raczej radzenie sobie z bugiem - nie przeszkadza w grze.

                    }


                }
            }
        });


        spaceCraftPolygon.translateYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldN, Number newN) {
                // System.out.println(newN + " : " + isSreleased + " : " + rate);

                // Algorytm przyspieszania statku
                //System.out.println("Aktualna wartosc Y:  "+(double)newN);
                if (is_S_released && rate != 1 && !pauseWasMade) {
                    double wantedYPosition = locationForGravitation + 40.0;
                    //  System.out.println("W momencie puszczenie + 20: " + wantedYPosition);
                    if ((double) newN >= wantedYPosition) {
                        rate = 1;
                        gravitationTransition.setRate(rate);

                    }
                }

            }
        });

        isDestroyed = false;
        animateGravitation(spaceCraftPolygon);
        rate = 1;
        gravitationTransition.setRate(rate);

        widthGamePaneScaling(newWidthValue);
        heightGamePaneScaling(newHeightValue);
        stopWatch.start();

    }


    public void showSaveResultsWindow() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        dialog.setTitle("Zapisz wynik");
        dialog.setHeaderText("Uzyj tego okna aby zapisac swoj wynik");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Windows_fxml/SaveResultsWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        fxmlLoader.getController();

        // MULTITHREADING !!!!!!! -> MUSOWO Z POWODU UZYCIA ANIMACJI
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SaveResultsWindowController controller = new SaveResultsWindowController();

                controller = fxmlLoader.getController();
                controller.setResult(player.getGamingResult());

                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    player.setNickName(controller.getNickNameField().getText().trim());

                    if (player.getNickName().isEmpty()) {
                        System.out.println("Nie podales nicku - wynik nie zapisany");
                    } else {
                        System.out.println("Nick: " + player.getNickName() + "\nWynik: " + player.getGamingResult());
                    }
                }







            }
        });


    }


    @FXML
    public void showStartGameWindow() {
        if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
            gravitationTransition.pause();

        }

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

        // MULTITHREADING - OKNO OTWIERAM NA INNYM WATKU
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                StartGameWindowController controller = new StartGameWindowController();
                controller = fxmlLoader.getController();
                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    leftPlanet = false;
                    healthPoints.setText("" + levelsDetails.getHealthPoint().get(controller.getDifficultyId()));
                    healthP = levelsDetails.getHealthPoint().get(controller.getDifficultyId());
                    difficultyId = controller.getDifficultyId();
                    player = new Player("",0);

                    levelToLoad = 1;
                    controller.processResults();
                    if (controller.getOfflineCheckBox().isSelected()) {

                        gravitationTransition.stop();
                        stopWatch = new StopWatch();
                        loadLevel(levelToLoad);

                    } else if (!controller.getAdressIP().getText().trim().isEmpty() && !controller.getPort().getText().trim().isEmpty()) {

                        gravitationTransition.stop();
                        stopWatch = new StopWatch();
                        loadLevel(levelToLoad);

                    }

                }

            }
        });


    }


    @FXML
    public void showTableOfFame() {
        if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
            gravitationTransition.pause();

        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        dialog.setTitle("Tablica wynikow");
        dialog.setHeaderText("Najwyzsze wyniki.");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Windows_fxml/TableOfFameWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    gravitationTransition.play();
                }

            }
        });


    }


    @FXML
    public void showRulesWindow() {
        if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
            gravitationTransition.pause();

        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        dialog.setTitle("Scenariusz|Zasady|Pomoc");
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
        // ODPALAMy OKNO NA INNYM WATKU - MULTITHREADING - DLACZEGO?
        // MOZE SIE ZDARZYC ZE KTOS ODPALI OKNO W TRAKCIE GRY (DZIALANIA ANIMACJI)

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    gravitationTransition.play();
                }
            }
        });


    }

    @FXML
    public void closeApplication() {
        Platform.exit();
    }
}
