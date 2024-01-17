module edu.cuny.ccny.ioannou_painting_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens edu.cuny.ccny.ioannou_painting_application to javafx.fxml;
    exports edu.cuny.ccny.ioannou_painting_application;
}