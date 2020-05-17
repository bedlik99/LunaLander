package ConfigClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa konfiguracyjna - atrybuty klasy są wczytywane z pliku konfiguracyjnego
 */
public final class Level {


    /**
     * Kontener - Lista przechowywująca nazwy poziomów
     */
    private List<String> levelNames = new ArrayList<>();

    /**
     * Kontener - Lista przechowywująca zmienne typu Integer - ilość punktów życia
     * grając na konkretnym poziomie trudności
     */
    private List<Integer> healthPoint = new ArrayList<>();


    /**
     * Kontener - Lista przechowywująca zmienne typu Float - wartości sił grawitacji, które ściągają statek.
     * Utalone dla konkretnych poziomach trudności
     */
    private  List<Double> gravityForce = new ArrayList<>();


    /**
     * Kontener - Lista przechowywująca zmienne typu Long - Ilość paliwa zależna od poziomu trudności
     */
    private  List<Double> amountOfFuel = new ArrayList<>();


    /**
     * Kontener - Lista przechowywująca zmienne typu Float - Podstawowe punkty za przejście konkretnego poziomu/za sukcesywne wylądowanie
     * ustalona dla konkretnych poziomów trudności
     */
    private  List<Long>  basicPointsForLevel = new ArrayList<>() ;

    /**
     * @return Zwraca listę elementów typu Integer - zawierającą punkty życia dane na przejscie gry(wszystkich poziomów)
     *      * na konkretnym: łatwym/średnim/trudnym poziomie trudności
     */
    public List<Integer> getHealthPoint() {
        return healthPoint;
    }


    /**
     * @return Zwraca listę elementów typu Float - zawierającą siły grawitacji działającą na statek,
     * na konkretnym: łatwym/średnim/trudnym poziomie trudności
     */
    public List<Double> getGravityForce() {
        return gravityForce;
    }


    /**
     * @return Zwraca listę elementów typu Long - zawierającą ilości paliwa, dane na przejscie poziomu
     * na konkretnym: łatwym/średnim/trudnym poziomie trudności
     */
    public List<Double> getAmountOfFuel() {
        return amountOfFuel;
    }


    /**
     * @return Zwraca listę elementów typu Long - zawierającą podstawowe punkty za przejscie poziomu
     * na konkretnym: łatwym/średnim/trudnym poziomie trudności
     */
    public List<Long> getBasicPointsForLevel() {
        return basicPointsForLevel;
    }


    /**
     * @return Zmienna zwracajaca liste nazw Poziomow
     */
    public List<String> getLevelNames() {
        return levelNames;
    }


}
