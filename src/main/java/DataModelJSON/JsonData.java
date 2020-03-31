package DataModelJSON;

// wczytanie Pliku konfiguracyjnego config.json podczas wlaczenia aplikacji
// Musimy wypelnic stale zmienne, ktore potem beda uzywane przy implementacji innych komponentow

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import ConfigClasses.Config;
import ConfigClasses.ModelClasses.LevelModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class JsonData {

        private static final String configFile = "/home/jahu/Dokumenty/IntelliJ_projects/JavaPrograms/Luna_Lander_PROZE/LunaLander_app/src/main/resources/configFile.json";
        private static final String levelModelExampleFile = "/home/jahu/Dokumenty/IntelliJ_projects/JavaPrograms/Luna_Lander_PROZE/LunaLander_app/src/main/resources/levelModelExample.json";
        private static Config config;  // zmienna przechowywujaca nasz plik konfiguracyjny
        private static LevelModel levelModel;

        public static Config getConfig() {
                return config;
        }

        public static LevelModel getLevelModel() {
                return levelModel;
        }

        private static File jsonFile;

           private static ObjectMapper mapper = getDefaultObjectMapper();
           private static JSONParser jsonParser = new JSONParser();

        private JsonData() { }

        private static ObjectMapper getDefaultObjectMapper() {

                ObjectMapper defaultObjectMapper = new ObjectMapper();
                defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
                // gdy chcemy dokladac wiecej elementow, a nie wiemy ile ich bedzie
                // ---
                return defaultObjectMapper;
        }

        public static JsonNode readJson(String src) throws IOException {

                return mapper.readTree(src);
        }

        public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {

                return mapper.treeToValue(node,clazz);
        }

        public static JsonNode toJson(Object a) {
                return mapper.valueToTree(a);

        }

        public static void loadConfigFile() {
                try {
//                        File json = new File(configFile);
//                        Config config= mapper.readValue(json, Config.class);

                        FileReader jsonFile = new FileReader(configFile);
                        JSONObject obj = (JSONObject) jsonParser.parse(jsonFile);
                        String jsonString = obj.toString();
                       JsonNode node = JsonData.readJson(jsonString);
                        config = JsonData.fromJson(node,Config.class);



                }  catch (IOException | ParseException ex) {
                        ex.printStackTrace();

                }
        }


        // tutaj musze napisac przykladowy jeden i go wczytac
        public static void loadLevelFile(){
                try {

//                        File json = new File(configFile);
//                       Config config= mapper.readValue(json, Config.class);

                        FileReader jsonFile = new FileReader(levelModelExampleFile);
                        JSONObject obj = (JSONObject) jsonParser.parse(jsonFile);
                        String jsonString = obj.toString();
                        JsonNode node = JsonData.readJson(jsonString);
                        levelModel = JsonData.fromJson(node,LevelModel.class);

                }  catch (IOException | ParseException ex) {
                        ex.printStackTrace();

                }

        }


        public static void savePlayerInfo(){


        }

}
