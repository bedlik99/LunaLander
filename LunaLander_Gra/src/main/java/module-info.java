module proze20l_bedlinski_karp {

    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires json.simple;
    requires javafx.graphics;

    opens ConfigClasses;
    opens mainWindow;
    opens DataModelJSON;
    opens ConfigClasses.LevelModelClass;
    opens mainWindow.Controllers;

}