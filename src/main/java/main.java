import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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

public class main extends Application {

    private final FileCollection fileCollection = new FileCollection();
    private final String[] implementationVersions = {"7.3_04"};
    private Stage primaryStage;
    private final FileChooser fileChooser = new FileChooser();
    private final ObservableList<Button> buttons = FXCollections.observableArrayList();
    private final StringProperty labelText = new SimpleStringProperty("Jar: No Jar Selected");
    private final ScrollPane contentsPane = new ScrollPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.primaryStage.setTitle("BTA Language Pack Editor");

        //menu bar
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");
        MenuBar menuBar = new MenuBar(fileMenu, editMenu, helpMenu);

        ListView<Button> fileList = new ListView<>(buttons);

        Label selectedJar = new Label();
        selectedJar.textProperty().bind(labelText);

        Button selectJar = new Button("Select the BTA Jar");
        Button export = new Button("Export Language Pack");
        export.setDisable(true);
        VBox jarSelectionBox = new VBox(selectedJar, selectJar, export);

        VBox.setMargin(jarSelectionBox, new Insets(10));
        jarSelectionBox.setSpacing(4);
        VBox.setVgrow(fileList, Priority.ALWAYS);

        VBox selectorContainer = new VBox(fileList, jarSelectionBox);

        //VBox contents = new VBox();

        SplitPane workspace = new SplitPane(selectorContainer, contentsPane);
        workspace.setDividerPositions((double) 2/7);
        VBox root = new VBox(menuBar, workspace);

        VBox.setVgrow(workspace, Priority.ALWAYS);

        Scene mainPanel = new Scene(root,512, 384);
        this.primaryStage.setScene(mainPanel);
        this.primaryStage.show();

        Dialog<String[]> exportDialog = new Dialog<>();
        exportDialog.setTitle("Configure Manifest");

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        TextField title = new TextField();
        TextField identifier = new TextField();
        TextField regionCode = new TextField();
        TextField credits = new TextField();

        regionCode.setPromptText("en_US, ru_RU, zn_CH...");
        identifier.setPromptText("my_language_pack");
        credits.setPromptText("Comma separated list");
        boolean confirmDisable = false;

        GridPane dialogPane = new GridPane();
        dialogPane.add(new Label("Title"), 0,0);
        dialogPane.add(new Label("ID"), 0,1);
        dialogPane.add(new Label("Region"), 0,2);
        dialogPane.add(new Label("Credits"), 0,3);
        dialogPane.add(title, 1,0);
        dialogPane.add(identifier,1,1);
        dialogPane.add(regionCode,1,2);
        dialogPane.add(credits,1,3);

        exportDialog.getDialogPane().getChildren().add(dialogPane);
        exportDialog.getDialogPane().lookupButton(confirm).setDisable(confirmDisable);

        //button handler

        selectJar.setOnAction(e -> {
            try {
                scanJar();
                export.setDisable(false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        export.setOnAction(e -> {
            String[] temp = {"a","b","c"};
            exportDialog.showAndWait();
            try {
                fileCollection.packItUp(temp);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void scanJar() throws IOException {
        fileChooser.setTitle("Select Jar File For Resource Extraction");
        File file = fileChooser.showOpenDialog(primaryStage);

        if(file == null) return;

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
            labelText.set("Jar: " + file.getName());

            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements(); ) {
                JarEntry entry = enumStructure.nextElement();
                if (entry.getName().endsWith(".lang") || entry.getName().endsWith("splashes.txt")) {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));
                    fileCollection.addFile(entry.getName(), inputReader);
                    createFileSelectButton(entry.getName());
                }
            }
            jarScanner.close();
        } else {
            //invalid jar warning here.
        }
    }

    private void createFileSelectButton(String input) {
        String name = Character.toUpperCase(input.charAt(input.lastIndexOf("/") + 1)) + input.substring(input.lastIndexOf("/") + 2);
        Button newButton = new Button(name);
        newButton.setId(input);
        newButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            populateScreen(newButton.getId());
        });

        buttons.add(newButton);
    }

    private void populateScreen(String id) {
        VBox vbox = new VBox();
        VBox.setVgrow(vbox, Priority.ALWAYS);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        //VBox.setMargin(vbox, new Insets(10));

        for(int i = 0; i < fileCollection.getFile(id).getFileLength(); i++) {
            Object item = fileCollection.getFile(id).passLineItemObject(i);

            if (item instanceof LineItemType && item instanceof LineItemContainerReturn) {
                vbox.getChildren().add(((LineItemContainerReturn) item).getContainer());
            }

            if (i % 2 == 0) {
                vbox.getChildren().getLast().setStyle("-fx-background-color: rgb(235, 235, 235);");
            }
        }

        contentsPane.setFitToWidth(true);
        contentsPane.setContent(vbox);
    }
}
