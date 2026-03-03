package windowComponents;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ExportDialog extends Dialog<String[]> {
    private final ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public ExportDialog() {
    setTitle("Configure Manifest");

    // Set the content of the dialog
    StackPane contentPane = new StackPane();

    contentPane.setPrefSize(300, 200);

    TextField title = new TextField();
    TextField identifier = new TextField();
    TextField regionCode = new TextField();
    TextField credits = new TextField();

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

    contentPane.getChildren().addAll(gridpane);

    getDialogPane().setContent(contentPane);

    // Add buttons to the dialog
    getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

    // Set the result converter
    setResultConverter(buttonType -> {

        if (buttonType == saveButtonType) {

            // Perform actions when the "Save" button is clicked
            return new String[]{title.getCharacters().toString(),identifier.getCharacters().toString(),regionCode.getCharacters().toString(),credits.getCharacters().toString()};

        } else {

            // Perform actions when the "Cancel" button is clicked or the dialog is closed
            return null;

        }

    });
    }
}