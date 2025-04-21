module edu.slu.cs.Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens edu.slu.cs.controller to javafx.fxml;

    exports edu.slu.cs;
    exports edu.slu.cs.controller;
    exports edu.slu.cs.model;
}