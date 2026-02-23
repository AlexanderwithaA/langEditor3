import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import windowComponents.jarSelectionBuilder;

public class windowBuilder {
    private final ObservableList<Button> buttons = FXCollections.observableArrayList();
    private final ScrollPane contentsPane = new ScrollPane();
    jarSelectionBuilder builder;

    // build the menuBar contents/items
    private MenuBar menuBarBuilder() {
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");
        return new MenuBar(fileMenu, editMenu, helpMenu);
    }

    // create the base format of the langEditor window
    private Scene baseLayout() {
        builder = new jarSelectionBuilder();

        ListView<Button> fileList = new ListView<>(buttons);
        VBox selectorContainer = new VBox(fileList, builder.getJarSelectionBox());
        SplitPane workspace = new SplitPane(selectorContainer, contentsPane);
        VBox root = new VBox(menuBarBuilder(), workspace);

        // style the above components
        VBox.setVgrow(fileList, Priority.ALWAYS);
        VBox.setVgrow(workspace, Priority.ALWAYS);
        workspace.setDividerPositions((double) 2/7);

        return new Scene(root,512, 384);
    }
}
