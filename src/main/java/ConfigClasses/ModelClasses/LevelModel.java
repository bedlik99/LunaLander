package ConfigClasses.ModelClasses;

import DataModelJSON.JsonData;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public final class LevelModel {

    private List<Double> spaceCraftPolygonPeakValues = new ArrayList<>();
    private List<Double> peakValues1 = new ArrayList<>();
    private List<Double> peakValues2 = new ArrayList<>();
    private List<Double> peakValues3 = new ArrayList<>();
    private List<Double> peakValues4 = new ArrayList<>();
    private List<Double> peakValues5 = new ArrayList<>();
    private List<Double> peakValues6 = new ArrayList<>();
    private List<Double> peakValues7 = new ArrayList<>();
    private List<Double> peakValues8 = new ArrayList<>();


    public void paint_example_level_surface(Polygon surfacePolygon,Polygon spaceCraftPolygon ){

        surfacePolygon.setStroke(Color.MIDNIGHTBLUE);
        surfacePolygon.setFill(Color.GREY);
        surfacePolygon.getPoints().addAll(peakValues1);
        surfacePolygon.getPoints().addAll(peakValues2);
        surfacePolygon.getPoints().addAll(peakValues3);
        surfacePolygon.getPoints().addAll(peakValues4);
        surfacePolygon.getPoints().addAll(peakValues5);
        surfacePolygon.getPoints().addAll(peakValues6);
        surfacePolygon.getPoints().addAll(peakValues7);
        surfacePolygon.getPoints().addAll(peakValues8);

        String imageURL = "spaceCraft.png";
        Image spaceCraftImage = new Image(imageURL);
        spaceCraftPolygon.setFill(new ImagePattern(spaceCraftImage));
        spaceCraftPolygon.getPoints().addAll(spaceCraftPolygonPeakValues);

    }


    public List<Double> getSpaceCraftPolygonPeakValues() {
        return spaceCraftPolygonPeakValues;
    }

    public List<Double> getPeakValues1() {
        return peakValues1;
    }

    public List<Double> getPeakValues2() {
        return peakValues2;
    }

    public List<Double> getPeakValues3() {
        return peakValues3;
    }

    public List<Double> getPeakValues4() {
        return peakValues4;
    }

    public List<Double> getPeakValues5() {
        return peakValues5;
    }

    public List<Double> getPeakValues6() {
        return peakValues6;
    }

    public List<Double> getPeakValues7() {
        return peakValues7;
    }

    public List<Double> getPeakValues8() {
        return peakValues8;
    }


}
