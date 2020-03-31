package ConfigClasses.ModelClasses;

import DataModelJSON.JsonData;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public final class LevelModel {

    private List<Double> peakValues1 = new ArrayList<>();
    private List<Double> peakValues2 = new ArrayList<>();
    private List<Double> peakValues3 = new ArrayList<>();
    private List<Double> peakValues4 = new ArrayList<>();
    private List<Double> peakValues5 = new ArrayList<>();
    private List<Double> peakValues6 = new ArrayList<>();
    private List<Double> peakValues7 = new ArrayList<>();
    private List<Double> peakValues8 = new ArrayList<>();

    public void create_example_level_surface(Polygon polygon){

        polygon.setStroke(Color.MIDNIGHTBLUE);
        polygon.setFill(Color.GREY);

        polygon.getPoints().addAll(peakValues1);
        polygon.getPoints().addAll(peakValues2);
        polygon.getPoints().addAll(peakValues3);
        polygon.getPoints().addAll(peakValues4);
        polygon.getPoints().addAll(peakValues5);
        polygon.getPoints().addAll(peakValues6);
        polygon.getPoints().addAll(peakValues7);
        polygon.getPoints().addAll(peakValues8);

/*        polygon.getPoints().addAll(0.0,0.0,0.0,-100.0,40.0,-100.0,40.0,0.0);
        polygon.getPoints().addAll(40.0,-100.0,60.0,-110.0,65.0,-115.0,70.0,-120.0,80.0,-100.0,80.0,0.0);
        polygon.getPoints().addAll(80.0,-100.0,150.0,-100.0,150.0,0.0);
        polygon.getPoints().addAll(150.0,-100.0,160.0,-120.0,165.0,-115.0,170.0,-100.0,170.0,0.0);
        polygon.getPoints().addAll(170.0,-100.0,190.0,-130.0,200.0,-140.0,220.0,-130.0,250.0,-100.0,250.0,0.0);
        polygon.getPoints().addAll(250.0,-100.0,280.0,-155.0,310.0,-170.0,340.0,-190.0,360.0,-200.0,400.0,-180.0,
                                     440.0,-150.0,480.0,-120.0,500.0,-100.0,500.0,0.0);
        polygon.getPoints().addAll(500.0,-100.0,590.0,-100.0,590.0,0.0);
        polygon.getPoints().addAll(590.0,-100.0,600.0,-110.0,610.0,-120.0,620.0,-130.0,630.0,-140.0,650.0,-135.0,
                                   670.0,-125.0,680.0,-115.0, 714.0,-100.0, 714.0,0.0);*/



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
