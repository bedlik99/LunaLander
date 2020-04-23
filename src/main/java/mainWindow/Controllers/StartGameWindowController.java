package mainWindow.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StartGameWindowController {

    @FXML
    private TextField port;

    @FXML
    private TextField adressIP;

    @FXML
    private CheckBox offlineCheckBox;

    @FXML
    private RadioButton easyDiff, mediumDiff, hardDiff;

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
                    System.out.println("Latwy poziom trudnosci");
                    difficultyId = 0;
                }
                if (diffLvLGroup.getToggles().get(1) == newV) {
                    System.out.println("Sredni poziom trudnosci");
                    difficultyId = 1;
                }
                if (diffLvLGroup.getToggles().get(2) == newV) {
                    System.out.println("Trudny poziom trudnosci");
                    difficultyId = 2;
                }
            }
        });

        offlineCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldV, Boolean newV) {
                if (newV) {
                    port.setDisable(true);
                    adressIP.setDisable(true);
                } else {
                    port.setDisable(false);
                    adressIP.setDisable(false);
                }
            }
        });


    }


    public void processResults() {
        String portNumber = port.getText().trim();
        String numberIP = adressIP.getText().trim();

        if (offlineCheckBox.isSelected()) {
            System.out.println("Grasz offline!");
        } else if (portNumber.isEmpty() && numberIP.isEmpty()) {
            System.out.println("Nie podales danych hosta!");
        } else if (!portNumber.isEmpty() && numberIP.isEmpty()) {
            System.out.println("Nie podano adresu IP!");
        } else if (portNumber.isEmpty()) {
            System.out.println("Nie podano numeru portu!");
        } else {
            System.out.println("Grasz online!");
            System.out.println("Numer portu: " + portNumber);
            System.out.println("Adres IP: " + numberIP);
        }


    }

    public int getDifficultyId() {
        return difficultyId;
    }

    public TextField getPort() {
        return port;
    }

    public TextField getAdressIP() {
        return adressIP;
    }

    public CheckBox getOfflineCheckBox() {
        return offlineCheckBox;
    }
}
