package org.wildedit.windowComponents;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import org.controlsfx.control.SearchableComboBox;

import java.io.File;
import java.util.Locale;

public class ExportDialog extends Dialog<String[]> {
    private final ButtonType saveButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final StringProperty labelText = new SimpleStringProperty();
    private String exportLocation;
    private boolean userIdInput = false;

    public ExportDialog() {
        setTitle("Configure Manifest");

        // dialog contents
        VBox contents = new VBox();
        contents.setPrefSize(512, 384);
        contents.setSpacing(8);
        contents.setAlignment(Pos.CENTER);

        TextField langPackTitle = new TextField();
        langPackTitle.prefWidthProperty().bind(contents.widthProperty());
        TextField langPackID = new TextField();
        SearchableComboBox<String> languageCombox = new SearchableComboBox<>();
        SearchableComboBox<String> countryCombox = new SearchableComboBox<>();
//        ListView<TextField> credits = new ListView<>(creditsList);
        TextArea credits = new TextArea();
        credits.promptTextProperty().set("One contributer per line...");
        credits.wrapTextProperty().set(false);

        languageCombox.getItems().addAll(Locale.getISOLanguages());
        countryCombox.getItems().addAll(Locale.getISOCountries());

        Button locationPicker = new Button();
        locationPicker.textProperty().bind(labelText);
        labelText.set("No export location Selected...");

        //gridpane containing all the elements
        GridPane gridpane = new GridPane(5,5);
        gridpane.add(new Label("Title"), 0,0);
        gridpane.add(new Label("ID"), 0,1);
        gridpane.add(new Label("Locale"), 0,3);
        gridpane.add(new Label("Credits"), 0,4);
        gridpane.add(langPackTitle, 1,0);
        gridpane.add(langPackID,1,1);
        gridpane.add(new Label("Language Code"), 1,2);
        gridpane.add(new Label("Region Code"), 2,2);
        gridpane.add(languageCombox,1,3);
        gridpane.add(countryCombox,2,3);
        gridpane.add(credits,1,4);

        GridPane.setColumnSpan(langPackTitle, 2);
        GridPane.setColumnSpan(langPackID, 2);
        GridPane.setColumnSpan(credits, 2);

        contents.getChildren().addAll(gridpane, locationPicker);
        getDialogPane().setContent(contents);

        // Add buttons to the dialog
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        //actions
        locationPicker.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            exportLocation = directoryChooser.showDialog(null).getPath(); // I mean, in the docs it says, "If the owner window for the
            // directory selection dialog is set, input to all windows in the dialog's owner chain is blocked while the dialog
            // is being shown." which implies the windows can be unset. But this does not feel right

            exportLocation = exportLocation.replace("\\","/");
            if (!new File(exportLocation).exists()) {
                labelText.set("Invalid Location: " + exportLocation);
            } else {
                labelText.set("Export Location: " + exportLocation);
            }

            if(!exportLocation.endsWith("/")) {
                exportLocation = exportLocation + "/";
            }
        });

        langPackTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            String parsedTitle = langPackTitle.getText().replaceAll("[^a-zA-Z0-9.\\-]", "_").toLowerCase();
            if (parsedTitle.length() >= 127) {
                parsedTitle = parsedTitle.substring(0,128);
            }
            if (langPackID.getText().isEmpty() || !userIdInput) {
                userIdInput = false;
                langPackID.setText(parsedTitle);
            }
        });

        langPackID.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String parsedTitle = langPackTitle.getText().replaceAll("[^a-zA-Z0-9.\\-]", "_").toLowerCase();
            if (parsedTitle.length() >= 127) {
                parsedTitle = parsedTitle.substring(0,128);
            }
            if (langPackID.isFocused()) {
                userIdInput = true;
                System.out.println("eyo");
            } else if (langPackID.getText().isEmpty()){ //binded to focused property, idc if something here is useless now
                userIdInput = false;
                langPackID.setText(parsedTitle);
            }
        });

        // Set the result converter
        setResultConverter(buttonType -> {

            if (buttonType == saveButtonType) {

                if(exportLocation != null && !langPackTitle.getText().isBlank() && !langPackID.getText().isBlank() && !credits.getText().isBlank() && !languageCombox.getSelectionModel().getSelectedItem().isBlank() && !countryCombox.getSelectionModel().getSelectedItem().isBlank()) {
                    String[] manifest = new String[6];
                    manifest[0] = langPackTitle.getText();
                    manifest[1] = langPackID.getText();
                    manifest[2] = languageCombox.getValue() + "_" + countryCombox.getValue();
                    manifest[3] = credits.getText();
                    manifest[4] = exportLocation;
                    // pack inputs into array on save, zip gets made after this
                    return manifest;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled!");
                    alert.showAndWait();
                }
            }
            // just do nothing ig
            return null;
        });
    }
}