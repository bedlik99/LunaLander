package DataModelJSON;

import ConfigClasses.Config;
import ConfigClasses.LevelModelClass.LevelModel;
import ConfigClasses.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Klasa abstrakcyjna, będziemy wykorzystywać jej pola w różnych miejscach, ponieważ zawiera konfiguracyjne elementy projektu.
 * Do pól klasy JsonData dostaniemy się przez statyczne odwołania: (Nazwę klasy)JsonData.nazwaParametru - bo nie możemy stworzyć jej instancji.
 * Ze względu na wyżej zaznaczoną abstrakcyjność klasy wszystkie pola - atrybuty i metody będą statyczne, aby móc się do nich odwołać. Z modyfikatorem <code>static</code>
 *
 * <p>Ze względu na wyżej wymienione zasady działania klasy, w czasie działania projektu,
 * będzie istnieć zawsze tylko jedno możliwe odwołanie(referencja) do klas konfiguracyjnych. Poprzez statyczne odwołanie do pola klasy JsonData</p>
 */
public abstract class Data {

    /**
     * Obiekt klasy zawierającej główne zmienne konfiguracyjne - Rozmiary okna,punkty za poziom, nazwa okna itd.
     * Inicjalizacja pól tej zmiennej jest wykonywana przez wczytanie pliku konfiguracyjnego "do" tej klasy, wykorzystując
     * parsowanie pliku *.json jako obiekt klasy Config ( w tym przypadku)
     * Konstruktor klasy jest konstruktorem domyślnym - nie zawierającym inicjalizującego zmienne kodu.
     * <p> Jest to jedyna instancja klasy Config przedstawiająca wartości konfiguracyjne elementów programu</p>
     *
     * @see Config
     */
    private static Config config;


    /**
     * Obiekt z pozwalający na wczytywanie specyficznego ułożenia danych w pliku *.json i mapowanie go na konkretny
     * Obiekt posiadający dane ułożenie i vice-versa.
     */
    private static ObjectMapper mapper = getDefaultObjectMapper();

    /**
     * Obiekt pozwalający na parsowanie wczytanego pliku na JSON obiekt - Jest to przejściowy etap potrzebny do poprawnego
     * przypisania danych wczytanych z pliku do konkretnego Obiektu.
     */
    private static JSONParser jsonParser = new JSONParser();

    /**
     * Kontener -Lista "pozwalajaca sie obserwowac" (Statyczna) to znaczy ze jezeli przypiszemy ja do innej listy w naszym przypadku
     * FileteredList a potem SortedList, to gdy dolozymy element do Tej tutaj ObservableList listOfPlayer, nie musze nadpisywac
     * Listy posortowanej - sama zauwazy za zaszla zmiana i doda element
     */
    private static LinkedHashSet<Player> listOfPlayers = new LinkedHashSet<>();

    /**
     * Zmienna klasy String przetrzymujaca zawartosc pliku konfiguracyjnego
     */
    private static String configStringObject;
    /**
     * Zmienna klasy String przetrzymujaca zawartosc pliku opisujacego wyglad poziomu 1
     */
    private static String levelModel1StringObject;
    /**
     * Zmienna klasy String przetrzymujaca zawartosc pliku opisujacego wyglad poziomu 2
     */
    private static String levelModel2StringObject;
    /**
     * Zmienna klasy String przetrzymujaca zawartosc pliku opisujacego wyglad poziomu 3
     */
    private static String levelModel3StringObject;

    /**
     * Zmienna klasy String przetrzymujaca zawartosc pliku z danymi graczy
     */
    private static String resultsStringObject;


    public static String getResultsStringObject() {
        return resultsStringObject;
    }


    public static String getConfigStringObject() {
        return configStringObject;
    }

    public static String getLevelModel1StringObject() {
        return levelModel1StringObject;
    }

    public static String getLevelModel2StringObject() {
        return levelModel2StringObject;
    }

    public static String getLevelModel3StringObject() {
        return levelModel3StringObject;
    }


    /**
     * @return Zwraca ObservableList, przechowywujaca dane graczy
     */
    public static LinkedHashSet<Player> getSetOfPlayers() {
        return listOfPlayers;
    }

    /**
     * @return Obiekt klasy Config - obiekt zawierający zmienne konfiguracyjne
     * @see Config
     */
    public static Config getConfig() {
        return config;
    }


    /**
     * @return Zwraca obiekt pozwalający na poprawne odczytywanie pliku *.json oraz zapisywanie do niego
     */
    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // gdy chcemy dokladac wiecej elementow, a nie wiemy ile ich bedzie

        return defaultObjectMapper;
    }

    /**
     * Funkcja przyjmująca na parametr skonwertowany na String'a obiekt typu JSONObject.
     * <p>Ten Obiekt klasy JSONObject to wczytany plik *.json i sparsowany na obiekt Jsonow'y.</p>
     * Następnie przy pomocy mappera czyta i zwraca obiekt klasy JsonNode,
     * którego potem parsujemy na klasę przy pomocy funkcji fromJson i przypisujemy do odpowiedniej klasy.
     *
     * @param src plik *.json po parsowaniu na obiekt klasy JSONObject skonwertowany na obiekt typu String
     * @return zwraca Obiekt klasy JsonNode, który będzie już bezpośrednio parsowany na Klasę Konfiguracyjną
     * @throws IOException Jeżeli coś się nie wczyta
     */
    public static JsonNode readJson(String src) throws IOException {
        return mapper.readTree(src);
    }


    /**
     * Funkcja mapuje przysłany Obiekt klasy JsonNode i przy pomocy mappera, wyciąga z wartości i przypisuje je do zmiennych
     * klasy danej w 2 parametrze. Zwraca obiekt klasy którą mapuje z obiektu JsonNode, na klasę konfiguracyjną.
     *
     * @param node  Obiekt bezpośrednio mapowany na klasę konfiguracyjną
     * @param clazz Klasa konfiguracyjna na która mapujemy
     * @param <A>   Klasa konfiguracyjna np. Config, LevelModel1,LevelModel2,...
     * @return Zwraca obiekt Klasy, której przypisujemy wczytane dane
     * @throws JsonProcessingException Jeżeli przypisanie się nie powiodło
     */
    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return mapper.treeToValue(node, clazz);
    }


    /**
     * Funkcja dodajaca wynik gracza do listy na serwerze
     * @param playerStringObject Obiekt String przeslany z aplikacji klienta
     */
    public static void addResultToServer(String playerStringObject) {

        String[] itemPieces = playerStringObject.split(" [|] ");

        String nickName = itemPieces[0];
        if (!nickName.trim().isEmpty()) {
            String scoreInString = itemPieces[1];
            double scoreInDouble = Double.parseDouble(scoreInString);

            Player ExPlayer = new Player(nickName, scoreInDouble);
            listOfPlayers.add(ExPlayer);

            try{
                saveResults(config.getPlayerResultsFileName());
            }catch (IOException e){
                System.out.println("Nie udalo sie zaktualizowac serwerowego pliku z wynikami graczy " + e.getMessage());
            }

        }
    }

    /**
     * Funkcja parsujaca pliki .*json na obiekty klasy String
     * @param fileName nazwa pliku do sparsowania
     */
    public static void receiveString(String fileName) {
        try {
//            URL res = Data.class.getResource("/ConfigFiles/" + fileName);
//            File file = Paths.get(res.toURI()).toFile();
            Path path = Paths.get("src/main/resources/ConfigFiles/",fileName);
            File file = path.toFile();
            String toLoadFile = file.getAbsolutePath();

            FileReader jsonFile = new FileReader(toLoadFile);
            JSONObject obj = (JSONObject) jsonParser.parse(jsonFile);
            String jsonString = obj.toString();

            if (fileName.equals("configFile.json")) {
              configStringObject = jsonString;
              JsonNode node = Data.readJson(jsonString);
              config = Data.fromJson(node, Config.class);
            } else if (fileName.equals("levelModel1.json")) {
                levelModel1StringObject = jsonString;
            } else if (fileName.equals("levelModel2.json")) {
                levelModel2StringObject = jsonString;
            } else if (fileName.equals("levelModel3.json")) {
                levelModel3StringObject = jsonString;
            }

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Funkcja parsujaca plik z wynikami gracza *.txt na pojedynczy obiekt klasy String
     * @param fileName nazwa pliku z zapisanymi wynikami
     * @throws IOException Wyjatek rzucany w przypadku wystapienia bledu w czasie czytania pliku
     */
    public static void readResultsAsString(String fileName) throws IOException {

        Path path = Paths.get("src/main/resources/ConfigFiles/",fileName);
        String data = "";
        data = new String(Files.readAllBytes(path));

        resultsStringObject = data;

    }


    /**
     * Metoda wczytujaca plik .txt, w ktorym sa zapisani gracze, a dokladniej ich nicki oraz odpowiednio wyniki.
     * Podczas wczytywania lini - czyli danych jednego gracza - atrybuty sa zczytywane, tworzony jest gracz o takich atrybutach
     * I ten gracz jest dodawany do Listy Observable - listy wszystkich graczy
     * @param fileName Nazwa pliku tutaj jest to playersResults.txt
     * @throws IOException Wyjatek wyrzuca sie jezeli nie znajdzie takiego pliku
     */
    public static void loadResults(String fileName) throws IOException{

        Path path = Paths.get("src/main/resources/ConfigFiles/",fileName);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String input;

            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split(" [|] ");

                String nickName = itemPieces[0];
                if (!nickName.trim().isEmpty()) {
                    String scoreInString = itemPieces[1];
                    double scoreInDouble = Double.parseDouble(scoreInString);

                    Player ExPlayer = new Player(nickName, scoreInDouble);

                        listOfPlayers.add(ExPlayer);

                }

            }
        }
    }


    /**
     * Funkcja zapisujaca wyniki do pliku .txt. Wyniki sa brane z listy Observable, ktora zawiera wszystkich graczy.
     * Lista Observable - listOfPlayers jest zawsze uzupelniana podczas wlaczania gry i potem sa ewentualnie dopisywani gracze
     * @param fileName nazwa pliku tutaj - playersResults.txt
     * @throws IOException Wyjatek jest rzucany jezeli nie udalo sie otworzyc pliku
     */
    public static void saveResults(String fileName) throws IOException{

        Path path = Paths.get("src/main/resources/ConfigFiles/",fileName);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            int i = 1;
            for (Player item : listOfPlayers) {
                bw.write(String.format("%s" + " | " + "%s",
                        item.getNickName(),
                        item.getGamingResult()));

                if (i != listOfPlayers.size()) {
                    bw.newLine();
                }
                i++;
            }

        }

    }

    
    
    
}


















