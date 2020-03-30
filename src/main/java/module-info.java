module LunaLander {
    requires javafx.fxml;
    requires javafx.controls;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires json.simple;

    opens ConfigClasses;
    opens controlWindow;
    opens DataModelJSON;

}