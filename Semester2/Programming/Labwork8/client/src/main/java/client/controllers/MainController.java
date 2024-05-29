package client.controllers;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.script.ScriptExecutor;
import client.ui.DialogManager;
import client.utility.DragonPresenter;
import client.utility.Localizator;
import common.domain.Dragon;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;
import javafx.animation.FillTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainController {
    private Localizator localizator;
    private UDPClient client;

    private Runnable authCallback;
    private volatile boolean isRefreshing = false;

    private List<Dragon> collection;

    private final HashMap<String, Locale> localeMap = new HashMap<>();

    private final HashMap<String, Color> colorMap = new HashMap<>();
    private final HashMap<Integer, Label> infoMap = new HashMap<>();
    private final Random random = new Random();

    private EditController editController;
    private Stage stage;

    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label userLabel;

    @FXML
    private Button helpButton;
    @FXML
    private Button infoButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeByIdButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button executeScriptButton;
    @FXML
    private Button addIfMaxButton;
    @FXML
    private Button addIfMinButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button logoutButton;

    @FXML
    private Tab tableTab;
    @FXML
    private TableView<Dragon> tableTable;

    @FXML
    private TableColumn<Dragon, String> ownerColumn;
    @FXML
    private TableColumn<Dragon, Integer> idColumn;
    @FXML
    private TableColumn<Dragon, String> nameColumn;
    @FXML
    private TableColumn<Dragon, Float> xColumn;
    @FXML
    private TableColumn<Dragon, Float> yColumn;
    @FXML
    private TableColumn<Dragon, String> dateColumn;
    @FXML
    private TableColumn<Dragon, Long> ageColumn;
    @FXML
    private TableColumn<Dragon, String> colorColumn;
    @FXML
    private TableColumn<Dragon, String> typeColumn;
    @FXML
    private TableColumn<Dragon, String> characterColumn;

    @FXML
    private TableColumn<Dragon, Double> headEyeCountColumn;

    @FXML
    private Tab visualTab;
    @FXML
    private AnchorPane visualPane;

    @FXML
    public void initialize() {
        localeMap.put("Русский", new Locale("ru_RU"));
        localeMap.put("English(AU)", new Locale("en_AU"));
        localeMap.put("Eesti", new Locale("et_EE"));
        localeMap.put("Polski", new Locale("pl_PL"));

        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
        languageComboBox.setStyle("-fx-font: 13px \"Sergoe UI\";");
        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(newValue)));
            changeLanguage();
        });

        ownerColumn.setCellValueFactory(dragon -> new SimpleStringProperty(dragon.getValue().creator().toString()));
        idColumn.setCellValueFactory(dragon -> new SimpleIntegerProperty(dragon.getValue().id()).asObject());
        nameColumn.setCellValueFactory(dragon -> new SimpleStringProperty(dragon.getValue().name()));
        xColumn.setCellValueFactory(dragon -> new SimpleFloatProperty(dragon.getValue().coordinates().x()).asObject());
        yColumn.setCellValueFactory(dragon -> new SimpleFloatProperty(dragon.getValue().coordinates().y()).asObject());
        dateColumn.setCellValueFactory(dragon -> new SimpleStringProperty(localizator.getDate(dragon.getValue().creationDate())));
        ageColumn.setCellValueFactory(dragon -> new SimpleLongProperty(dragon.getValue().age()).asObject());
        colorColumn.setCellValueFactory(dragon -> new SimpleStringProperty(dragon.getValue().color().toString()));
        typeColumn.setCellValueFactory(dragon -> new SimpleStringProperty(dragon.getValue().type().toString()));
        characterColumn.setCellValueFactory(dragon -> new SimpleStringProperty(dragon.getValue().character().toString()));

        headEyeCountColumn.setCellValueFactory(dragon -> {
            if (dragon.getValue().head() != null) {
                return new SimpleDoubleProperty(dragon.getValue().head().eyesCount()).asObject();
            }
            return null;
        });

        tableTable.setRowFactory(tableView -> {
            var row = new TableRow<Dragon>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    doubleClickUpdate(row.getItem());
                }
            });
            return row;
        });

        visualTab.setOnSelectionChanged(event -> visualise(false));
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void logout() {
        SessionHandler.setCurrentUser(null);
        SessionHandler.setCurrentLanguage("Русский");
        setRefreshing(false);
        authCallback.run();
    }

    @FXML
    public void help() {
        try {
            var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest(SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            DialogManager.createAlert(localizator.getKeyString("Help"), localizator.getKeyString("HelpResult"), Alert.AlertType.INFORMATION, true);
        } catch (APIException | ErrorResponseException | IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    public void info() {
        try {
            var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest(SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            var formatted = MessageFormat.format(localizator.getKeyString("InfoResult"), response.type, response.size, localizator.getDate(response.lastSaveTime), localizator.getDate(response.lastInitTime));
            DialogManager.createAlert(localizator.getKeyString("Info"), formatted, Alert.AlertType.INFORMATION, true);
        } catch (APIException | ErrorResponseException | IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    public void add() {
        editController.clear();
        editController.show();
        var dragon = editController.getDragon();
        if (dragon != null) {
            dragon = dragon.copy(dragon.id(), SessionHandler.getCurrentUser());

            try {
                var response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(dragon, SessionHandler.getCurrentUser()));
                if (response.getError() != null && !response.getError().isEmpty()) {
                    throw new APIException(response.getError());
                }

                loadCollection();
                DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddResult"), Alert.AlertType.INFORMATION, false);
            } catch (APIException | ErrorResponseException e) {
                DialogManager.alert("AddErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    @FXML
    public void update() {
        Optional<String> input = DialogManager.createDialog(localizator.getKeyString("Update"), "ID:");
        if (input.isPresent() && !input.get().isEmpty()) {
            try {
                var dragon = getDragon(input.orElse(""));
                doubleClickUpdate(dragon, false);
            } catch (NumberFormatException e) {
                DialogManager.alert("NumberFormatException", localizator);
            } catch (BadOwnerException e) {
                DialogManager.alert("BadOwnerError", localizator);
            } catch (NotFoundException e) {
                DialogManager.alert("NotFoundException", localizator);
            }
        }
    }

    private Dragon getDragon(String input) throws NotFoundException, BadOwnerException {
        var id = Integer.parseInt(input);
        var dragon = collection.stream().filter(d -> d.id() == id).findAny().orElse(null);
        if (dragon == null) throw new NotFoundException();
        if (dragon.creator().id() != SessionHandler.getCurrentUser().id()) throw new BadOwnerException();
        return dragon;
    }

    @FXML
    public void removeById() {
        Optional<String> input = DialogManager.createDialog(localizator.getKeyString("RemoveByID"), "ID: ");
        if (input.isPresent() && !input.get().isEmpty()) {
            try {
                var id = getDragon(input.orElse("")).id();
                var response = (RemoveByIdResponse) client.sendAndReceiveCommand(new RemoveByIdRequest(id, SessionHandler.getCurrentUser()));
                if (response.getError() != null && !response.getError().isEmpty()) {
                    throw new APIException(response.getError());
                }

                loadCollection();
                DialogManager.createAlert(localizator.getKeyString("RemoveByID"), localizator.getKeyString("RemoveByIDSuc"), Alert.AlertType.INFORMATION, false);
            } catch (APIException | ErrorResponseException e) {
                DialogManager.alert("RemoveByIDErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            } catch (NumberFormatException e) {
                DialogManager.alert("NumberFormatException", localizator);
            } catch (BadOwnerException e) {
                DialogManager.alert("BadOwnerError", localizator);
            } catch (NotFoundException e) {
                DialogManager.alert("NotFoundException", localizator);
            }
        }
    }

    @FXML
    public void clear() {
        try {
            var response = (ClearResponse) client.sendAndReceiveCommand(new ClearRequest(SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            loadCollection();
            DialogManager.createAlert(localizator.getKeyString("Clear"), localizator.getKeyString("ClearSuc"), Alert.AlertType.INFORMATION, false);
        } catch (APIException | ErrorResponseException e) {
            DialogManager.alert("ClearErr", localizator);
        } catch (IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    public void executeScript() {
        var chooser = new FileChooser();
        chooser.setInitialDirectory(new File("."));
        var file = chooser.showOpenDialog(stage);
        if (file != null) {
            var result = (new ScriptExecutor(this, localizator)).run(file.getAbsolutePath());
            if (result == ScriptExecutor.ExitCode.ERROR) {
                DialogManager.alert("ScriptExecutionErr", localizator);
            } else {
                DialogManager.info("ScriptExecutionSuc", localizator);
            }
        }
    }

    @FXML
    public void addIfMax() {
        editController.clear();
        editController.show();
        var product = editController.getDragon();
        if (product != null) {
            product = product.copy(product.id(), SessionHandler.getCurrentUser());

            try {
                var response = (AddIfMaxResponse) client.sendAndReceiveCommand(new AddIfMaxRequest(product, SessionHandler.getCurrentUser()));
                if (response.getError() != null && !response.getError().isEmpty()) {
                    throw new APIException(response.getError());
                }

                loadCollection();
                if (response.isAdded) {
                    DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddResult"), Alert.AlertType.INFORMATION, false);
                } else {
                    DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddNotMax"), Alert.AlertType.INFORMATION, false);
                }
            } catch (APIException | ErrorResponseException e) {
                DialogManager.alert("AddErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    @FXML
    public void addIfMin() {
        editController.clear();
        editController.show();
        var product = editController.getDragon();
        if (product != null) {
            product = product.copy(product.id(), SessionHandler.getCurrentUser());

            try {
                var response = (AddIfMinResponse) client.sendAndReceiveCommand(new AddIfMinRequest(product, SessionHandler.getCurrentUser()));
                if (response.getError() != null && !response.getError().isEmpty()) {
                    throw new APIException(response.getError());
                }

                loadCollection();
                if (response.isAdded) {
                    DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddResult"), Alert.AlertType.INFORMATION, false);
                } else {
                    DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddNotMin"), Alert.AlertType.INFORMATION, false);
                }
            } catch (APIException | ErrorResponseException e) {
                DialogManager.alert("AddErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    public void refresh() {
        var refresher = new Thread(() -> {
            while (isRefreshing()) {
                Platform.runLater(this::loadCollection);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread was interrupted, Failed to complete operation");
                }
            }
        });
        refresher.start();
    }

    public void visualise(boolean refresh) {
        visualPane.getChildren().clear();
        infoMap.clear();

        for (var dragon : tableTable.getItems()) {
            var creatorName = dragon.creator().name();

            if (!colorMap.containsKey(creatorName)) {
                var r = random.nextDouble();
                var g = random.nextDouble();
                var b = random.nextDouble();
                if (Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) < 0.6) {
                    r += (1 - r) / 1.4;
                    g += (1 - g) / 1.4;
                    b += (1 - b) / 1.4;
                }
                colorMap.put(creatorName, Color.color(r, g, b));
            }

            var size = Math.min(125, Math.max(75, dragon.age() * 2) / 2);

            var circle = new Circle(size, colorMap.get(creatorName));
            double x = Math.abs(dragon.coordinates().x());
            while (x >= 720) {
                x = x / 10;
            }
            double y = Math.abs(dragon.coordinates().y());
            while (y >= 370) {
                y = y / 3;
            }
            if (y < 100) y += 125;

            var id = new Text('#' + String.valueOf(dragon.id()));
            var info = new Label(new DragonPresenter(localizator).describe(dragon));

            info.setVisible(false);
            circle.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    doubleClickUpdate(dragon);
                }
            });

            circle.setOnMouseEntered(mouseEvent -> {
                id.setVisible(false);
                info.setVisible(true);
                circle.setFill(colorMap.get(creatorName).brighter());
            });

            circle.setOnMouseExited(mouseEvent -> {
                id.setVisible(true);
                info.setVisible(false);
                circle.setFill(colorMap.get(creatorName));
            });

            id.setFont(Font.font("Segoe UI", size / 1.4));
            info.setStyle("-fx-background-color: white; -fx-border-color: #c0c0c0; -fx-border-width: 2");
            info.setFont(Font.font("Segoe UI", 15));

            visualPane.getChildren().add(circle);
            visualPane.getChildren().add(id);

            infoMap.put(dragon.id(), info);
            if (!refresh) {
                var path = new Path();
                path.getElements().add(new MoveTo(-500, -150));
                path.getElements().add(new HLineTo(x));
                path.getElements().add(new VLineTo(y));
                id.translateXProperty().bind(circle.translateXProperty().subtract(id.getLayoutBounds().getWidth() / 2));
                id.translateYProperty().bind(circle.translateYProperty().add(id.getLayoutBounds().getHeight() / 4));
                info.translateXProperty().bind(circle.translateXProperty().add(circle.getRadius()));
                info.translateYProperty().bind(circle.translateYProperty().subtract(120));
                var transition = new PathTransition();
                transition.setDuration(Duration.millis(750));
                transition.setNode(circle);
                transition.setPath(path);
                transition.setOrientation(PathTransition.OrientationType.NONE);
                transition.play();
            } else {
                circle.setCenterX(x);
                circle.setCenterY(y);
                info.translateXProperty().bind(circle.centerXProperty().add(circle.getRadius()));
                info.translateYProperty().bind(circle.centerYProperty().subtract(120));
                id.translateXProperty().bind(circle.centerXProperty().subtract(id.getLayoutBounds().getWidth() / 2));
                id.translateYProperty().bind(circle.centerYProperty().add(id.getLayoutBounds().getHeight() / 4));
                var darker = new FillTransition(Duration.millis(750), circle);
                darker.setFromValue(colorMap.get(creatorName));
                darker.setToValue(colorMap.get(creatorName).darker().darker());
                var brighter = new FillTransition(Duration.millis(750), circle);
                brighter.setFromValue(colorMap.get(creatorName).darker().darker());
                brighter.setToValue(colorMap.get(creatorName));
                var transition = new SequentialTransition(darker, brighter);
                transition.play();
            }
        }

        for (var id : infoMap.keySet()) {
            visualPane.getChildren().add(infoMap.get(id));
        }
    }

    private void loadCollection() {
        try {
            var response = (ShowResponse) client.sendAndReceiveCommand(new ShowRequest(SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            setCollection(response.dragons);
            visualise(true);
        } catch (SocketTimeoutException e) {
            DialogManager.alert("RefreshLost", localizator);
        } catch (APIException | ErrorResponseException e) {
            DialogManager.alert("RefreshFailed", localizator);
        } catch (IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    private void doubleClickUpdate(Dragon dragon) {
        doubleClickUpdate(dragon, true);
    }

    private void doubleClickUpdate(Dragon dragon, boolean ignoreAnotherUser) {
        if (ignoreAnotherUser && dragon.creator().id() != SessionHandler.getCurrentUser().id()) return;

        editController.fill(dragon);
        editController.show();

        var updatedDragon = editController.getDragon();
        if (updatedDragon != null) {
            updatedDragon = updatedDragon.copy(dragon.id(), SessionHandler.getCurrentUser());

            if (dragon.head() != null && updatedDragon.head() != null) {
                updatedDragon.head().setEyesCount(dragon.head().eyesCount());
            }

            try {
                if (!updatedDragon.validate()) throw new InvalidFormException();

                var response = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(dragon.id(), updatedDragon, SessionHandler.getCurrentUser()));
                if (response.getError() != null && !response.getError().isEmpty()) {
                    if (response.getError().contains("BAD_OWNER")) {
                        throw new BadOwnerException();
                    }
                    throw new APIException(response.getError());
                }

                loadCollection();
                DialogManager.createAlert(localizator.getKeyString("Update"), localizator.getKeyString("UpdateSuc"), Alert.AlertType.INFORMATION, false);
            } catch (APIException | ErrorResponseException e) {
                DialogManager.createAlert(localizator.getKeyString("Error"), localizator.getKeyString("UpdateErr"), Alert.AlertType.ERROR, false);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            } catch (InvalidFormException e) {
                DialogManager.createAlert(localizator.getKeyString("Update"), localizator.getKeyString("InvalidDragon"), Alert.AlertType.INFORMATION, false);
            } catch (BadOwnerException e) {
                DialogManager.alert("BadOwnerError", localizator);
            }
        }
    }

    public void changeLanguage() {
        userLabel.setText(localizator.getKeyString("UserLabel") + " " + SessionHandler.getCurrentUser().name());

        exitButton.setText(localizator.getKeyString("Exit"));
        logoutButton.setText(localizator.getKeyString("LogOut"));
        helpButton.setText(localizator.getKeyString("Help"));
        infoButton.setText(localizator.getKeyString("Info"));
        addButton.setText(localizator.getKeyString("Add"));
        updateButton.setText(localizator.getKeyString("Update"));
        removeByIdButton.setText(localizator.getKeyString("RemoveByID"));
        clearButton.setText(localizator.getKeyString("Clear"));
        executeScriptButton.setText(localizator.getKeyString("ExecuteScript"));
        addIfMaxButton.setText(localizator.getKeyString("AddIfMax"));
        addIfMinButton.setText(localizator.getKeyString("AddIfMin"));

        tableTab.setText(localizator.getKeyString("TableTab"));
        visualTab.setText(localizator.getKeyString("VisualTab"));

        ownerColumn.setText(localizator.getKeyString("Owner"));
        nameColumn.setText(localizator.getKeyString("Name"));
        dateColumn.setText(localizator.getKeyString("CreationDate"));
        ageColumn.setText(localizator.getKeyString("Age"));
        colorColumn.setText(localizator.getKeyString("Color"));
        typeColumn.setText(localizator.getKeyString("DragonType"));
        characterColumn.setText(localizator.getKeyString("DragonCharacter"));

        headEyeCountColumn.setText(localizator.getKeyString("DragonHeadEyesCount"));

        editController.changeLanguage();

        loadCollection();
    }

    public void setCollection(List<Dragon> collection) {
        this.collection = collection;
        tableTable.setItems(FXCollections.observableArrayList(collection));
    }

    public void setAuthCallback(Runnable authCallback) {
        this.authCallback = authCallback;
    }

    public void setContext(UDPClient client, Localizator localizator, Stage stage) {
        this.client = client;
        this.localizator = localizator;
        this.stage = stage;

        languageComboBox.setValue(SessionHandler.getCurrentLanguage());
        localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(SessionHandler.getCurrentLanguage())));
        changeLanguage();

        userLabel.setText(localizator.getKeyString("UserLabel") + " " + SessionHandler.getCurrentUser().name());
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public void setEditController(EditController editController) {
        this.editController = editController;
        editController.changeLanguage();
    }
}
