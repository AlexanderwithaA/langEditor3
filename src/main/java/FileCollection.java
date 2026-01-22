import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class FileCollection {


    private final HashMap<String, FileContainer> fileMap = new HashMap<>();

//    public void jarScanner(File file) throws IOException {
//        JarFile jarScanner = new JarFile(file);
//        Manifest manifest = new Manifest(jarScanner.getManifest());
//        boolean validJar = false;
//
//        System.out.println("input file");
//
//        for (String value : implementationVersions) {
//            if (value.equals(manifest.getMainAttributes().getValue("Implementation-Version"))) {
//                validJar = true;
//                break;
//            }
//        }
//
//        if (validJar) {
//            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements(); ) {
//                JarEntry entry = enumStructure.nextElement();
//                if (entry.getName().endsWith(".lang") || entry.getName().endsWith("splashes.txt")) {
//                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));
//                    fileMap.put(entry.getName(), new FileContainer(entry.getName(), inputReader));
//                }
//            }
//            jarScanner.close();
//        } else {
//            //invalid jar warning here.
//        }
//    }

    public void addFile(String fileName, BufferedReader inputReader) throws IOException {
        fileMap.put(fileName, new FileContainer(fileName, inputReader));
    }
}