import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class FileCollection {

    private final HashMap<String, FileContainer> fileMap = new HashMap<>();

    public void addFile(String fileName, BufferedReader inputReader) throws IOException {
        fileMap.put(fileName, new FileContainer(fileName, inputReader));
    }

    public FileContainer getFile(String file) {
        return fileMap.get(file);
    }

    public void packItUp(String[] manifest) {
        HashMap<String, FileContainer> cleanFileMap = new HashMap<>();
        for(String key : fileMap.keySet()) {
            if (!fileMap.get(key).areYouModified()) {
                cleanFileMap.put(key,fileMap.get(key));
            }
        }


    }
}