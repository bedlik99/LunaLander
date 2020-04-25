package ConfigClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa zajmująca się liczeniem wartości czasowych
 * w określonych przez użytkownika momentach
 * <p>
 * Mierzy czas lądowania na powierzchni, uzyskany czas będzie jedną ze składowych
 * punktów zdobytych za przejście poziomu
 */

public class StopWatch {
    /**
     * Zmienna przechowywująca wartość w którym czas zaczął być mierzony
     * w milisekundach
     */
    private double start = 0.0;
    /**
     * Zmienna przechowywująca wartość w którym czas przestał być mierzony
     * w milisekundach
     */
    private double stop = 0.0;

    /**
     * Zmienna przechowywwujaca czas policzony do zatrzymania gry przez gracza
     */
    private double timeToPause = 0.0;

    /**
     * Lista przechowywujaca czasu przechodzenia kazdego poziomu.
     * Zatem bedzie maksymalnie n-elementowa , Gdzie n to liczba poziomow.
     */
    private List<Double> levelDurations = new ArrayList<>();


    /**
     * Metoda uruchamiana przy starcie stopera
     * <p>Pobiera czas w momencie wywołania, w milisekundach</p>
     */
    public void start(){
        start = System.currentTimeMillis();
    }

    /**
     * Metoda uruchamiana przy zatrzynywaniu naszego stopera
     * <p>Pobiera czas w momencie wywołania, w milisekundach</p>
     */
    public void stop(){
        stop = System.currentTimeMillis();
        levelDurations.add(getResult());
    }

    /**
     * @return Zwraca liste czasow uzyskanych przez gracza podczas gry
     */
    public List<Double> getLevelDurations() {
        return levelDurations;
    }

    /**
     * Metoda wyliczająca czas w sekundach!, który został wychwycony między dwoma
     * odstępami czasowymi
     * @return wartość czasu między dwoma punktami czasowymi w sekundach
     */
    public double getResult(){
        return ( Math.round( (timeToPause + ((stop - start) / 1000.0)) *100.0)/100.0);
    }

    /**
     * Funkcja wyliczajaca czas do zatrzymania przez gracza gry.
     */
    public void stopCheckPoint(){
        timeToPause = timeToPause+ ((double)System.currentTimeMillis() - start)/1000.0;
    }

    /**
     * Funkcja resetujaca czas podczas gdy gracz gra. A konkretnie gdy rozbije statek. - Czas jest zerowany.
     */
    public void resetTimeToPause(){
        timeToPause =0;
    }

    /**
     * @return Zwraca wartosc double czasu rozpoczecia liczenia czasu
     */
    public double getStart() {
        return start;
    }

    /**
     * @return Zwraca wartosc double czasu zakonczenia liczenia czasu
     */
    public double getStop() {
        return stop;
    }

    /**
     * Nadpisana metoda, mozemy wywołać w strumieniu wyjścia bezpośrednio instancję klasy,
     * zwróci ona pewien ciąg znaków(String)
     * @return ciąg znaków, informujący o zmierzonym czasie
     */
    public String toString(){
        return  getResult() + " s";

    }
}
