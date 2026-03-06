package windowComponents;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Arrays;

public class ExportDialog extends Dialog<String[]> {
    private final ButtonType saveButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    private boolean[] checkArray = {false,false,false,false}; // this method sucks and I haven't even completed it yet
    private String exportLocation;
    private final StringProperty labelText = new SimpleStringProperty();

    public ExportDialog() {
        setTitle("Configure Manifest");

        // dialog contents
        StackPane contentPane = new StackPane();
        VBox vbox = new VBox();

        contentPane.setPrefSize(300, 200);

        TextField title = new TextField();
        TextField identifier = new TextField();
        TextField regionCode = new TextField();
        TextField credits = new TextField();

        //I should get a region code picker. Also, this does not determine the file path of the language pack atm, which is an issue, probably.
        regionCode.setPromptText("en_US, ru_RU, zn_CH...");
        identifier.setPromptText("my_language_pack");
        credits.setPromptText("Comma separated list");

        GridPane gridpane = new GridPane();
        gridpane.add(new Label("Title"), 0,0);
        gridpane.add(new Label("ID"), 0,1);
        gridpane.add(new Label("Region"), 0,2);
        gridpane.add(new Label("Credits"), 0,3);
        gridpane.add(title, 1,0);
        gridpane.add(identifier,1,1);
        gridpane.add(regionCode,1,2);
        gridpane.add(credits,1,3);

        gridpane.setVgap(5);
        gridpane.setHgap(5);

        Button locationPicker = new Button();
        locationPicker.textProperty().bind(labelText);
        labelText.set("No export location Selected...");

        vbox.getChildren().addAll(gridpane,locationPicker);

        contentPane.getChildren().addAll(vbox);
        getDialogPane().setContent(contentPane);

        // Add buttons to the dialog
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);


        title.setOnAction(e -> {
             checkArray[0] = title.getCharacters().isEmpty();
        });

        identifier.setOnAction(e -> {
            checkArray[1] = identifier.getCharacters().isEmpty();
        });

        regionCode.setOnAction(e -> {
            checkArray[2] = regionCode.getCharacters().isEmpty();
        });

        credits.setOnAction(e -> {
            checkArray[3] = credits.getCharacters().isEmpty();
        });

        locationPicker.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            exportLocation = directoryChooser.showDialog(null).getPath(); // I mean, in the docs it says, "If the owner window for the
            // directory selection dialog is set, input to all windows in the dialog's owner chain is blocked while the dialog
            // is being shown." which implies the windows can be unset. But this does not feel right

            exportLocation = exportLocation.replace("\\","/");
            if (! new File(exportLocation).exists()) {
                labelText.set("Invalid Location: " + exportLocation);
            } else {
                labelText.set("Export Location: " + exportLocation);
            }

            if(!exportLocation.endsWith("/")) {
                exportLocation = exportLocation + "/";
            }
        });

        // Set the result converter
        setResultConverter(buttonType -> {

            if (buttonType == saveButtonType) {

                if(Arrays.equals(checkArray, new boolean[]{true, true, true, true})) {
                    // pack inputs into array on save, zip gets made after this
                    return new String[]{title.getCharacters().toString(),identifier.getCharacters().toString(),regionCode.getCharacters().toString(),credits.getCharacters().toString(), exportLocation};
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