package mainWindow.Controllers;

import ConfigClasses.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class TableOfFameWindowController {

    @FXML
    private ListView<Player> listOfScores;

    private String listItem;


    public void setListOfScores(ListView<Player> listOfScores) {
        this.listOfScores = listOfScores;
    }
}
