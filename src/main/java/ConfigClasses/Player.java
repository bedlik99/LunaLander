package ConfigClasses;

/**
 * Klasa opisujaca obiekt Gracz naszego progremu
 */
public class Player  {

    /**
     * Zmienna przechowywujaca nick Gracza
     */
    private String nickName;
    /**
     * Zmienna przechowuwujaca wynik gracza. Inicjalizowany po skonczonej rozgrywce.
     */
    private double gamingResult;

    // Mozna wrzuc liste typu double, ktora bedzie przetrzymywac 3 czasy kazdego uczestnika

    /**
     * @return Zwraca nick Gracza
     */
    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @param nickName nick gracza
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Konstruktor ktory potrzebuje nazwy gracz i jego wyniku
     * Gdy gracz jest tworzony podczas dzialania gry nick jest nicjalizowany na pusty string: ""
     * A gaminResult na: 0
     * @param nickName Nazwa gracza
     * @param gamingResult Wynik gracza
     */
    public Player(String nickName, double gamingResult){
        this.nickName = nickName;
        this.gamingResult = gamingResult;
    }

    /**
     * @return Wynik gracza
     */
    public double getGamingResult() {
        return gamingResult;
    }

    /**
     * Wynik gracza jest uzyskiwany po kazdym skonczonym poziomie.
     * Zatem zawsze dopisujemy do poprzedniego wyniku, aktualny uzyskany na danym poziomie.
     * @param gamingResult wynik gry
     */
    public void setGamingResult(double gamingResult) {
        this.gamingResult = this.gamingResult + gamingResult;
    }

    /**
     * Nadpisana funkcja To String pozwalajaca na zapis gry w liscie graficznej programu w takim formacie jaki jest tu zapisany
     * @return String Nicku gracza i jego wyniku w przedstawionym formacie
     */
    @Override
    public String toString() {
        return nickName + "  |  " + gamingResult;
    }
}
