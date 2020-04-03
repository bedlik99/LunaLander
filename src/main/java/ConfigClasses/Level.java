package ConfigClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa konfiguracyjna - atrybuty klasy są wczytywane z pliku konfiguracyjnego
 */
public final class Level {
    /**
     * Kontener - Lista przechowywująca nazwy poziomów
     */
    private List<String> levelName = new ArrayList<>();
    /**
     * Zmienna przechowywująca obiekt klasy DifficultyLevel
     * Klasa przechowycująca różne poziomy trudności
     * @see DifficultyLevel
     */
    private DifficultyLevel difficultyLevel;
    /**
     * @return Zwraca Listę przechowywującą nazwy poziomów
     */
    public List<String> getLevelName() {
        return levelName;
    }
    /**
     * @return Zwraca referencję na obiekt klasy DifficultyLevel
     * @see DifficultyLevel
     */
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

}
