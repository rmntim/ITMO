module ru.rmntim.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.rmntim.client to javafx.fxml;
    exports ru.rmntim.client;
}