package ConfigClasses;

/**
 * Klasa przechowywująca zmienne konfiguracyjne - zmienne wczytywane z pliku konfiguracyjnego
 * <p>Atrybuty klasy są wczytywane z pliku konfiguracyjnego</p>
 */
public final class Config {

    /**
     * Zmienna konfiguracyjna, przetrzymująca nazwę okna głownego
     */
    private String mainWindowTitle;
    /**
     * Zmienna konfiguracyjna, przechowywująca szerokość okna
     */
    private double windowWidth;
    /**
     * Zmienna konfiguracyjna, przechowywująca wysokość okna
     */
    private double windowHeight;

    /**
     * Zmienna konfiguracyjna, przechowywująca liczbę poziomów
     */
    private int numberOfLevels;
    /**
     * Zmienna konfiguracyjna, przechowywująca Obiekt klasy Level
     * @see Level
     */
    private Level level;

    /**
     * @return Zwraca wartość zmiennej String, mówiącej o tytule okna
     */
    public String getMainWindowTitle() {
        return mainWindowTitle;
    }

    /**
     * @return Zwraca wartość zmiennej double, mówiącej o szerokości okna
     */
    public double getWindowWidth() {
        return windowWidth;
    }

    /**
     * @return Zwraca wartość zmiennej double, mówiącej o wysokości okna
     */
    public double getWindowHeight() {
        return windowHeight;
    }

    /**
     * @return Zwraca wartość zmiennej int, mówiącej o liczbie poziomów w grze
     */
    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    /**
     * @return Zwraca referencje na atrybut(Obiekt) klasy Level, z ktorego wczytujemy inne atrybuty
     * @see Level
     */
    public Level getLevel() {
        return level;
    }

}
