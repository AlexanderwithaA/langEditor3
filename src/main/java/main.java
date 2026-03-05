import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

//main should contain only boilerplate and logic related code, javafx window building, and IO (ie jar scanning) should be done in other classes
public class main extends Application {

    private Stage primaryStage;
    private final FileCollection fileCollection = new FileCollection();
    private final WindowBuilder windowBuilder = new WindowBuilder(fileCollection);
    private final IOmanager iOmanager = new IOmanager(fileCollection, windowBuilder, primaryStage);
    //private String[] manifest;

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

        windowBuilder.getJarSelectionBuilder().getJarSelectButton().setOnAction(e -> {
            try {
                iOmanager.scanJar();
                windowBuilder.getJarSelectionBuilder().setExportAllowance(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        windowBuilder.getJarSelectionBuilder().getJarExportButton().setOnAction(e -> {
            Optional<String[]> result;
            result = windowBuilder.exportWindow();

            //check if result is present, if it is, packItUp should be given a title, ID, region, and credits to turn into the manifest
            if(result.isPresent()) {

                try {
                    fileCollection.packItUp(result.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


}
