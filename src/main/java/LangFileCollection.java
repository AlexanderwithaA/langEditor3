import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class LangFileCollection {

    HashMap<String, LangFile> fileMap = new HashMap<>();

    public void populateCollection(String input) {
        if (!input.isBlank()) {
            File file = new File(input);
            intermediateCheck(file);
        }
    }

    public void populateCollection(String[] args) {
        for (String input : args) {
            if (!input.isBlank()) {
                File file = new File(input);
                intermediateCheck(file);
            }
        }
    }

    private void intermediateCheck(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                if(file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("lang")) {
                    insertFile(file);
                } else {
//                    System.out.println("Man...\nwhat's this piece of junk? This " + file.getName() + "???\nGet that junk outta here! I DON'T LIKE "
//                            + file.getName().substring(file.getName().lastIndexOf(".") + 1) + "'s!");
                }
            } else if (file.isDirectory()) {
                File[] directoryListing = file.listFiles();
                if (directoryListing != null) {
                    for (File item : directoryListing) {
                        intermediateCheck(item); //Nested function calling >:)
                    }
                }
            }
        }
    }

    private void insertFile(File path) {
        String name = path.getName();
        if(!fileMap.containsKey(name)) {
            fileMap.put(name, new LangFile(path));
            fileMap.put("__" + name, new LangFile(path));
        }
    }

    public String[] getFileNames() {
         return fileMap.keySet().toArray(new String[0]);
    }

    public LangFile loadFile(String name) {
        return fileMap.get(name);
    }

//    public LangFile emptyMap(File path) {
//        insertFile(path);
//        temp = fileMap.la
//        temp.dropAllValues();
//        return temp;
//    }

    public void jarScanner(File file) throws IOException {
        JarFile jarScanner = new JarFile(file);
        Manifest manifest = new Manifest(jarScanner.getManifest());

        Map<String, Attributes> entries = manifest.getEntries();
        Object[] temp1 = entries.keySet().toArray();
        Object[] temp2 = entries.entrySet().toArray();

        for(int i = 0; i < temp1.length; i++) {
            System.out.println(temp1[i] + " | " + temp2[i]);
        }
    }

}
