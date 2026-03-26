package org.wildedit.windowComponents;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

public class ExportDialog extends Dialog<String[]> {
    private final ButtonType saveButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final boolean[] checkArray = {false,false,false,false,false}; // this method sucks and I haven't even completed it yet
    private String exportLocation;
    private final StringProperty labelText = new SimpleStringProperty();

    public ExportDialog() {
        setTitle("Configure Manifest");

        // dialog contents
        StackPane contentPane = new StackPane();
        contentPane.setPrefSize(512, 384);

        TextField langPackTitle = new TextField();
        TextField langPackID = new TextField();
        ComboBox<String> languageCombox = new ComboBox<>();
        ComboBox<String> countryCombox = new ComboBox<>();
        ListView<String> credits = new ListView<>();

        languageCombox.getItems().addAll(Locale.getISOLanguages());
        countryCombox.getItems().addAll(Locale.getISOCountries());

        Button locationPicker = new Button();
        locationPicker.textProperty().bind(labelText);
        labelText.set("No export location Selected...");

        //gridpane containing all the elements
        GridPane gridpane = new GridPane(5,5);
        gridpane.add(new Label("Title"), 0,0);
        gridpane.add(new Label("ID"), 0,1);
        gridpane.add(new Label("Locale"), 0,2);
        gridpane.add(new Label("Credits"), 0,3);
        gridpane.add(langPackTitle, 1,0);
        gridpane.add(langPackID,1,1);
        gridpane.add(languageCombox,1,2);
        gridpane.add(countryCombox,2,2);
        gridpane.add(credits,1,3);
        gridpane.add(locationPicker,0,4);

        //gridpane.prefWidthProperty().bind(contentPane.widthProperty());
        GridPane.setHgrow(gridpane, Priority.ALWAYS);
        GridPane.setFillWidth(gridpane,true);
        gridpane.gridLinesVisibleProperty().set(true);

        GridPane.setColumnSpan(locationPicker, GridPane.REMAINING);
        GridPane.setColumnSpan(langPackTitle, 2);
        GridPane.setColumnSpan(langPackID, 2);
        GridPane.setColumnSpan(credits, 2);

        contentPane.getChildren().addAll(gridpane);
        getDialogPane().setContent(contentPane);

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
                checkArray[4] = false;
            } else {
                labelText.set("Export Location: " + exportLocation);
                checkArray[4] = true;
            }

            if(!exportLocation.endsWith("/")) {
                exportLocation = exportLocation + "/";
            }
        });

        // Set the result converter
        setResultConverter(buttonType -> {

            if (buttonType == saveButtonType) {

                if(Arrays.equals(checkArray, new boolean[]{true, true, true, true, true})) {
                    // pack inputs into array on save, zip gets made after this
                    return new String[]{"empty","empty","empty","empty"};//String[]{title.getCharacters().toString(),identifier.getCharacters().toString(), "localeCombox.getCharacters().toString()",credits.getCharacters().toString(), exportLocation};
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All fields must be filled!");
                    alert.showAndWait();
                    System.out.println(Arrays.toString(checkArray));
                }
            }
            // just do nothing ig
            return null;
        });
    }
}