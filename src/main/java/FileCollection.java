import com.google.gson.Gson;

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

    //manifest should contain: title, ID, region, credits
    public void packItUp(String[] manifest) throws IOException {
        HashMap<String, List<String>> cleanFileMap = new HashMap<>();
        for(String key : fileMap.keySet()) {
            if (fileMap.get(key).areYouModified()) {
                cleanFileMap.put(key,fileMap.get(key).returnDiffClone());
            }
        }

        // create the manifest, using a class as the template
        Gson gson = new Gson();
        String json = gson.toJson(new ManifestTemplate(manifest[1],manifest[0],manifest[2],manifest[3]));

        FileOutputStream fileOS = new FileOutputStream(manifest[4] + manifest[1] + ".zip");
        ZipOutputStream zipOS = new ZipOutputStream(fileOS);

        // surely I should be using a different type of input stream??? StringReader()??
        // stream in the manifest
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        ZipEntry zipEntry = new ZipEntry("lang_info.json");
        zipOS.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int data;
        while ((data = inputStream.read(bytes)) >= 0) {
            zipOS.write(bytes, 0, data);
        }
        inputStream.close();

        // stream in the file data
        for(String path : cleanFileMap.keySet()) {
            //convert list to byte stream
            StringBuilder stringBuilder = new StringBuilder();
            for(String element : cleanFileMap.get(path)) {
                stringBuilder.append(element).append("\n");
            }

            byte[] stringBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

            //pack byte stream into zip

            inputStream = new ByteArrayInputStream(stringBytes);
            zipEntry = new ZipEntry(path);
            zipOS.putNextEntry(zipEntry);

            while ((data = inputStream.read(bytes)) >= 0) {
                zipOS.write(bytes, 0, data);
            }
            inputStream.close();
        }
        zipOS.close();
        fileOS.close();
    }
}