package org.wildedit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.wildedit.lineItem.LineItemContainerReturn;
import org.wildedit.lineItem.LineItemType;
import org.wildedit.windowComponents.ExportDialog;
import org.wildedit.windowComponents.JarSelectionBuilder;

import java.io.IOException;
import java.util.Optional;

// This class's purpose is to separate all the logic of this program from the UI. Mainly to clean up the main class
public class WindowBuilder {
    private final ObservableList<Button> fileSelectionPane = FXCollections.observableArrayList();
    private final ScrollPane contentsPane = new ScrollPane();
    private final FileCollection fileCollection;
    private boolean showFilledLineItems = true;
    JarSelectionBuilder builder;

    public WindowBuilder(FileCollection fileCollection) {
        this.fileCollection = fileCollection;
    }

    // build the menuBar contents/items
    private MenuBar menuBarBuilder() {
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        //file item
        MenuItem scan =  new MenuItem("Scan Jar");
        MenuItem exportLangPack = new MenuItem("Export Language Pack");
        MenuItem importLangPack = new MenuItem("Import Language Pack");
        MenuItem pullJar = new MenuItem("Sync With Jar");
        MenuItem quit = new MenuItem("Quit");
        fileMenu.getItems().addAll(scan, exportLangPack, new SeparatorMenuItem(), importLangPack, pullJar, new SeparatorMenuItem(), quit);

        //edit item
        CheckMenuItem showFilled =  new CheckMenuItem("Show Filled Strings");
        showFilled.setSelected(true);
        editMenu.getItems().addAll(showFilled);

        //help item
        MenuItem basicUsage = new MenuItem("Basic Usage");
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().addAll(basicUsage,about);



        scan.addEventHandler(ActionEvent.ACTION,event -> {
            builder.getJarSelectButton().fire();
        });

        exportLangPack.addEventHandler(ActionEvent.ACTION,event -> {
            builder.getJarExportButton().fire();
        });

        basicUsage.addEventHandler(ActionEvent.ACTION,event -> {
            ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            Dialog<String> dialog = new Dialog<>();
            try {
                dialog.getDialogPane().setContent(IOmanager.scanTxt(getClass().getClassLoader().getResourceAsStream("help-basicusage")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dialog.resizableProperty().setValue(true);
            dialog.getDialogPane().getButtonTypes().addAll(ok);
            dialog.show();
        });

        quit.addEventHandler(ActionEvent.ACTION,event -> {
            System.exit(0);
        });

        showFilled.addEventHandler(ActionEvent.ACTION,event -> {
            showFilledLineItems = showFilled.selectedProperty().get();
            loadFileContents(contentsPane.getContent().getId());
        });

        return new MenuBar(fileMenu, editMenu, helpMenu);
    }

    // create the base format of the langEditor window
    public Scene baseLayout() {
        builder = new JarSelectionBuilder();

        ListView<Button> fileList = new ListView<>(fileSelectionPane);
        VBox selectorContainer = new VBox(fileList, builder.getJarSelectionBox());
        SplitPane workspace = new SplitPane(selectorContainer, contentsPane);
        VBox root = new VBox(menuBarBuilder(),workspace);
        // yeah, I removed the menubar, no, it didn't have a purpose yet.

        // style the above components
        VBox.setVgrow(fileList, Priority.ALWAYS);
        VBox.setVgrow(workspace, Priority.ALWAYS);
        workspace.setDividerPositions((double) 2/7);

        return new Scene(root,512, 384);
    }

    // jarSelectionBuilder passthrough
    public JarSelectionBuilder getJarSelectionBuilder() {
        return builder;
    }

    // create buttons for selecting jar files
    public Button newFileSelectButton(String input) {
        String name = Character.toUpperCase(input.charAt(input.lastIndexOf("/") + 1)) + input.substring(input.lastIndexOf("/") + 2);
        Button newButton = new Button(name);
        newButton.setId(input);

        fileSelectionPane.add(newButton);
        return newButton;
    }

    //read the data from a file and create the correlating components on screen
    //the ID getting thrown all over the place is the file path
    public void loadFileContents(String id) {
        VBox vbox = new VBox();
        vbox.setId(id);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);

        for(int i = 0; i < fileCollection.getFile(id).getFileLength(); i++) {
            if (showFilledLineItems || !fileCollection.getFile(id).lineItemModificationState(i)) {
                Object item = fileCollection.getFile(id).passLineItemObject(i);

                if (item instanceof LineItemType && item instanceof LineItemContainerReturn) {
                    vbox.getChildren().add(((LineItemContainerReturn) item).getContainer());
                }

                if (i % 2 == 0) { // alternating colors for better visibility
                    vbox.getChildren().getLast().setStyle("-fx-background-color: rgb(235, 235, 235);");
                }
            }
        }

        contentsPane.setFitToWidth(true);
        contentsPane.setContent(vbox);
    }

    public Optional<String[]> exportWindow() {
        ExportDialog dialog = new ExportDialog();
        return dialog.showAndWait();
    }
}
