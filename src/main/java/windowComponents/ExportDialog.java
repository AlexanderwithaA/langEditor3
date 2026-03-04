package windowComponents;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ExportDialog extends Dialog<String[]> {
    private final ButtonType saveButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public ExportDialog() {
        setTitle("Configure Manifest");

        // dialog contents
        StackPane contentPane = new StackPane();

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

        contentPane.getChildren().addAll(gridpane);
        getDialogPane().setContent(contentPane);

        // Add buttons to the dialog
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Set the result converter
        setResultConverter(buttonType -> {

            if (buttonType == saveButtonType) {

                // pack inputs into array on save, zip gets made after this
                return new String[]{title.getCharacters().toString(),identifier.getCharacters().toString(),regionCode.getCharacters().toString(),credits.getCharacters().toString()};

            } else {

                // just do nothing ig
                return null;

            }

        });

        while(true) {
            if(title.)
        }
    }
}