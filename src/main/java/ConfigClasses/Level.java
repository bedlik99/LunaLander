package ConfigClasses;

import java.util.ArrayList;
import java.util.List;

public final class Level {
    private List<String> levelName = new ArrayList<>();
    private List<String> levelFileJson =  new ArrayList<>(); // -> wczytywanie zaprojektowanych leveli
    private DifficultyLevel difficultyLevel;

    private StopWatch stopWatch;

    public List<String> getLevelName() {
        return levelName;
    }

    public List<String> getLevelFileJson() {
        return levelFileJson;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    // Koncepcja: po przejsciu levela, nalezy wywolac funkcje
    // czytajaca plik json-owy { readJsonLevel() } - stworzyc modul odczytu

}
