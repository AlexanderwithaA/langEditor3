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
}