import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import windowComponents.ExportDialog;
import windowComponents.JarSelectionBuilder;

import java.util.Optional;

// This class's purpose is to separate all the logic of this program from the UI. Mainly to clean up the main class
public class WindowBuilder {
    private final ObservableList<Button> fileSelectionPane = FXCollections.observableArrayList();
    private final ScrollPane contentsPane = new ScrollPane();
    private final FileCollection fileCollection;
    JarSelectionBuilder builder;

    public WindowBuilder(FileCollection fileCollection) {
        this.fileCollection = fileCollection;
    }

    // build the menuBar contents/items
    private MenuBar menuBarBuilder() {
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");
        return new MenuBar(fileMenu, editMenu, helpMenu);
    }

    // create the base format of the langEditor window
    public Scene baseLayout() {
        builder = new JarSelectionBuilder();

        ListView<Button> fileList = new ListView<>(fileSelectionPane);
        VBox selectorContainer = new VBox(fileList, builder.getJarSelectionBox());
        SplitPane workspace = new SplitPane(selectorContainer, contentsPane);
        VBox root = new VBox(workspace);
        // yeah, I removed the menubar, no, it didn't have a purpose yet.
        // menuBarBuilder(),

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
    public void loadFileContents(String id) {
        VBox vbox = new VBox();
        VBox.setVgrow(vbox, Priority.ALWAYS);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);

        for(int i = 0; i < fileCollection.getFile(id).getFileLength(); i++) {
            Object item = fileCollection.getFile(id).passLineItemObject(i);

            if (item instanceof LineItemType && item instanceof LineItemContainerReturn) {
                vbox.getChildren().add(((LineItemContainerReturn) item).getContainer());
            }

            if (i % 2 == 0) { // alternating colors for better visibility
                vbox.getChildren().getLast().setStyle("-fx-background-color: rgb(235, 235, 235);");
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
