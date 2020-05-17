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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import mainWindow.Controllers.SaveResultsWindowController;
import mainWindow.Controllers.StartGameWindowController;
import mainWindow.Controllers.TableOfFameWindowController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

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

    /**
     * Zmienna 'etykieta' zaierajaca notke o prawidlowej predkosci statku
     */
    @FXML
    private Label speedAcceptance;

    /**
     * Zmienna 'etykieta' zawierajaca czas gry
     */
    @FXML
    private Label durationOfPlay;

    /**
     * Zmienna 'etykieta' zawierajaca notke o paliwie do uzycia
     */
    @FXML
    private Label fuelToUse;

    /**
     * Zmienna 'etykieta' zawierajaca notke o zyciach gracza
     */
    @FXML
    private Label healthPoints;

    @FXML
    private Label labelPoints;

    /**
     * Zmienna 'przycisk' ktorego dzialanie pozwala na wyswietlenie okna do rozpoczecia nowej gry
     */
    @FXML
    private Button startButton;

    /**
     * Zmienna 'przycisk' ktorego dzialanie pozwala na wyswietlenie okna zasad gry
     */
    @FXML
    private Button rulesButton;

    /**
     * Zmienna 'przycisk' ktorego dzialanie pozwala na wyswietlenie okna z tablica wynikow
     */
    @FXML
    private Button resultsButton;

    /**
     * Zmienna 'kontener graficzny' przechowywujaca lewa granice gry
     */
    @FXML
    private VBox gamePaneLeftBorderBox;

    /**
     * Zmienna 'kontener graficzny' przechowywujaca prawa granice gry
     */
    @FXML
    private VBox gamePaneRightBorderBox;

    /**
     * Wielokąt opisujący powierzchnię planety
     */
    private final Polygon surfaceShapePolygon = new Polygon();

    /**
     * Wielokąt opisujący statek kosmiczny
     */
    private Polygon spaceCraftPolygon = new Polygon();

    /**
     * Wielokat opisujac ksztalt lewej granicy
     */
    private Polygon leftBorderPolygon = new Polygon();

    /**
     * Wielokat opisujac ksztalt prawej granicy
     */
    private Polygon rightBorderPolygon = new Polygon();



    /**
     * Wielokat opisujac ksztalt gwiazdy
     */
    private Polygon starPolygon = new Polygon();

    /**
     * Wielokat opisujac ksztalt plywajacego baku z paliwem
     */
    private Polygon tankFuelPolygon = new Polygon();

    /**
     * Obiekt klasy opisującej model poziomu będący modelem konkretnego poziomu (poziom1,poziom2,...)
     * Nie tworze instancji, przypisuję gotową instancję klasy będącej atrybutem klasy JsonData.
     */
    private final LevelModel levelModel1 = Data.getLevelModel1();
    /**
     * Obiekt klasy opisującej model poziomu będący modelem konkretnego poziomu (poziom1,poziom2,...)
     * Nie tworze instancji, przypisuję gotową instancję klasy będącej atrybutem klasy JsonData.
     */
    private final LevelModel levelModel2 = Data.getLevelModel2();
    /**
     * Obiekt klasy opisującej model poziomu będący modelem konkretnego poziomu (poziom1,poziom2,...)
     * Nie tworze instancji, przypisuję gotową instancję klasy będącej atrybutem klasy JsonData.
     */
    private final LevelModel levelModel3 = Data.getLevelModel3();

    /**
     * Obiekt klasy Level pozwalajacy na szybsze dojscie do zmiennych konfiguracyjnych wczytanych podczas rozpoczecia programu
     */
    private final Level levelsDetails = Data.getConfig().getLevel();

    /**
     * Zmienna obiektowa klasy pozwalajacej na stworzenie animacji dzialania sily grawitacji, sciagajacej statek na powierzchnie planety
     */
    private final TranslateTransition gravitationTransition = new TranslateTransition();
    /**
     * Zmienna obiektowa klasy pozwalajacej na stworzenie animacji dzialania silnikow statku
     */
    private final TranslateTransition playerTransition = new TranslateTransition();
    /**
     * Zmienna obiektowa klasy pozwalajacej na stworzenie animacji wybuchu statku, po niefortunnym rozbiciu przez gracza
     */
    private final ScaleTransition explosionAnimation = new ScaleTransition();
    /**
     * Zmienna obiektowa klasy pozwalajacej na stworzenie animacji poruszania sie zbiornika z paliwem
     */
    private final TranslateTransition fuelTankTransition = new TranslateTransition();

    private final ScaleTransition starScaleTransition = new ScaleTransition();

    /**
     * Zmienna obiektowa klasy Stopwatch pozwalajaca na zmierzenie czasu przejscia danego poziomu
     */
    private StopWatch stopWatch;

    /**
     * Zmienna klasy Shape pozwalajaca na detekcje kolizji statku z gwiazda
     */
    private Shape intersectWithStar;
    /**
     * Zmienna klasy Shape pozwalajaca na detekcje kolizji statku z powierzchnia planety
     */
    private Shape intersectWithSurface;
    /**
     * Zmienna klasy Shape pozwalajaca na detekcje kolizji statku z lewa granica planszy
     */
    private Shape intersectWithLeftBorder;
    /**
     * Zmienna klasy Shape pozwalajaca na detekcje kolizji statku z prawa granica planszy
     */
    private Shape intersectWithRightBorder;
    /**
     * Zmienna klasy Shape pozwalajaca na detekcje kolizji statku z bakiem paliwa
     */
    private Shape intersectWithTankFuel;
    /**
     * Zmienna pozwalajaca na zaznaczenie miejsca w trakcie dzialania gry,gdy przycisk dzialania silnikow jest puszczony
     */
    private boolean is_S_released;
    /**
     * Zmienna pozwalajaca na zaznaczenie miejsca w trakcie dzialania gry, gdy statek zostal zniszczony
     */
    private boolean isDestroyed = false;
    /**
     * Zmienna pozwalajaca na zaznaczenie miejsca w trakcie dzialania gry, gdzie statek aktualnie sie znajduje ( konkretnie w osi Y )
     */
    private double locationForGravitation;

    /**
     * Zmienna przechowujaca podstawowa wysokosc obszaru z gra
     */
    private double basicGamePaneHeight;
    /**
     * Zmienna przechowujaca podstawowa wysokosc powierzchni planet
     */
    private double basicSurfacePolygonHeight;
    /**
     * Zmienna przechowujaca podstawowa wysokosc statku kosmicznego
     */
    private double basicSpaceCraftHeight;
    /**
     * Zmienna przechowujaca podstawowa szerokosc powierzchni planety
     */
    private double basicSurfaceWidth;
    /**
     * Zmienna przechowujaca podstawowa szerokosc statku kosmicznego
     */
    private double basicSpaceCraftWidth;

    /**
     * Zmienna przechowujaca uaktualniona (nowa) szerokosc panelu z gra
     */
    private double newWidthValue;
    /**
     * Zmienna przechowujaca uaktualniona (nowa) wysokosc panelu z gra
     */
    private double newHeightValue;

    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca tempo dzialania animacji
     */
    private static double rate = 1;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca aktualny poziom do wgrania
     */
    private static int levelToLoad = 1;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca aktualna wartosc paliwa do zuzycia przez gracza podczas gry
     */
    private static double fuel = 1;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca aktualna ilosc zyc gracza
     */
    private static int healthP;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca Id aktualnego poziomu trudnosci
     */
    private static int difficultyId;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca flage na moment w programie w ktorym gra zostala zapauzowana
     */
    private static boolean pauseWasMade = false;

    private static boolean tankFuelWasTaken = false;
    private static boolean starWasTaken = false;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca flage na moment w programie w ktorym gracz opuscil
     * z planety zamiast na niej wyladowac
     */
    private static boolean leftPlanet;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca wartosc wspolczynnika predkosci, ktory steruje w osi X statkiem
     */
    private static double leftRightCoefficient = 2.8;
    /**
     * Zmienna przechowujaca statyczna zmienna przechowywujaca wartosc aktualna wspolczynnika rozszerzenia planszy z gra
     */
    private static double widthRatio=1;
    /**
     * Zmienna przechowujaca Obiekt klasy Player - Zmienna jest inicjalizowana podczas kazdego rozpoczecia gry
     */
    private Player player;

    /**
     * Zmienna przechowujaca Filtrowana liste graczy
     */
    private FilteredList<Player> filteredList;
    /**
     * Zmienna przechowujaca  Obiekt typu Predicate - pozwalajacy na zaznaczenie wszystkich graczy, jako tych
     * ktorzy maja byc sortowani w liscie wynikow
     */
    private Predicate<Player> allPlayers;
    /**
     * Zmienna przechowywujaca sortowana liste graczy
     */
    private SortedList<Player> sortedList;



    /**
     * Potezna Funkcja Inicjalizujaca.
     * Wywoluje sie sama!!!
     * Wywoluje sie podczas wczytania okna, do ktorego dany controller jest zbindowany. Tutaj gdy glowne okno gry sie wczytuje.
     * W skrocie co robi:
     * 1. Inicjalizuje zmienne, ktore powinny zostac zainicjalizowane podczas stworzenia glownego okna
     * 2. Obsluguje Change Listenery, (np. skalowanie szerokosci i wysokosci gry)
     * 3. Obsluguje Event Handlery, (np. gdy zostanie wcisniety przycisk Pauzy, Odpauzowania, Przyciski sterowania statkiem)
     */
    public void initialize() {

        basicGamePaneHeight = gamePane.getPrefHeight();
        basicSurfacePolygonHeight = levelModel1.getBasicSurfaceHeight();
        basicSpaceCraftHeight = levelModel1.getBasicSpaceCraftHeight();
        basicSurfaceWidth = levelModel1.getBasicSurfaceWidth();
        basicSpaceCraftWidth = levelModel1.getBasicSpaceCraftWidth();
        barOfFuel.setProgress(fuel);

        gamePaneHBox.getChildren().add(spaceCraftPolygon);
        gamePaneLeftBorderBox.getChildren().add(leftBorderPolygon);
        gamePaneHBox.getChildren().add(starPolygon);
        gamePaneRightBorderBox.getChildren().add(rightBorderPolygon);
        gamePane.setBottom(surfaceShapePolygon);
        gamePane.setCenter(tankFuelPolygon);

        enableButtons();


        allPlayers = new Predicate<Player>() {
            @Override
            public boolean test(Player player) {
                return true;
            }
        };

        filteredList = new FilteredList<>(Data.getListOfPlayers(), allPlayers);

        sortedList = new SortedList<>(filteredList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return -Double.compare(p1.getGamingResult(), p2.getGamingResult());
            }
        });


        gravitationTransition.statusProperty().addListener(new ChangeListener<Animation.Status>() {
            @Override
            public void changed(ObservableValue<? extends Animation.Status> observableValue, Animation.Status oldStat, Animation.Status newStat) {
                if (newStat == Animation.Status.PAUSED) {
                    if (oldStat != newStat) {
                        stopWatch.stopCheckPoint();
                    }
                } else if (newStat == Animation.Status.RUNNING) {
                    if (oldStat != newStat) {
                        stopWatch.start();
                    }
                }
            }
        });


        barOfFuel.progressProperty().addListener(new ChangeListener<Number>() {
            private double actualFuel;

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldF, Number newF) {
                actualFuel = Math.abs((double) Math.round((double) newF * 100.0) / 100.0);

                if (actualFuel == 0.00) {
                    fuelToUse.setText("PUSTY BAK");
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

                if (pauseWasMade && (keyEvent.getCode().equals(KeyCode.W) || keyEvent.getCode().equals(KeyCode.D) || keyEvent.getCode().equals(KeyCode.A))) {
                    // nie dotykac - naprawa bugu

                } else {

                    if (keyEvent.getCode().equals(KeyCode.W) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {
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

                if (pauseWasMade && (keyEvent.getCode().equals(KeyCode.W) || keyEvent.getCode().equals(KeyCode.D) || keyEvent.getCode().equals(KeyCode.A))) {
                    // nie dotykac - naprawa bugu
                } else {

                    if (keyEvent.getCode().equals(KeyCode.P) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                        if (!pauseWasMade) {
                            pauseWasMade = true;
                        }
                        enableButtons();
                        fuelTankTransition.pause();
                        gravitationTransition.pause();
                        spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftImage()));

                    }

                    if (keyEvent.getCode().equals(KeyCode.O) && gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                        if (pauseWasMade) {
                            pauseWasMade = false;
                        }

                        disableButtons();
                        is_S_released = true;
                        fuelTankTransition.play();
                        gravitationTransition.play();
                        rate = 0.5;
                        gravitationTransition.setRate(rate);

                    }

                    if (keyEvent.getCode().equals(KeyCode.W) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {

                        is_S_released = false;
                        if (!isDestroyed && fuel > 0 && gravitationTransition.getStatus() != Animation.Status.PAUSED) {
                            accelerateUP();
                            spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftDownFireImage()));
                        }

                    }

                    if (keyEvent.getCode().equals(KeyCode.D) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {

                        if (!isDestroyed && fuel > 0 && gravitationTransition.getStatus() != Animation.Status.PAUSED) {
                            accelerateToRight(leftRightCoefficient);
                            spaceCraftPolygon.setFill(new ImagePattern(Data.getSpaceCraftLeftFireImage()));
                        }

                    }

                    if (keyEvent.getCode().equals(KeyCode.A) && gravitationTransition.getStatus() == Animation.Status.RUNNING) {

                        if (!isDestroyed && fuel > 0 && gravitationTransition.getStatus() != Animation.Status.PAUSED) {
                            accelerateToLeft(leftRightCoefficient);
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


    } //end of initialize method


    public void animateStar(Polygon starPolygon){


            if(levelToLoad == 1){
                starPolygon.setTranslateY(200);
            }else if(levelToLoad == 2){
                starPolygon.setTranslateY(150);
                starPolygon.setTranslateX(200);
            }else if(levelToLoad == 3){
                starPolygon.setTranslateY(190);
                starPolygon.setTranslateX(-140);
            }

            starScaleTransition.setDuration(Duration.seconds(0.9));
            starScaleTransition.setToX(1.35);
            starScaleTransition.setToY(1.35);
            starScaleTransition.setCycleCount(Animation.INDEFINITE);
            starScaleTransition.setAutoReverse(true);
            starScaleTransition.setNode(starPolygon);
            starScaleTransition.play();

    }

    /**
     * Funkcja, tworzaca animacje poruszania sie zbiorniku z paliwem
     * @param tankFuelPolygon Wielokat opisujacy ksztalt zbiornika z paliwem
     */
    public void animateTankFuel(Polygon tankFuelPolygon) {

        if (fuelTankTransition.getStatus() == Animation.Status.STOPPED || fuelTankTransition.getStatus() == Animation.Status.PAUSED ) {
            tankFuelPolygon.setTranslateX(-300);
            tankFuelPolygon.setTranslateY(-150);

            fuelTankTransition.setDuration(Duration.seconds(4));
            fuelTankTransition.setToX(300);
            fuelTankTransition.setToY(100);
            fuelTankTransition.setCycleCount(Animation.INDEFINITE);
            fuelTankTransition.setAutoReverse(true);
            fuelTankTransition.setNode(tankFuelPolygon);
            fuelTankTransition.play();

        }

    }


    /**
     * Funkcja "obezwladniajaca" dzialanie przyciskow w pewnych momentach. Podczas grania przez gracza.
     * Przyciski sa ponownie odblokowywane gdy gracz przejdzie gre/straci wszystkie zycia/zapauzuje gre
     */
    public void disableButtons() {
        if (!startButton.isDisabled() || !rulesButton.isDisabled() || !resultsButton.isDisabled()) {
            startButton.setDisable(true);
            rulesButton.setDisable(true);
            resultsButton.setDisable(true);
        }

    }
    /**
     * Funkcja odblokowywujaca dzialanie przyciskow w pewnych momentach. Podczas gdy gracz nie gra lub skonczyl rozgrywke.
     * Przyciski sa odblokowywane gdy gracz przejdzie gre/straci wszystkie zycia/zapauzuje gre
     */
    public void enableButtons() {
        if (startButton.isDisabled() || rulesButton.isDisabled() || resultsButton.isDisabled()) {
            startButton.setDisable(false);
            rulesButton.setDisable(false);
            resultsButton.setDisable(false);
        }
    }

    /**
     * Funkcja Skaluja Plansze gry w osi X (Szerokosc) oraz jej wszystkie elementy rowniez w tej osi!
     * @param newValue Nowa szerokosc planszy z gra
     */
    public void widthGamePaneScaling(Double newValue) {

        if (basicSurfaceWidth < (double) newValue) {
            // System.out.println("Rozszerzam");
            double ratioMoreThan1 = (double) newValue / basicSurfaceWidth;
            widthRatio = ratioMoreThan1;
            surfaceShapePolygon.setScaleX(ratioMoreThan1);
            surfaceShapePolygon.setTranslateX(basicSurfaceWidth * ratioMoreThan1 / 2 - basicSurfaceWidth / 2);

            spaceCraftPolygon.setScaleX(ratioMoreThan1);
            spaceCraftPolygon.setTranslateX(basicSpaceCraftWidth * ratioMoreThan1 / 2 - basicSpaceCraftWidth / 2);

            tankFuelPolygon.setScaleX(ratioMoreThan1);
            tankFuelPolygon.setTranslateX(-300*ratioMoreThan1);

             if(levelToLoad == 2){
                starPolygon.setTranslateX(200*ratioMoreThan1);
            }else if(levelToLoad == 3){
                starPolygon.setTranslateX(-140*ratioMoreThan1);
            }


            playerTransition.setRate(ratioMoreThan1);
            leftRightCoefficient = (ratioMoreThan1 * 2.8);


            if (fuelTankTransition.getStatus() == Animation.Status.RUNNING || fuelTankTransition.getStatus() == Animation.Status.PAUSED) {
                fuelTankTransition.stop();
                fuelTankTransition.setToY(300.0 * ratioMoreThan1);
                fuelTankTransition.play();
            }


        } else {
            // System.out.println("Zwezam");
            double ratioLessThan1 = (double) newValue / basicSurfaceWidth;
            widthRatio = ratioLessThan1;

            if(widthRatio < 1){
                leftRightCoefficient = 2.8;
            }

            surfaceShapePolygon.setScaleX(ratioLessThan1);
            surfaceShapePolygon.setTranslateX((basicSurfaceWidth / 2 - (basicSurfaceWidth / 2) * ratioLessThan1) * (-1));

            spaceCraftPolygon.setScaleX(ratioLessThan1);
            spaceCraftPolygon.setTranslateX((basicSpaceCraftWidth / 2 - (basicSpaceCraftWidth / 2) * ratioLessThan1) * (-1));

            tankFuelPolygon.setScaleX(ratioLessThan1);
            tankFuelPolygon.setTranslateX(-300*ratioLessThan1);

            if(levelToLoad == 2){
                starPolygon.setTranslateX(200*ratioLessThan1);
            }else if(levelToLoad == 3){
                starPolygon.setTranslateX(-140*ratioLessThan1);
            }

            playerTransition.setRate(ratioLessThan1);

            if (fuelTankTransition.getStatus() == Animation.Status.RUNNING || fuelTankTransition.getStatus() == Animation.Status.PAUSED) {
                fuelTankTransition.stop();
                fuelTankTransition.setToX(300.0 * ratioLessThan1);
                fuelTankTransition.play();
            }

        }

    }

    /**
     * Funkcja Skaluja Plansze gry w osi Y (Wysokosc) oraz jej wszystkie elementy rowniez w tej osi!
     * @param newValue Nowa wysokosc planszy z gra
     */
    public void heightGamePaneScaling(Double newValue) {
        if (basicGamePaneHeight < (double) newValue) {
            // System.out.println("Zwiekszam okno");
            // System.out.println(gamePane.getHeight()); 671px
            double ratioMoreThan1 = (double) newValue / basicGamePaneHeight;
            surfaceShapePolygon.setScaleY(ratioMoreThan1);
            surfaceShapePolygon.setTranslateY(((basicSurfacePolygonHeight + 3) / 2 - (basicSurfacePolygonHeight / 2) * ratioMoreThan1));

            spaceCraftPolygon.setScaleY(ratioMoreThan1);
            spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight / 2 - (basicSpaceCraftHeight / 2) * ratioMoreThan1) * (-1));

            leftBorderPolygon.setScaleY(ratioMoreThan1);
            leftBorderPolygon.setTranslateY((500.0 / 2 - (500.0 / 2) * ratioMoreThan1) * (-1));

            rightBorderPolygon.setScaleY(ratioMoreThan1);
            rightBorderPolygon.setTranslateY((500.0 / 2 - (500.0 / 2) * ratioMoreThan1) * (-1));

            tankFuelPolygon.setScaleY(ratioMoreThan1);
            tankFuelPolygon.setTranslateY(-150*ratioMoreThan1);


            if(levelToLoad == 1){
                starPolygon.setTranslateY(200*ratioMoreThan1);
            }else if(levelToLoad == 2){
                starPolygon.setTranslateY(150*ratioMoreThan1);
            }else if(levelToLoad == 3){
                starPolygon.setTranslateY(190*ratioMoreThan1);
            }

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING || gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                gravitationTransition.stop();
                gravitationTransition.setToY(515 * ratioMoreThan1 + (50.0 * ratioMoreThan1 - 50.0));
                gravitationTransition.play();
            }

            if (fuelTankTransition.getStatus() == Animation.Status.RUNNING || fuelTankTransition.getStatus() == Animation.Status.PAUSED) {
                fuelTankTransition.stop();
                fuelTankTransition.setToY(100.0 * ratioMoreThan1 + (50.0 * ratioMoreThan1 - 50.0));
                fuelTankTransition.play();
            }



        } else {

            //  System.out.println("Zmniejszam okno");
            double ratioLessThan1 = (double) newValue / basicGamePaneHeight;
            surfaceShapePolygon.setScaleY(ratioLessThan1);
            surfaceShapePolygon.setTranslateY((basicSurfacePolygonHeight * ratioLessThan1 / 2 - basicSurfacePolygonHeight / 2) * (-1));

            spaceCraftPolygon.setScaleY(ratioLessThan1);
            spaceCraftPolygon.setTranslateY((basicSpaceCraftHeight * ratioLessThan1 / 2 - basicSpaceCraftHeight / 2));

            tankFuelPolygon.setScaleY(ratioLessThan1);
            tankFuelPolygon.setTranslateY(-150*ratioLessThan1);

            if(levelToLoad == 1){
                starPolygon.setTranslateY(200*ratioLessThan1);
            }else if(levelToLoad == 2){
                starPolygon.setTranslateY(150*ratioLessThan1);
            }else if(levelToLoad == 3){
                starPolygon.setTranslateY(190*ratioLessThan1);
            }

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING || gravitationTransition.getStatus() == Animation.Status.PAUSED) {
                gravitationTransition.stop();
                gravitationTransition.setToY(515 * ratioLessThan1 - (25.0 - 25.0 * ratioLessThan1));
                gravitationTransition.play();
            }


            if (fuelTankTransition.getStatus() == Animation.Status.RUNNING || fuelTankTransition.getStatus() == Animation.Status.PAUSED) {
                fuelTankTransition.stop();
                fuelTankTransition.setToY(100.0 * ratioLessThan1 - (25.0 - 25.0 * ratioLessThan1));
                fuelTankTransition.play();
            }

        }

    }

    /**
     * Funkcja tworzaca animacje grawitacji, dzialajacej na statek kosmiczny
     * @param spaceCraft Wielokat opisujacy ksztalt statku kosmicznego
     */
    public void animateGravitation(Polygon spaceCraft) {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {

            gravitationTransition.setDelay(Duration.seconds(0.5));
            gravitationTransition.setDuration(Duration.seconds(levelsDetails.getGravityForce().get(difficultyId))); // w jakim czasie ma opasc statek - zmienna konfiguracyjna (grawitacja)
            gravitationTransition.setToY(515); // 512.8 Na do jakiego miejsca ma spasc - dopoki nie uderzy w ziemie! - Piękna funkcja!
            gravitationTransition.setNode(spaceCraft);
            gravitationTransition.play();

            gravitationTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (spaceCraft.getTranslateY() < 50) {
                        leftPlanet = true;
                        //System.out.println("Wyleciales z planety!");
                        gravitationTransition.stop();
                        fuelTankTransition.stop();

                        if (gamePaneHBox.getChildren().size() != 0) {
                            gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
                        }
                        spaceCraftPolygon = new Polygon();
                        gamePaneHBox.getChildren().add(spaceCraftPolygon);

                    }
                    if (gravitationTransition.getRate() <= 0.5 && !leftPlanet) {
                        //System.out.println("Gratulacje, wylądowales dobrze!");


                        stopWatch.stop();
                        durationOfPlay.setText(stopWatch.toString());
                        fuelTankTransition.stop();
                        gamePane.getChildren().removeAll(tankFuelPolygon);

                        double pointsForLevel = Data.getConfig().getLevel().getBasicPointsForLevel().get(difficultyId);
                        double pointsForFuelLeft = Math.ceil(fuel * 250);
                        double factorForTime = Math.floor(stopWatch.getLevelDurations().get(levelToLoad - 1) / 4); // nie da sie w 4 sekundy przejsc wiec nie ma problemu
                        double totalResult = Math.ceil((pointsForLevel + pointsForFuelLeft) - factorForTime*20);
                        player.setGamingResult(totalResult);
                        labelPoints.setText(""+player.getGamingResult());

                        levelToLoad++;
                        fuelTankTransition.stop();

                        if (levelToLoad <= 3 && !leftPlanet) {
                            loadLevel(levelToLoad);
                        }

                        if (levelToLoad == 4) {
                            enableButtons();
                            showSaveResultsWindow();
                        }

                    } else {
                        animateExplosion();
                    }

                }
            });

        }

    }

    /**
     * Funkcja opisujaca algorytm zuzycia paliwa, gdy gracz uzywa silnikow statku
     */
    public void useFuel() {
        if (fuel > 0) {
            Timeline timeline = new Timeline();
            fuel = fuel - levelsDetails.getAmountOfFuel().get(difficultyId);

            KeyValue keyValue = new KeyValue(barOfFuel.progressProperty(), fuel);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValue);
            timeline.getKeyFrames().add(keyFrame);

            timeline.play();
        }

    }

    /**
     * Funkcja opisujaca animacje poruszania stakiem w prawo
     * @param coefficient Zmienna opisujaca wspolczynnik tempa przesuniecia
     */
    public void accelerateToRight(double coefficient) {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            playerTransition.setDuration(Duration.seconds(0.02));
            playerTransition.setToX(spaceCraftPolygon.translateXProperty().getValue() + coefficient);
            playerTransition.setNode(spaceCraftPolygon);
            playerTransition.play();


        }
        useFuel();
    }

    /**
     * Funkcja opisujaca animacje poruszania stakiem w lewo
     * @param coefficient Zmienna opisujaca wspolczynnik tempa przesuniecia
     */
    public void accelerateToLeft(double coefficient) {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {
            playerTransition.setDuration(Duration.seconds(0.02));
            playerTransition.setToX(spaceCraftPolygon.translateXProperty().getValue() - coefficient);
            playerTransition.setNode(spaceCraftPolygon);
            playerTransition.play();


        }
        useFuel();

    }

    /**
     * Funkcja opisujaca animacje poruszania statkiem do gory (tylko w osi Y)
     */
    public void accelerateUP() {

        if (gamePane.getChildren().size() != 0 && gamePaneHBox.getChildren().size() != 0) {

            if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
                if (rate > 0) {
                    rate = rate - 0.035;
                    gravitationTransition.setRate(rate);
                    useFuel();
                } else if (rate <= 0 && rate > -0.15) {
                    rate = rate - 0.02;
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
     * Funkcja tworzaca animacje eksplozji statku po niefortunnym zderzeniu
     */
    public void animateExplosion() {

        if (gamePaneHBox.getChildren().size() != 0 && gravitationTransition.getStatus() == Animation.Status.STOPPED) {

            explosionAnimation.setDuration(Duration.seconds(1.2));
            explosionAnimation.setToX(3.0);
            explosionAnimation.setToY(1.0);
            explosionAnimation.setCycleCount(2);
            spaceCraftPolygon.setFill(new ImagePattern(Data.getExplosionImage()));
            explosionAnimation.setNode(spaceCraftPolygon);
            explosionAnimation.play();

            explosionAnimation.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // Odejmij zycie
                    healthP--;
                    healthPoints.setText("" + healthP);
                    stopWatch.resetTimeToPause();
                    gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);

                    //zaladuj level ponownie, jezeli gracz ma zycie
                    if (healthP != 0 && explosionAnimation.getStatus() == Animation.Status.STOPPED) {

                        stopWatch.resetTimeToPause();
                        loadLevel(levelToLoad);

                    } else if (healthP == 0 && explosionAnimation.getStatus() == Animation.Status.STOPPED){
                        gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
                        enableButtons();
                        showSaveResultsWindow();
                        // Okienko z komunikatem o zapisanie wyniku gry i pokazaniem liczby zdobytych punktow
                    }

                }
            });
        }
    }

    /**
     * Funkcja wczytująca wybrany level
     * Funkcja obsluguje change listenera statku, gdy porusza sie po planszy
     * W tym Change Listenerze, jest zapisana obsluga kolizji statku z innymi Elementami planszy -
     * (bakiem paliwa, granicami planszy czy powierzchnia planety)
     * @param levelNumber Numer poziomu do wczytania
     */
    public void loadLevel(int levelNumber) {

        if (gamePaneHBox.getChildren().size() != 0) {
            this.gamePaneHBox.getChildren().removeAll(spaceCraftPolygon);
            this.gamePaneHBox.getChildren().removeAll(starPolygon);
            this.gamePane.getChildren().removeAll(tankFuelPolygon);

        }

        this.starPolygon = new Polygon();
        this.gamePaneHBox.getChildren().add(starPolygon);

        this.spaceCraftPolygon = new Polygon();
        this.gamePaneHBox.getChildren().add(spaceCraftPolygon);

        this.tankFuelPolygon = new Polygon();
        this.gamePane.setCenter(tankFuelPolygon);

        if (levelNumber == 1) {
            this.levelModel1.paintLevel(surfaceShapePolygon, spaceCraftPolygon, leftBorderPolygon, rightBorderPolygon, tankFuelPolygon, starPolygon);
        } else if (levelNumber == 2) {
            this.levelModel2.paintLevel(surfaceShapePolygon, spaceCraftPolygon, leftBorderPolygon, rightBorderPolygon, tankFuelPolygon, starPolygon);
        } else if (levelNumber == 3) {
            this.levelModel3.paintLevel(surfaceShapePolygon, spaceCraftPolygon, leftBorderPolygon, rightBorderPolygon, tankFuelPolygon, starPolygon);
        }


        if (spaceCraftPolygon.getPoints().size() != 0 && leftBorderPolygon.getPoints().size() != 0
                && rightBorderPolygon.getPoints().size() != 0 && tankFuelPolygon.getPoints().size() != 0) {

            spaceCraftPolygon.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
                private double finish, predictedFinish, acceptableValue, finishSpeed;

                @Override
                public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {

                    if (spaceCraftPolygon != null) {

                        intersectWithStar = Shape.intersect(spaceCraftPolygon,starPolygon);
                        intersectWithTankFuel = Shape.intersect(spaceCraftPolygon,tankFuelPolygon);
                        intersectWithSurface = Shape.intersect(spaceCraftPolygon, surfaceShapePolygon);
                        intersectWithRightBorder = Shape.intersect(spaceCraftPolygon, rightBorderPolygon);
                        intersectWithLeftBorder = Shape.intersect(spaceCraftPolygon, leftBorderPolygon);

                        finish = Math.floor(spaceCraftPolygon.translateYProperty().getValue());
                        predictedFinish = Math.floor(gravitationTransition.getToY());
                        acceptableValue = Math.abs(predictedFinish - finish);
                        finishSpeed = gravitationTransition.getRate();

                        if(intersectWithStar.getBoundsInLocal().getWidth() > -1) {

                            if(!starWasTaken && gravitationTransition.getStatus() == Animation.Status.RUNNING){
                                player.setGamingResult(300.0*((difficultyId+1)*0.5));
                                labelPoints.setText(""+player.getGamingResult());
                                starScaleTransition.stop();
                                starPolygon.setFill(new ImagePattern(Data.getStarImageEmpty()));
                                starWasTaken = true;
                            }

                        }

                        if(intersectWithTankFuel.getBoundsInLocal().getWidth() > -1) {

                            if(!tankFuelWasTaken && gravitationTransition.getStatus() == Animation.Status.RUNNING){
                                fuel = fuel +0.20;
                                barOfFuel.setProgress(fuel);
                                gamePane.getChildren().removeAll(tankFuelPolygon);
                                tankFuelWasTaken = true;
                            }


                        }

                        if (intersectWithSurface.getBoundsInLocal().getWidth() > -1
                                || intersectWithRightBorder.getBoundsInLocal().getWidth() > -1
                                || intersectWithLeftBorder.getBoundsInLocal().getWidth() > -1) {

                            //System.out.println("Kolizja");
                            if (acceptableValue >= 13.0 || finishSpeed > 0.5) {
                                //System.out.println("Rozbiles się! :(");
                                gravitationTransition.stop();
                                fuelTankTransition.stop();

                                animateExplosion();

                            }

                        }


                    }
                }
            });
        }

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


        pauseWasMade = false;
        tankFuelWasTaken = false;
        starWasTaken = false;
        disableButtons();
        fuel = 1.0;
        barOfFuel.setProgress(fuel);
        speedAcceptance.setText("ZLA");
        fuelToUse.setText("Paliwo");
        fuelToUse.setTextFill(Paint.valueOf("Black"));
        stopWatch.resetTimeToPause();


        animateStar(starPolygon);
        animateTankFuel(tankFuelPolygon);
        animateGravitation(spaceCraftPolygon);
        leftPlanet = false;
        isDestroyed = false;
        rate = 1;
        gravitationTransition.setRate(rate);
        stopWatch.start();


        widthGamePaneScaling(newWidthValue);
        heightGamePaneScaling(newHeightValue);
    }


    /**
     * Funkcja Obslugujaca wywolanie sie okna zapisu danych po zakonczonej grze
     * Wyswietla Okno z mozliwoscia podania nicku przez gracz i zapisie wyniku do listy w grze.
     * Otwiera sie na oddzielnym watku
     */
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

        // MULTITHREADING ! -> MUSOWO Z POWODU UZYCIA ANIMACJI
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

                        if(Data.getIsPlayingOnline()){
                            Data.addToScoreTable(player);
                            Data.addToOfflineList(player);

                            // otwarcie socketa -> wyslanie zadania -> nastepnie otrzymanie wiadomosci zwrotnej i wyslanie danych gracza
                            try (Socket socket = new Socket("127.0.0.1",14623)) {

                                // socket.setSoTimeout(5000);
                                ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
                                ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

                                String stringFromServer;

                                toServer.writeObject("SEND_NICK_RESULT");
                                stringFromServer =(String) fromServer.readObject();

                                if(stringFromServer.equals("WAITING")){
                                    toServer.writeObject(player.toString());
                                }

                                System.out.println("WYNIK DO SERWERA ZOSTAL POMYSLNIE WYSLANY");

                            }catch (ClassNotFoundException | IOException e){
                                System.out.println("Nie udalo sie polaczyc z serwerem - Wynik nie zostal wyslany - Wynik zostal zapisany lokalnie");

                            }


                        }else {
                            Data.addToScoreTable(player);
                        }

                    }

                }

            }
        });


    }


    /**
     * Funkcja obslugujaca przycisk startu gry.
     * Wyswietla okno w celu rozpoczecia nowej gry.
     * Okno otwiera sie na oddzielnym watku!!
     */
    @FXML
    public void showStartGameWindow() {
        if (gravitationTransition.getStatus() == Animation.Status.RUNNING) {
            gravitationTransition.pause();

        }
        if (explosionAnimation.getStatus() == Animation.Status.RUNNING) {
            explosionAnimation.stop();
        }


        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindowPane.getScene().getWindow());
        dialog.setTitle("Typ gry");
        dialog.setHeaderText("Okno do wyboru poziomu trudnosci i rozpoczecia gry");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Windows_fxml/StartGameWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        // MULTITHREADING - OKNO OTWIERANE NA INNYM WATKU
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
                    labelPoints.setText("0");
                    durationOfPlay.setText("0");

                    healthPoints.setText("" + levelsDetails.getHealthPoint().get(controller.getDifficultyId()));
                    healthP = levelsDetails.getHealthPoint().get(controller.getDifficultyId());
                    difficultyId = controller.getDifficultyId();
                    player = new Player("", 0);

                    if(widthRatio < 1){
                        leftRightCoefficient =2.8;
                    }else{
                        leftRightCoefficient = widthRatio * 2.8;
                    }

                    levelToLoad = 1;
                    gravitationTransition.stop();
                    fuelTankTransition.stop();
                    stopWatch = new StopWatch();
                    loadLevel(levelToLoad);



                }

            }
        });

    }

    /**
     * Funkcja obslugujaca przycisk Tablicy wynikow.
     * Wyswietla okno w celu pokazania listy graczy i ich wynikow od najlepszego do najgorszego wyniku.
     * Okno otwiera sie na oddzielnym watku!!
     */
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


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TableOfFameWindowController controller = new TableOfFameWindowController();
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                controller = fxmlLoader.getController();
                controller.getListOfScores().setItems(sortedList);
                Optional<ButtonType> result = dialog.showAndWait();

            }
        });


    }

    /**
     * Funkcja obslugujaca przycisk Zasad gry
     * Wyswietla okno z zapisana instrukcja i zasadami gry.
     * Okno otwiera sie na oddzielnym watku!!
     */
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
            }
        });


    }

    /**
     * Funkcja oblugujaca przycisk Zakoncz
     * Zamyka aplikacje
     */
    @FXML
    public void closeApplication() {
        Platform.exit();
    }
}
