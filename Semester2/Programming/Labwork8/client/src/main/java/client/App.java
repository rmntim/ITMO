package client;

import client.controllers.AuthController;
import client.controllers.EditController;
import client.controllers.MainController;
import client.network.UDPClient;
import client.utility.Localizator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    private static final int PORT = 23586;
    public static final Logger logger = LoggerFactory.getLogger("ClientLogger");
    public static UDPClient client;

    private Stage mainStage;
    private Localizator localizator;

    public static void main(String[] args) {
        try {
            client = new UDPClient(InetAddress.getLocalHost(), PORT);
            launch(args);
        } catch (IOException e) {
            logger.info("Can't connect to server.", e);
        }
    }

    @Override
    public void start(Stage stage) {
        localizator = new Localizator(ResourceBundle.getBundle("locales/gui", new Locale("ru", "RU")));
        mainStage = stage;
        authStage();
    }

    public void startMain() {
        var mainLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        var mainRoot = loadFxml(mainLoader);

        var editLoader = new FXMLLoader(getClass().getResource("/edit.fxml"));
        var editRoot = loadFxml(editLoader);

        var editScene = new Scene(editRoot);
        var editStage = new Stage();
        editStage.setScene(editScene);
        editStage.setResizable(false);
        editStage.setTitle("Products");
        EditController editController = editLoader.getController();

        editController.setStage(editStage);
        editController.setLocalizator(localizator);

        MainController mainController = mainLoader.getController();
        mainController.setEditController(editController);
        mainController.setContext(client, localizator, mainStage);
        mainController.setAuthCallback(this::authStage);

        mainStage.setScene(new Scene(mainRoot));
        mainController.setRefreshing(true);
        mainController.refresh();
        mainStage.show();
    }

    private void authStage() {
        var authLoader = new FXMLLoader(getClass().getResource("/auth.fxml"));
        var authRoot = loadFxml(authLoader);
        AuthController authController = authLoader.getController();
        authController.setCallback(this::startMain);
        authController.setClient(client);
        authController.setLocalizator(localizator);

        mainStage.setScene(new Scene(authRoot));
        mainStage.setTitle("Products");
        mainStage.setResizable(false);
        mainStage.show();
    }

    private Parent loadFxml(FXMLLoader loader) {
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            logger.error("Can't load {}", loader, e);
            System.exit(1);
        }
        return parent;
    }
}
