import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class FileCollection {

    private String[] implementationVersions = {"7.3_04"};
    private HashMap<String, File> fileMap = new HashMap<>();

    public void jarScanner(File file) throws IOException {
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
            BufferedReader inputReader = null;

            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements(); ) {
                JarEntry entry = enumStructure.nextElement();
                if (entry.getName().endsWith(".lang") || entry.getName().endsWith(".txt")) {
                    inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));

                    //System.out.println(entry.getName() + " | " + inputReader.readLine());

                    if (!fileMap.containsKey(entry.getName())) {
                        fileMap.put(entry.getName(), new File(entry.getName(), inputReader));
                    }
                }
                if (entry.getName().endsWith("splashes.txt")) {
                    //I need to make a class for the splashes file...
                }
            }
            jarScanner.close();
        } else {
            //invalid jar warning here.
        }
    }
}