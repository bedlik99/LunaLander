package mainWindow.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StartGameWindowController {


    @FXML
    private RadioButton mediumDiff;

    @FXML
    private ToggleGroup diffLvLGroup;

    private int difficultyId;

    public void initialize() {
        mediumDiff.fire();
        difficultyId = 1;


        diffLvLGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldV, Toggle newV) {
                if (diffLvLGroup.getToggles().get(0) == newV) {
                    //System.out.println("Latwy poziom trudnosci");
                    difficultyId = 0;
                }
                if (diffLvLGroup.getToggles().get(1) == newV) {
                    //System.out.println("Sredni poziom trudnosci");
                    difficultyId = 1;
                }
                if (diffLvLGroup.getToggles().get(2) == newV) {
                   // System.out.println("Trudny poziom trudnosci");
                    difficultyId = 2;
                }
            }
        });


    }


    public int getDifficultyId() {
        return difficultyId;
    }

}
