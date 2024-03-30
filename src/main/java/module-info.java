module IACreator.main {
    requires java.desktop;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires org.apache.logging.log4j;

    exports com.alazeprt.iac;
    opens com.alazeprt.iac to javafx.fxml;

    exports com.alazeprt.iac.ui;
    opens com.alazeprt.iac.ui to javafx.fxml;

    exports com.alazeprt.iac.utils;
}