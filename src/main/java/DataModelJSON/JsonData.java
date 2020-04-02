package DataModelJSON;

// wczytanie Pliku konfiguracyjnego config.json podczas wlaczenia aplikacji
// Musimy wypelnic stale zmienne, ktore potem beda uzywane przy implementacji innych komponentow

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

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

        private static Config config;
        private static LevelModel levelModel;

        private static ObjectMapper mapper = getDefaultObjectMapper();
        private static JSONParser jsonParser = new JSONParser();

        public static Config getConfig() {
        return config;
    }
        public static LevelModel getLevelModel() { return levelModel; }

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false); // gdy chcemy dokladac wiecej elementow, a nie wiemy ile ich bedzie

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

    public static void loadFile(String fileName) {
        try {
            URL res = JsonData.class.getResource("/" + fileName);
            File file = Paths.get(res.toURI()).toFile();
            String toLoadFile = file.getAbsolutePath();

                FileReader jsonFile = new FileReader(toLoadFile);
                JSONObject obj = (JSONObject) jsonParser.parse(jsonFile);
                String jsonString = obj.toString();
                JsonNode node = JsonData.readJson(jsonString);

                if(fileName.equals("configFile.json")) {
                    config = JsonData.fromJson(node, Config.class);
                }else if(fileName.equals("levelModelExample.json")){
                    levelModel = JsonData.fromJson(node,LevelModel.class);
                }

        }  catch (IOException | ParseException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

        public static void savePlayerInfo(){

        }

}
