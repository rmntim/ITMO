package client.controllers;

import client.auth.SessionHandler;
import client.ui.DialogManager;
import client.utility.Localizator;
import common.domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class EditController {
    private Stage stage;
    private Dragon dragon;
    private Localizator localizator;

    @FXML
    private Label titleLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label colorsLabel;
    @FXML
    private Label typesLabel;
    @FXML
    private Label charactersLabel;
    @FXML
    private Label hasHead;
    @FXML
    private Label headEyeCountLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField xField;
    @FXML
    private TextField yField;
    @FXML
    private TextField ageField;
    @FXML
    private ChoiceBox<String> colorsBox;
    @FXML
    private ChoiceBox<String> typesBox;
    @FXML
    private ChoiceBox<String> charactersBox;
    @FXML
    private TextField headEyeCount;

    @FXML
    private ChoiceBox<String> hasHeadBox;

    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        cancelButton.setOnAction(event -> stage.close());
        var colors = FXCollections.observableArrayList(Arrays.stream(Color.values()).map(Enum::toString).collect(Collectors.toList()));
        colorsBox.setItems(colors);
        colorsBox.setStyle("-fx-font: 12px \"Segoe UI\";");

        var types = FXCollections.observableArrayList(Arrays.stream(DragonType.values()).map(Enum::toString).collect(Collectors.toList()));
        typesBox.setItems(types);
        typesBox.setStyle("-fx-font: 12px \"Segoe UI\";");

        var characters = FXCollections.observableArrayList(Arrays.stream(DragonCharacter.values()).map(Enum::toString).collect(Collectors.toList()));
        charactersBox.setItems(characters);
        charactersBox.setStyle("-fx-font: 12px \"Segoe UI\";");

        var hasHead = FXCollections.observableArrayList("TRUE", "FALSE");
        hasHeadBox.setItems(hasHead);
        hasHeadBox.setValue("FALSE");
        hasHeadBox.setStyle("-fx-font: 12px \"Sergoe UI\";");

        Collections.singletonList(headEyeCount).forEach(field -> field.disableProperty().bind(hasHeadBox.getSelectionModel().selectedItemProperty().isEqualTo("FALSE")));

        xField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("[-\\d]{0,11}")) {
                xField.setText(oldValue);
            } else {
                if (newValue.matches(".+-.*")) {
                    Platform.runLater(() -> xField.clear());
                } else if (newValue.length() == 10 && Long.parseLong(newValue) > Integer.MAX_VALUE || newValue.length() == 11 && Long.parseLong(newValue) < Integer.MIN_VALUE) {
                    xField.setText(oldValue);
                }
            }
        });

        yField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("[-\\d]{0,20}")) {
                yField.setText(oldValue);
            } else {
                if (newValue.matches(".+-.*")) {
                    Platform.runLater(() -> yField.clear());
                } else if (!newValue.isEmpty() && (newValue.length() == 19 && new BigInteger(newValue).compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || newValue.length() == 20 && new BigInteger(newValue).compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)) {
                    yField.setText(oldValue);
                }
            }
        });

        Collections.singletonList(ageField).forEach(field -> field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!field.isDisabled()) {
                if (!newValue.matches("\\d{0,19}")) {
                    field.setText(oldValue);
                } else {
                    if (!newValue.isEmpty() && (new BigInteger(newValue).compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || new BigInteger(newValue).compareTo(new BigInteger(String.valueOf(0))) <= 0)) {
                        field.setText(oldValue);
                    }
                }

            }
        }));
    }

    @FXML
    public void ok() {
        nameField.setText(nameField.getText().trim());
        var errors = new ArrayList<String>();

        DragonHead head = null;
        if (hasHeadBox.getValue().equals("TRUE")) {
            head = new DragonHead(Double.parseDouble(headEyeCount.getText()));
        }

        if (nameField.getText().isEmpty()) {
            errors.add("- " + localizator.getKeyString("Name") + " " + localizator.getKeyString("CannotBeEmpty"));
        }

        Color color = null;
        if (colorsBox.getValue() != null) {
            color = Color.valueOf(colorsBox.getValue());
        }

        DragonType type = null;
        if (typesBox.getValue() != null) {
            type = DragonType.valueOf(typesBox.getValue());
        }

        DragonCharacter character = null;
        if (charactersBox.getValue() != null) {
            character = DragonCharacter.valueOf(charactersBox.getValue());
        }

        if (!errors.isEmpty()) {
            DialogManager.createAlert(localizator.getKeyString("Error"), String.join("\n", errors), Alert.AlertType.ERROR, false);
        } else {
            var newDragon = new Dragon(-1, nameField.getText(), new Coordinates(Float.parseFloat(xField.getText()), Float.parseFloat(yField.getText())), ZonedDateTime.now(), Long.parseLong(ageField.getText()), color, type, character, head, SessionHandler.getCurrentUser());
            if (!newDragon.validate()) {
                DialogManager.alert("InvalidDragon", localizator);
            } else {
                dragon = newDragon;
                stage.close();
            }
        }
    }

    public Dragon getDragon() {
        var tmpDragon = dragon;
        dragon = null;
        return tmpDragon;
    }

    public void clear() {
        nameField.clear();
        xField.clear();
        yField.clear();
        ageField.clear();
        colorsBox.valueProperty().setValue(null);
        typesBox.valueProperty().setValue(null);
        charactersBox.valueProperty().setValue(null);
        hasHeadBox.valueProperty().setValue("FALSE");

        headEyeCount.clear();
    }

    public void fill(Dragon dragon) {
        nameField.setText(dragon.name());
        xField.setText(Float.toString(dragon.coordinates().x()));
        yField.setText(Float.toString(dragon.coordinates().y()));
        ageField.setText(Long.toString(dragon.age()));
        colorsBox.setValue(dragon.color().toString());
        typesBox.setValue(dragon.type().toString());
        charactersBox.setValue(dragon.character().toString());
        hasHeadBox.setValue(dragon.head() == null ? "FALSE" : "TRUE");
        if (dragon.head() != null) {
            headEyeCount.setText(dragon.head().eyesCount().toString());
        } else {
            headEyeCount.clear();
        }
    }

    public void changeLanguage() {
        titleLabel.setText(localizator.getKeyString("EditTitle"));
        nameLabel.setText(localizator.getKeyString("Name"));
        ageLabel.setText(localizator.getKeyString("Age"));
        colorsLabel.setText(localizator.getKeyString("Color"));
        typesLabel.setText(localizator.getKeyString("DragonType"));
        charactersLabel.setText(localizator.getKeyString("DragonCharacter"));
        hasHead.setText(localizator.getKeyString("DragonHead"));
        headEyeCountLabel.setText(localizator.getKeyString("DragonHeadEyesCount"));

        cancelButton.setText(localizator.getKeyString("CancelButton"));
    }

    public void show() {
        if (!stage.isShowing()) stage.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLocalizator(Localizator localizator) {
        this.localizator = localizator;
    }
}
