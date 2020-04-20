package ConfigClasses.LevelModelClass;

import DataModelJSON.JsonData;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

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


    // tutaj mozliwe ze w przypadku wyglądu powierzchni planety - będziemy wysyłać do funkcji listę kształtów(Polygon)
    // i okaże się lepsze zapisanie każdemu wielokątowi oddzielnie wartości wieszkołków
    /**
     * Metoda rysująca pole powierzchni oraz statek kosmiczny podczas ładowania konkretnego poziomu(poziom 1/poziom 2/ poziom 3)
     * Wartości wierzchołków wykorzystane do rysowania poziomu, są uprzednio wczytane z pliku konfiguracyjnego
     * Funkcja ustala wypełnienie <code>surfacePolygon.setFill()</code> wielokąta powierzchni planety kolorem jak i kolor krawędzi
     * <code>surfacePolygon.setStroke()</code>
     *
     * <p> Wielokąt opisujący statek kosmiczny jest wypelniany obrazkiem, którego zmienna przechowywująca
     * jest zadeklarowana i zainicjalizowana w klasie <code>JsonData</code> </p>
     * @param surfacePolygon Wielokąt opisujący wygląd powierchni planety
     * @param spaceCraftPolygon Wielokąt opisujący wygląd statku kosmicznego
     * @see Polygon
     */

    public void paintLevel(Polygon surfacePolygon,Polygon spaceCraftPolygon ){
        surfacePolygon.setStroke(Color.MIDNIGHTBLUE);
        surfacePolygon.setFill(Color.GREY);

        surfacePolygon.getPoints().addAll(surfacePeakValues);

        spaceCraftPolygon.setFill(new ImagePattern(JsonData.getSpaceCraftImage()));
        spaceCraftPolygon.getPoints().addAll(spaceCraftPeakValues);
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




}
