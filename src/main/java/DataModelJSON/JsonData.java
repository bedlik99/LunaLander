package DataModelJSON;

// wczytanie Pliku konfiguracyjnego config.json podczas wlaczenia aplikacji
// Musimy wypelnic stale zmienne, ktore potem beda uzywane przy implementacji innych komponentow

import ConfigClasses.Config;
import ConfigClasses.LevelModelClass.LevelModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Klasa abstrakcyjna, będziemy wykorzystywać jej pola w różnych miejscach, ponieważ zawiera konfiguracyjne elementy projektu.
 * Do pól klasy JsonData dostaniemy się przez statyczne odwołania: (Nazwę klasy)JsonData.nazwaParametru - bo nie możemy stworzyć jej instancji.
 * Ze względu na wyżej zaznaczoną abstrakcyjność klasy wszystkie pola - atrybuty i metody będą statyczne, aby móc się do nich odwołać. Z modyfikatorem <code>static</code>
 *
 * <p>Ze względu na wyżej wymienione zasady działania klasy, w czasie działania projektu,
 *  będzie istnieć zawsze tylko jedno możliwe odwołanie(referencja) do klas konfiguracyjnych. Poprzez statyczne odwołanie do pola klasy JsonData</p>
 */
public abstract class JsonData {

    /**
     * Obiekt klasy Image, przechowywujący obiekt graficzny(zdjęcie.png) statku kosmicznego,
     * Wykorzystywany przy tworzeniu poziomu - wtedy rysujemy statek.
     */
        private static Image spaceCraftImage = new Image("spaceCraft.png");


    /**
     * Obiekt klasy zawierającej główne zmienne konfiguracyjne - Rozmiary okna,punkty za poziom, nazwa okna itd.
     * Inicjalizacja pól tej zmiennej jest wykonywana przez wczytanie pliku konfiguracyjnego "do" tej klasy, wykorzystując
     * parsowanie pliku *.json jako obiekt klasy Config ( w tym przypadku)
     * Konstruktor klasy jest konstruktorem domyślnym - nie zawierającym inicjalizującego zmienne kodu.
     * <p> Jest to jedyna instancja klasy Config przedstawiająca wartości konfiguracyjne elementów programu</p>
     * @see Config
     */
    private static Config config;


    /**
     * Obiekt klasy zawierającej opis modelu poziomu pierwszego.Inicjalizacja pól odbywa się poprzez wczytanie zmiennych z pliku *.json
     * i parsowanie ich na obiekt klasy LevelModel1.
     *
     * <p>Zmienne konfiguracyjne to wartości wierzchołków wielokątów opisujących powierzchnię planety i statek kosmiczny.</p>
     * Konstruktor klasy jest konstruktorem domyślnym - nie zawierającym inicjalizującego zmienne kodu.
     * <p>Jest to jedyna instancja klasy przedstawiającej model poziomu w całym programie</p>
     * @see LevelModel
     */
        private static LevelModel levelModel1,levelModel2,levelModel3;


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
     * @return Obiekt klasy Config - obiekt zawierający zmienne konfiguracyjne
     * @see Config
     */
    public static Config getConfig() {
        return config;
    }


    /**
     * @return Obiekt klasy LevelModel - obiekt zawierający model planszy poziomu 1
     * @see LevelModel
     */
        public static LevelModel getLevelModel1() { return levelModel1; }


    /**
     * @return Obiekt klasy LevelModel - obiekt zawierający model planszy poziomu 2
     * @see LevelModel
     */
    public static LevelModel getLevelModel2() {
        return levelModel2;
    }


    /**
     * @return Obiekt klasy LevelModel - obiekt zawierający model planszy poziomu 3
     * @see LevelModel
     */
    public static LevelModel getLevelModel3() {
        return levelModel3;
    }


    /**
     * @return Zwraca Obiekt klasy image - obrazek.png statku kosmicznego, funkcja wywolywana podczas rysowania poziomu
     */
    public static Image getSpaceCraftImage() { return spaceCraftImage; }

    /**
     * @return Zwraca obiekt pozwalający na poprawne odczytywanie pliku *.json oraz zapisywanie do niego
     */
    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false); // gdy chcemy dokladac wiecej elementow, a nie wiemy ile ich bedzie

        return defaultObjectMapper;
    }


    /**
     * Funkcja przyjmująca na parametr skonwertowany na String'a obiekt typu JSONObject.
     * <p>Ten Obiekt klasy JSONObject to wczytany plik *.json i sparsowany na obiekt Jsonow'y.</p>
     * Następnie przy pomocy mappera czyta i zwraca obiekt klasy JsonNode,
     * którego potem parsujemy na klasę przy pomocy funkcji fromJson i przypisujemy do odpowiedniej klasy.
     * @param src plik *.json po parsowaniu na obiekt klasy JSONObject skonwertowany na obiekt typu String
     * @return  zwraca Obiekt klasy JsonNode, który będzie już bezpośrednio parsowany na Klasę Konfiguracyjną
     * @throws IOException Jeżeli coś się nie wczyta
     */
    public static JsonNode readJson(String src) throws IOException {
        return mapper.readTree(src);
    }



    /**
     * Funkcja mapuje przysłany Obiekt klasy JsonNode i przy pomocy mappera, wyciąga z wartości i przypisuje je do zmiennych
     * klasy danej w 2 parametrze. Zwraca obiekt klasy którą mapuje z obiektu JsonNode, na klasę konfiguracyjną.
     * @param node Obiekt bezpośrednio mapowany na klasę konfiguracyjną
     * @param clazz Klasa konfiguracyjna na która mapujemy
     * @param <A> Klasa konfiguracyjna np. Config, LevelModel1,LevelModel2,...
     * @return Zwraca obiekt Klasy, której przypisujemy wczytane dane
     * @throws JsonProcessingException Jeżeli przypisanie się nie powiodło
     */
    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return mapper.treeToValue(node,clazz);
    }

//    public static JsonNode toJson(Object a) {
//        return mapper.valueToTree(a);
//    }

    /**
     * <p>Metoda wczytuje plik konfiguracyjny, wykorzystując funkcje parsowania pliku z pliku o formacie *.json na klasę konfiguracyjną
     *  i tym samym inicjalizuje atrybuty obiektu klasy konfiguracyjnej, tutaj: config,levelModel1 itd.</p>
     *
     *  Najpierw pobiera adres URL pliku konfiguracyjnego *.json. Dzięki ułożeniu struktury plików przez Maven'a,
     *  posiadamy folder "resources" w którym są pliki źródłowe- obrazki, plik.fxml(określający wygląd okna),pliki konfiguracyjne *.json
     *  Dzięki temu odwołujemy się funkcją .getResource("/nazwapliku.format"). Gdzie: "nazwapliku.format" to parametr metody - obiekt klasy String
     *
     *  <p> Konstrukcja if-else ma za zadanie wybrać odpowiedni plik do wczytania i przypisać odpowiedniemu atrybutowi klasy JsonData (klasie konfiguracyjnej) -
     *   wartości wczytane z pliku </p>
     * @param fileName nazwa pliku(String), który będzie wczytany pisana z rozszerzeniem w formacie: nazwapliku.format
     */
    public static void loadFile(String fileName) {
        try {
            URL res = JsonData.class.getResource("/Files_json/" + fileName);
            File file = Paths.get(res.toURI()).toFile();
            String toLoadFile = file.getAbsolutePath();

                FileReader jsonFile = new FileReader(toLoadFile);
                JSONObject obj = (JSONObject) jsonParser.parse(jsonFile);
                String jsonString = obj.toString();
                JsonNode node = JsonData.readJson(jsonString);

                if(fileName.equals("configFile.json")) {
                    config = JsonData.fromJson(node, Config.class);
                }else if(fileName.equals("levelModel1.json")){
                    levelModel1 = JsonData.fromJson(node, LevelModel.class);
                }else if(fileName.equals("levelModel2.json")) {
                    levelModel2 = JsonData.fromJson(node, LevelModel.class);
                }else if(fileName.equals("levelModel3.json")) {
                    levelModel3 = JsonData.fromJson(node, LevelModel.class);
                }
        }  catch (IOException | ParseException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

//        public static void savePlayerInfo(){
//
//        }

}
