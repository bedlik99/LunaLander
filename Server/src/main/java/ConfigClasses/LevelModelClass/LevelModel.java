package ConfigClasses.LevelModelClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa konfiguracyjna wyglad konkretnego poziomu
 * Wygląd czyli: ukształtowanie powierzchni planety do wylądowania(rózna dla innych poziomów)
 * oraz statek(zawsze rysowany - ten sam)
 * Siła grawitacji, ilość żyć, ilość paliwa i podstawowe ilość punktów za przejście poziomu są jedynie zależne od poziomu trudności
 */
public final class LevelModel {

    /**
     * Podstawowa szerokość statku kosmicznego.
     */
    private double basicSpaceCraftWidth;

    /**
     * Podstawowa szerokość powierzchni planety.
     */
    private double basicSurfaceWidth;
    /**
     * Podstawowa wysokość statku kosmicznego.
     */
    private double basicSpaceCraftHeight;
    /**
     * Podstawowa wysokość powierzchni planety.
     */
    private double basicSurfaceHeight;
    /**
     * Kontener - Lista punktów wierzchołków wielokąta opisującego statek kosmiczny
     */
    private List<Double> spaceCraftPeakValues = new ArrayList<>();
    /**
     * Kontener - Lista przechowywująca wyglad - zmienne typy Double, wartości wierzchołków -
     * wielokąta opisującego wygląd powierzchni.
     */
    private List<Double> surfacePeakValues = new ArrayList<>();

    /**
     * Kontener - Lista przechowywujaca punkty lewej i prawej granicy planszy(maja takie same wymiary)
     */
    private List<Double> borderPeakValues = new ArrayList<>();

    /**
     * Kontener - Lista przechowywujaca punkty prawej granicy planszy
     */
    private List<Double> tankFuelPeakValues = new ArrayList<>();

    /**
     * Kontener - Lista przechowywujaca punkty elementu - gwiazdy
     */
    private List<Double> starPeakValues = new ArrayList<>();

    /**
     * @return Getter zwracajcy liste przechowywujaca punkty opisujace ksztalt wielokata, gwiazdy
     */
    public List<Double> getStarPeakValues() {
        return starPeakValues;
    }
    /**
     * @return Getter zwracajcy liste przechowywujaca punkty opisujace ksztalt wielokata, baku paliwa
     */
    public List<Double> getTankFuelPeakValues() {
        return tankFuelPeakValues;
    }

    /**
     * @return Wartość szerokości statku kosmicznego
     */
    public double getBasicSpaceCraftWidth() {
        return basicSpaceCraftWidth;
    }

    /**
     * @return Wartość szerokości powierzchni planety
     */
    public double getBasicSurfaceWidth() {
        return basicSurfaceWidth;
    }

    /**
     * @return Wartość wysokości statku kosmicznego
     */
    public double getBasicSpaceCraftHeight() {
        return basicSpaceCraftHeight;
    }
    /**
     * @return Wartość wysokości powierzchni planety
     */
    public double getBasicSurfaceHeight() {
        return basicSurfaceHeight;
    }

    /**
     * Funkcja zwracająca Listę przechowywującą wartości wierzchołków potrzebnych do narysowania wielokątu
     * opisującego statek kosmiczny
     * @return Lista wartości o typie Double - wierzchołki wielokąta
     */
    public List<Double> getSpaceCraftPeakValues() {
        return spaceCraftPeakValues;
    }

    /**
     * Funkcja zwracająca Listę przechowywującą wartości wierzchołków potrzebnych do narysowania wielokątu
     * opisującego pole powierzchni planety
     * @return Lista wartości o typie Double - wierzchołki wielokąta
     */
    public List<Double> getSurfacePeakValues() {
        return surfacePeakValues;
    }

    public List<Double> getBorderPeakValues() {
        return borderPeakValues;
    }
}
