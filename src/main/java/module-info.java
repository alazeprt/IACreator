module IACreator.main {
    requires java.desktop;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    exports com.alazeprt.iac;
    opens com.alazeprt.iac to javafx.fxml;
    exports com.alazeprt.iac.ui;
    opens com.alazeprt.iac.ui to javafx.fxml;
}