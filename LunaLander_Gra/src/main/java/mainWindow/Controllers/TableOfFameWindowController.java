package mainWindow.Controllers;

import ConfigClasses.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class TableOfFameWindowController {

    @FXML
    private  ListView<Player> listOfScores;

    public void initialize(){
        listOfScores.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public  ListView<Player> getListOfScores() {
        return listOfScores;
    }


}
