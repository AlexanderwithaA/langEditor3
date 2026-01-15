import java.io.*;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LangFileCollection {

    HashMap<String, LangFile> fileMap = new HashMap<>();
    final String[] implementationVersions = {"7.3_04"};

    public void populateCollection(String input) {
//        if (!input.isBlank()) {
//            File file = new File(input);
//            intermediateCheck(file);
//        }
    }
//
//    public void populateCollection(String[] args) {
//        for (String input : args) {
//            if (!input.isBlank()) {
//                File file = new File(input);
//                intermediateCheck(file);
//            }
//        }
//    }

//    private void intermediateCheck(File file) {
//        if (file.exists()) {
//            if (file.isFile()) {
//                if(file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("lang")) {
//                    insertFile(file);
//                } else {
////                    System.out.println("Man...\nwhat's this piece of junk? This " + file.getName() + "???\nGet that junk outta here! I DON'T LIKE "
////                            + file.getName().substring(file.getName().lastIndexOf(".") + 1) + "'s!");
//                }
//            } else if (file.isDirectory()) {
//                File[] directoryListing = file.listFiles();
//                if (directoryListing != null) {
//                    for (File item : directoryListing) {
//                        intermediateCheck(item); //Nested function calling >:)
//                    }
//                }
//            }
//        }
//    }

//    private void insertFile(File path) {
//        if(!fileMap.containsKey(fileName)) {
//            fileMap.put(fileName, new LangFile(fileName, inputStream));
//            //fileMap.put("__" + name, new LangFile(path));
//        }
//    }

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
        boolean validJar = false;

        for(String value : implementationVersions) {
            if(value.equals(manifest.getMainAttributes().getValue("Implementation-Version"))) {
                validJar = true;
                break;
            }
        }

        if(validJar) {
            BufferedReader inputReader = null;

            for (Enumeration<JarEntry> enumStructure = jarScanner.entries(); enumStructure.hasMoreElements();) {
                JarEntry entry = enumStructure.nextElement();
                if(entry.getName().endsWith(".lang")) {
                    inputReader = new BufferedReader(new InputStreamReader(jarScanner.getInputStream(entry)));

                    System.out.println(entry.getName() + " | " + inputReader.readLine());

                    if(!fileMap.containsKey(entry.getName())) {
                        fileMap.put(entry.getName(), new LangFile(entry.getName(), inputReader));
                    }
                }
                if(entry.getName().endsWith("splashes.txt")) {
                    //I need to make a class for the splashes file...
                }
            }
            jarScanner.close();
            inputReader.close();
        } else {
            //invalid jar warning here.
        }
    }

}
