package ConfigClasses;

public class StopWatch {

    private long start;
    private long stop;
    private String name;

    public StopWatch(String name) {
        this.name = name;
    }

    // metoda uruchamiana przy starcie stopera
    public void start(){
    // pobieramy aktualny czas - start stopera
        start = System.currentTimeMillis();
    }

    // metoda uruchamiana przy zatrzynywaniu naszego stopera
    public void stop(){
        // pobieramy aktualny czas - stop stopera
        stop = System.currentTimeMillis();
    }

    public double getResult(){
        // zamiana milisekund na sekundy
        return (stop - start) / 1000.0;
    }

    public String toString(){
        return name + ": " + this.getResult() + " sec ";

    }
}
