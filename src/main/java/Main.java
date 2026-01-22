import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
    private Stage primaryStage;
    private final FileChooser fileChooser = new FileChooser();
    private final ObservableList<Button> buttons = FXCollections.observableArrayList();
    private final StringProperty labelText = new SimpleStringProperty("Jar: No Jar Selected");

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
        ScrollPane filePane = new ScrollPane(fileList);

        Label selectedJar = new Label();
        selectedJar.textProperty().bind(labelText);

        Button selectJar = new Button("Select the BTA Jar");
        VBox jarSelectionBox = new VBox(selectedJar, selectJar);

        VBox.setMargin(jarSelectionBox, new Insets(10));
        jarSelectionBox.setSpacing(4);
        VBox.setVgrow(filePane, Priority.ALWAYS);
        VBox.setVgrow(fileList, Priority.ALWAYS);

        VBox selectorContainer = new VBox(filePane, jarSelectionBox);

        VBox contents = new VBox();
        ScrollPane contentsPane = new ScrollPane(contents);

        SplitPane workspace = new SplitPane(selectorContainer, contentsPane);
        VBox root = new VBox(menuBar, workspace);

        VBox.setVgrow(workspace, Priority.ALWAYS);

        Scene mainPanel = new Scene(root,512, 384);
        this.primaryStage.setScene(mainPanel);
        this.primaryStage.show();

        //button handler

        selectJar.setOnAction(e -> {
            try {
                scanJar();
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
            openFileContents(newButton.getId());
        });

        buttons.add(newButton);
    }

    private void openFileContents(String id) {
        FileContainer container = fileCollection.getFile(id);
        for(int i = 0; i < container.getFileLength(); i++) {
            createLineItemBox(container.passLineItemObject(i));
        }
    }

    private void createLineItemBox(Object item) {
        GridPane pane = new GridPane(4,4);

        if(item instanceof LineItemType) {
            switch(((LineItemType) item).getType()) {
                case LINE_ITEM:
                    Label text = new Label(((LineItem) item).getOldContents());
                    TextField field = new TextField();
                    pane.add(text, 0,0);
                    pane.add(field, 2,0);
                    break;
                case LANG_LINE_ITEM:
                    break;
                case COMMENTED_LINE_ITEM:
                    Label text = new Label(((CommentedLineItem) item).getContents());
                    pane.add(text, 0,0);
                    GridPane.setColumnSpan(text, 3);
                    break;
                case BLANK_LINE_ITEM:
                    break;
                case UNKNOWN_LINE_ITEM:
                    break;
            }
        }
    }
}
