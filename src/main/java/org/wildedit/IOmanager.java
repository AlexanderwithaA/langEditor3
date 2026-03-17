package org.wildedit;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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

public class IOmanager {
    private final String[] implementationVersions = {"7.3_04"};
    private final FileChooser fileChooser = new FileChooser();
    private final FileCollection fileCollection;
    private final WindowBuilder windowBuilder;
    private final Stage stage;

    public IOmanager(FileCollection fileCollection, WindowBuilder windowBuilder, Stage stage) {
        this.fileCollection = fileCollection;
        this.windowBuilder = windowBuilder;
        this.stage = stage;
    }

    public void scanJar() throws IOException {
        fileChooser.setTitle("Select Jar File For Resource Extraction");
        File file = fileChooser.showOpenDialog(stage);

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

        if (validJar) { // Need to only allow jars. Currently non-jars can be selected as well
            windowBuilder.getJarSelectionBuilder().setImportButtonText("Jar: " + file.getName());

            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements(); ) {
                JarEntry entry = enumStructure.nextElement();
                if (entry.getName().endsWith(".lang")) { // Removed the ability to edit the splashes as that does nothing atm  || entry.getName().endsWith("splashes.txt")
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));
                    fileCollection.addFile(entry.getName(), inputReader);
                    Button newButton = windowBuilder.newFileSelectButton(entry.getName());
                    newButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> windowBuilder.loadFileContents(newButton.getId()));
                }
            }
            jarScanner.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Invalid jar, ensure the selected jar is BTA"); //alternate text of: "Last I checked that ain't BTA"
            alert.showAndWait();
        }
    }
}
