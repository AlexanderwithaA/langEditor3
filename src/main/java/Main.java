import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Main extends Application {

    private final FileCollection fileCollection = new FileCollection();
    private final String[] implementationVersions = {"7.3_04"};
    private static Stage primaryStage;
    private static FileChooser fileChooser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage.setTitle("BTA Language Pack Editor");

        //menu bar
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");
        MenuBar menuBar = new MenuBar(fileMenu, editMenu, helpMenu);

        ScrollPane filePane = new ScrollPane();

        Label selectedJar = new Label("Jar: No Jar Selected");
        Button selectJar = new Button("Select the BTA Jar");
        VBox jarSelectionBox = new VBox(selectedJar, selectJar);

        VBox.setMargin(jarSelectionBox, new Insets(10));
        jarSelectionBox.setSpacing(4);
        VBox.setVgrow(filePane, Priority.ALWAYS);

        VBox selectorContainer = new VBox(filePane, jarSelectionBox);


        ScrollPane contentsPane = new ScrollPane();

        SplitPane workspace = new SplitPane(selectorContainer, contentsPane);
        VBox root = new VBox(menuBar, workspace);

        VBox.setVgrow(workspace, Priority.ALWAYS);

//        contentsPane.setContent(new Label("contents"));
//        filePane.setContent(new Label("file"));

        Scene mainPanel = new Scene(root,512, 384);
        this.primaryStage.setScene(mainPanel);
        this.primaryStage.show();

        //button handler

        selectJar.setOnAction(e -> {});
    }

    private void scanJar() throws IOException {
        fileChooser.setTitle("Select Jar File For Resource Extraction");
        File file = fileChooser.showOpenDialog(primaryStage);
        JarFile jarScanner = new JarFile(file);
        Manifest manifest = new Manifest(jarScanner.getManifest());
        boolean validJar = false;

        for (String value : implementationVersions) {
            if (value.equals(manifest.getMainAttributes().getValue("Implementation-Version"))) {
                validJar = true;
                break;
            }
        }

        if (validJar) {
            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements(); ) {
                JarEntry entry = enumStructure.nextElement();
                if (entry.getName().endsWith(".lang") || entry.getName().endsWith("splashes.txt")) {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));
                    fileCollection.addFile(entry.getName(), inputReader);
                }
            }
            jarScanner.close();
        } else {
            //invalid jar warning here.
        }
    }
}
