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
    private long start;
    /**
     * Zmienna przechowywująca wartość w którym czas przestał być mierzony
     * w milisekundach
     */
    private long stop;

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

    public List<Double> getLevelDurations() {
        return levelDurations;
    }

    /**
     * Metoda wyliczająca czas w sekundach!, który został wychwycony między dwoma
     * odstępami czasowymi
     * @return wartość czasu między dwoma punktami czasowymi w sekundach
     */
    public double getResult(){
        return (stop - start) / 1000.0;
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
