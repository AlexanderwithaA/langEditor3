import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileCollection {

    private final HashMap<String, FileContainer> fileMap = new HashMap<>();

    public void addFile(String fileName, BufferedReader inputReader) throws IOException {
        fileMap.put(fileName, new FileContainer(fileName, inputReader));
    }

    public FileContainer getFile(String file) {
        return fileMap.get(file);
    }

    public void packItUp(String[] manifest) throws IOException {
        HashMap<String, List<String>> cleanFileMap = new HashMap<>();
        for(String key : fileMap.keySet()) {
            if (fileMap.get(key).areYouModified()) {
                cleanFileMap.put(key,fileMap.get(key).returnDiffClone());
            }
        }

        FileOutputStream fileOS = new FileOutputStream("temp.zip");
        ZipOutputStream zipOS = new ZipOutputStream(fileOS);

        for(String path : cleanFileMap.keySet()) {
            //convert list to byte stream
            StringBuilder stringBuilder = new StringBuilder();
            for(String element : cleanFileMap.get(path)) {
                stringBuilder.append(element).append("\n");
            }

            byte[] stringBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

            //pack byte stream into zip

            InputStream inputStream = new ByteArrayInputStream(stringBytes);
            ZipEntry zipEntry = new ZipEntry(path);
            zipOS.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int data;
            while ((data = inputStream.read(bytes)) >= 0) {
                zipOS.write(bytes, 0, data);
            }
            inputStream.close();
        }
        zipOS.close();
        fileOS.close();
    }
}