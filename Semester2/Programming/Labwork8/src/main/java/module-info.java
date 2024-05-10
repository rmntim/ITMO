module ru.rmntim.lab {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.rmntim.lab to javafx.fxml;
    exports ru.rmntim.lab;
}