package mainWindow.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SaveResultsWindowController {

    @FXML
    private TextField nickNameField = new TextField();
    @FXML
    private Label result = new Label();

    public TextField getNickNameField() {
        return nickNameField;
    }

    public Label getResult() {
        return result;
    }

    public void setResult(double playerResult) {
        String labelRes = String.valueOf(playerResult);
        this.result.setText(labelRes);

    }





}
