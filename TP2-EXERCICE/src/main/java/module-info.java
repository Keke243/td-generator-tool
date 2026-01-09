module dirogue.example {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens dirogue.example.controllers to javafx.fxml;
    exports dirogue.example.view;
    exports dirogue.example.controllers;
    exports dirogue.example;
    exports dirogue.example.code_squelette;
    exports dirogue.example.rencontre;
}
