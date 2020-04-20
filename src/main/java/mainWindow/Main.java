package mainWindow;

import DataModelJSON.JsonData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ConfigClasses.Config;

/**
 * Klasa Main, dziedziczy po klasie Application, dzięki czemu ma za zadanie
 * wczytanie głównego okna aplikacji, oraz wykonanie pewnych działań konfiguracyjnych przed włączeniem okna
 */
public class Main extends Application {

    /**
     * Metoda wywoływana po metodzie init()(gdy metoda init() zreturnuje)
     * Działa na MainThreadzie - głównym wątku - ważna informacja, gdy bedzie tworzona część Serwisowa aplikacji
     * @param primaryStage Obiekt klasy Stage(Scena) pozwalająca stworzenie okna, pobranie pliku *.fxml, pokazanie okna itp.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Config config = JsonData.getConfig();

        Parent root = FXMLLoader.load(getClass().getResource("/Windows_fxml/MainGameWindow.fxml"));
        primaryStage.setTitle(config.getMainWindowTitle());
        Scene mainScene = new Scene(root,config.getWindowWidth(),config.getWindowHeight());
        primaryStage.setScene(mainScene);
        primaryStage.show();



    }

    /**
     * Metoda wywoływana jako pierwsza podczas "włączania" aplikacji - inicjalizuje obiekty Klas konfiguracyjnych,
     * które są potrzebne do działania programu
     */
    @Override
    public void init() throws Exception {
        JsonData.loadFile("configFile.json");
        JsonData.loadFile("levelModel1.json");
        JsonData.loadFile("levelModel2.json");
        JsonData.loadFile("levelModel3.json");

    }

    /**
     * Metoda wywoływana po zamknięciu poprawnym programu (nie zterminowaniu!).
     * Tutaj będzie zapis wyniku uzyskanego przez człowieka do pliku po skończonej grze.
     */
    @Override
    public void stop() throws Exception {
        // JsonData.saveResults();
    }

    /**
     * Funkcja main wywołująca funkcję launch, która odpowiada za "działanie "programu czyli wywołanie funkcji
     * 1. init()
     * 2. start()
     * 3. Po zamknięciu programu stop()
     * @param args Argument main
     */
    public static void main(String[] args) {
        launch(args);
    }
}
