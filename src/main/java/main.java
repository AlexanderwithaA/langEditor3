import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

//main should contain only boilerplate and logic related code, javafx window building, and IO (ie jar scanning) should be done in other classes
public class main extends Application {

    private Stage primaryStage;
    private final FileCollection fileCollection = new FileCollection();
    private final WindowBuilder windowBuilder = new WindowBuilder(fileCollection);
    private final IOmanager iOmanager = new IOmanager(fileCollection, windowBuilder, primaryStage);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.primaryStage.setTitle("BTA Language Pack Editor");

        Scene mainPanel = windowBuilder.baseLayout();
        this.primaryStage.setScene(mainPanel);
        this.primaryStage.show();

//        Dialog<String[]> exportDialog = new Dialog<>();
//        exportDialog.setTitle("Configure Manifest");
//
//        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
//        TextField title = new TextField();
//        TextField identifier = new TextField();
//        TextField regionCode = new TextField();
//        TextField credits = new TextField();
//
//        regionCode.setPromptText("en_US, ru_RU, zn_CH...");
//        identifier.setPromptText("my_language_pack");
//        credits.setPromptText("Comma separated list");
//        boolean confirmDisable = false;
//
//        GridPane dialogPane = new GridPane();
//        dialogPane.add(new Label("Title"), 0,0);
//        dialogPane.add(new Label("ID"), 0,1);
//        dialogPane.add(new Label("Region"), 0,2);
//        dialogPane.add(new Label("Credits"), 0,3);
//        dialogPane.add(title, 1,0);
//        dialogPane.add(identifier,1,1);
//        dialogPane.add(regionCode,1,2);
//        dialogPane.add(credits,1,3);
//
//        exportDialog.getDialogPane().getChildren().add(dialogPane);
//        exportDialog.getDialogPane().lookupButton(confirm).setDisable(confirmDisable);

        //button handler

        windowBuilder.getJarSelectionBuilder().getJarSelectButton().setOnAction(e -> {
            try {
                iOmanager.scanJar();
                windowBuilder.getJarSelectionBuilder().setExportAllowance(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        windowBuilder.getJarSelectionBuilder().getJarExportButton().setOnAction(e -> {
            String[] temp = {"a","b","c"};
            windowBuilder.exportWindow();
            try {
                fileCollection.packItUp(temp);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


}
