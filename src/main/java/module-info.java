module tanks {
    requires javafx.graphics;
    requires javafx.fxml;

    opens com.utriainen.controllers to javafx.fxml;

    exports com.utriainen;
}