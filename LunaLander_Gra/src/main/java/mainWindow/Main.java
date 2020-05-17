package mainWindow;

import DataModelJSON.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ConfigClasses.Config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Klasa Main, dziedziczy po klasie Application, dzięki czemu ma za zadanie
 * wczytanie głównego okna aplikacji, oraz wykonanie pewnych działań konfiguracyjnych przed włączeniem okna
 */
public class Main extends Application {
    private final static String mainConfigFileName = "configFile.json";
    private final static String[] requests = {"CONNECT", "GET_CONFIG", "GET_LEVEL_MODEL_1",
            "GET_LEVEL_MODEL_2", "GET_LEVEL_MODEL_3", "GET_RESULTS"};
    private static boolean isOnline = false;


    /**
     * Metoda wywoływana po metodzie init()(gdy metoda init() zreturnuje)
     * Działa na MainThreadzie - głównym wątku - ważna informacja, gdy bedzie tworzona część Serwisowa aplikacji
     *
     * @param primaryStage Obiekt klasy Stage(Scena) pozwalająca stworzenie okna, pobranie pliku *.fxml, pokazanie okna itp.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Config config = Data.getConfig();

        Parent root = FXMLLoader.load(getClass().getResource("/Windows_fxml/MainGameWindow.fxml"));
        primaryStage.setTitle(config.getMainWindowTitle());
        Scene mainScene = new Scene(root, config.getWindowWidth(), config.getWindowHeight());
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    /**
     * Metoda wywoływana jako pierwsza podczas "włączania" aplikacji - inicjalizuje obiekty Klas konfiguracyjnych,
     * które są potrzebne do działania programu
     */
    @Override
    public void init() throws Exception {


            System.out.println("Laczenie z serwerem...");
            try (Socket socket = new Socket("127.0.0.1", 14623)) {
                isOnline = true;

                // socket.setSoTimeout(5000);
                ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

                String stringFromServer;

                for (int i = 0; i <= (requests.length - 1); i++) {

                    if (i == 0) {
                        toServer.writeObject(requests[i]);
                        System.out.println("SERVER RESPONSE: " + fromServer.readObject());
                    } else if (i == 1) {

                        toServer.writeObject(requests[i]);
                        stringFromServer = (String) fromServer.readObject();
                        Data.loadFromServer(stringFromServer, i);

                    } else if (i == 2) {

                        toServer.writeObject(requests[i]);
                        stringFromServer = (String) fromServer.readObject();
                        Data.loadFromServer(stringFromServer, i);

                    } else if (i == 3) {

                        toServer.writeObject(requests[i]);
                        stringFromServer = (String) fromServer.readObject();
                        Data.loadFromServer(stringFromServer, i);

                    } else if (i == 4) {

                        toServer.writeObject(requests[i]);
                        stringFromServer = (String) fromServer.readObject();
                        Data.loadFromServer(stringFromServer, i);
                    } else if (i == 5) {

                        toServer.writeObject(requests[i]);
                        stringFromServer = (String) fromServer.readObject();
                        Data.loadFromServer(stringFromServer, i);

                        break;
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep interrupted");
                    }

                }

                Data.loadResults(Data.getConfig().getPlayerResultsFileName(), true);
                System.out.println("PLIKI KONFIGURACYJNE Z SERWERA ZOSTALY POMYSLNIE WGRANE");

            } catch (ClassNotFoundException | IOException e) {
                System.out.println("Serwer wylaczony - Aplikacja wlaczana w trybie OFFLINE");  // Nie udalo sie polaczyc z serwerem - pliki lokalne sa wgrywane
                isOnline = false;

                Data.loadSetupFile(mainConfigFileName);
                Data.loadSetupFile(Data.getConfig().getLevelModel1FileName());
                Data.loadSetupFile(Data.getConfig().getLevelModel2FileName());
                Data.loadSetupFile(Data.getConfig().getLevelModel3FileName());
                Data.loadResults(Data.getConfig().getPlayerResultsFileName(), false);

            }


        }


    /**
     * Metoda wywoływana po zamknięciu poprawnym programu (nie zterminowaniu!).
     * Tutaj będzie zapis wyniku(do pliku), uzyskanego przez człowieka, po skończonej grze.
     */
    @Override
    public void stop() throws Exception {

        if (isOnline) {
            Data.saveResults(Data.getConfig().getPlayerResultsFileName(), true);
            // gramy online to mozemy najwyzej do pliku lokalnego z wynikami dopisac wyniki zapisane podczas gry online
            // czyli nalezy wczytac plik lokalny z wynikami z aplikacji klienta - dopisac elementy do listy i zapisac nowa liste po zakonczeniu sesji
        } else {
            Data.saveResults(Data.getConfig().getPlayerResultsFileName(), false);  // Gramy offline to zapisujemy tak jak zawsze

        }


    }

    /**
     * Funkcja main wywołująca funkcję launch, która odpowiada za "działanie" programu czyli wywołanie funkcji
     * 1. init()
     * 2. start()
     * 3. Po zamknięciu programu stop()
     *
     * @param args Argument main
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }


}
