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

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Java Archive (*.jar)", "*.jar");
        fileChooser.getExtensionFilters().add(filter);
    }

    public void scanJar() throws IOException {
        fileChooser.setTitle("Select Jar File For Resource Extraction");
        File file = fileChooser.showOpenDialog(stage);

        if(file == null) return;

        JarFile jarScanner = new JarFile(file);
        Manifest manifest = new Manifest(jarScanner.getManifest());
        boolean validVersion = false;
        boolean validMainClass = manifest.getMainAttributes().getValue("Main-Class").equals("net.minecraft.client.Minecraft");

        for (String value : implementationVersions) {
            if (value.equals(manifest.getMainAttributes().getValue("Implementation-Version"))) {
                validVersion = true;
                break;
            }
        }

        if (validMainClass && validVersion) {
            windowBuilder.getJarSelectionBuilder().setImportButtonText("Jar: " + file.getName());
            windowBuilder.getJarSelectionBuilder().setExportAllowance(true);
            windowBuilder.getJarSelectionBuilder().setImportAllowance(false);

            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements(); ) {
                JarEntry entry = enumStructure.nextElement();
                if (entry.getName().endsWith(".lang")) { // Removed the ability to edit the splashes as that does nothing atm  || entry.getName().endsWith("splashes.txt")
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));

                    //Strip out locale data and lang path, readd it later!
                    String strippedPath = entry.getName().substring(entry.getName().indexOf("/",entry.getName().indexOf("/") + 1));
                    fileCollection.addFile(strippedPath, inputReader);
                    Button newButton = windowBuilder.newFileSelectButton(strippedPath);
                    newButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> windowBuilder.loadFileContents(newButton.getId()));
                }
            }
            jarScanner.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid jar, ensure the selected jar is BTA"); //alternate text of: "Last I checked that ain't BTA"
            alert.showAndWait();
        }
    }
}
