package windowComponents;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class jarSelectionBuilder {
    private boolean exporting;
    private final StringProperty labelText = new SimpleStringProperty();
    private final VBox jarSelectionBox;
    private final Button export;

    public jarSelectionBuilder() {
        Label selectedJar = new Label();
        Button selectJar = new Button("Select the BTA Jar");
        export = new Button("Export Language Pack");

        // set styling/properties for the above
        selectedJar.textProperty().bind(labelText);
        labelText.set("Jar: No Jar Selected");
        setExportAllowance(false);

        jarSelectionBox = new VBox(selectedJar, selectJar, export);

        // set styling/properties for the above
        VBox.setMargin(jarSelectionBox, new Insets(10));
        jarSelectionBox.setSpacing(4);


    }

    public VBox getJarSelectionBox() {
        return jarSelectionBox;
    }

    public void setImportButtonText(String newText) {
        labelText.set(newText);
    }

    // the boolean is inverted to make reading the function make more sense. Makes true allow exporting and false disallow exporting.
    public void setExportAllowance(Boolean state) {
        exporting = state;
        export.setDisable(!exporting);
    }
}
