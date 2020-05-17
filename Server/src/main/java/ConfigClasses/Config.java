package ConfigClasses;

/**
 * Klasa przechowywująca zmienne konfiguracyjne - zmienne wczytywane z pliku konfiguracyjnego
 * <p>Atrybuty klasy są wczytywane z pliku konfiguracyjnego</p>
 */
public final class Config {

    /**
     * Zmienna konfiguracyjna przetrzymujaca nazwe pliku konfiguracyjnego - opisujacego wyglad levelu 1
     */
    private String levelModel1FileName;
    /**
     * Zmienna konfiguracyjna przetrzymujaca nazwe pliku konfiguracyjnego - opisujacego wyglad levelu 2
     */
    private String levelModel2FileName;
    /**
     * Zmienna konfiguracyjna przetrzymujaca nazwe pliku konfiguracyjnego - opisujacego wyglad levelu 3
     */
    private String levelModel3FileName;
    /**
     * Zmienna konfiguracyjna przetrzymujaca nazwe pliku konfiguracyjnego - przetrzymujacego wszystkie uzyskane wyniki gry
     */
    private String playerResultsFileName;


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
     * @return Zwraca wartość zmiennej String, mówiącej o nazwie pliku konfiguracyjnego
     */
    public String getLevelModel1FileName() {
        return levelModel1FileName;
    }

    /**
     * @return Zwraca wartość zmiennej String, mówiącej o nazwie pliku konfiguracyjnego
     */
    public String getLevelModel2FileName() {
        return levelModel2FileName;
    }

    /**
     * @return Zwraca wartość zmiennej String, mówiącej o nazwie pliku konfiguracyjnego
     */
    public String getLevelModel3FileName() {
        return levelModel3FileName;
    }

    /**
     * @return Zwraca wartość zmiennej String, mówiącej o nazwie pliku konfiguracyjnego
     */
    public String getPlayerResultsFileName() {
        return playerResultsFileName;
    }

    /**
     * @return Zwraca wartość zmiennej String, mówiącej o nazwie pliku konfiguracyjnego
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
