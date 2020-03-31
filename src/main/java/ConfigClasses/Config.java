package ConfigClasses;


public final class Config {

    // Pliki konfiguracyjne okna/okien -> "zewnetrzne"
    private String mainWindowTitle;
    private double windowWidth;
    private double windowHeight;

    // Pliki konfiguracyjne gry -> "wewnetrzne"
    private int numberOfLevels;
    private Level level;


    public String getMainWindowTitle() {
        return mainWindowTitle;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public Level getLevel() {
        return level;
    }
}
