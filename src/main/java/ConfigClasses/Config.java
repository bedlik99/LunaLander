package ConfigClasses;


public final class Config {

    // Pliki konfiguracyjne okna/okien -> "zewnetrzne"
    private String mainWindowTitle;
    private int windowWidth;
    private int windowHeight;

    // Pliki konfiguracyjne gry -> "wewnetrzne"
    private int numberOfLevels;
    private Level level;


    public String getMainWindowTitle() {
        return mainWindowTitle;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public Level getLevel() {
        return level;
    }
}
