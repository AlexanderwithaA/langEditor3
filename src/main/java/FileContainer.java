import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileContainer {

    enum fileTypeEnum {
        LANG,
        TXT
    }

    private List<Object> fileContents = new ArrayList<Object>();
    private fileTypeEnum fileType;

    public FileContainer(String path, BufferedReader input) throws IOException {
        if(path.endsWith(".lang")) {
            fileType = fileTypeEnum.LANG;
        } if(path.endsWith(".txt")) {
            fileType = fileTypeEnum.TXT;
        }

        populateFileContents(input);
    }

    private void populateFileContents(BufferedReader input) throws IOException {
        while(input.ready()) {
            String line = input.readLine();

            System.out.println(line);

            if(line.isBlank()) {
                fileContents.add(new BlankLineItem());
                continue;
            }

            if(line.startsWith("#")) {
                fileContents.add(new CommentedLineItem(line));
                continue;
            }

            if(fileType == fileTypeEnum.LANG) {
                String key = "";
                String value = "";

                if(line.split("=", 2)[0].equals(line.split("=", 2)[0].toLowerCase())) {
                    key = line.split("=", 2)[0];
                }

                if(line.split("=", 2).length > 1) {
                    value = line.split("=", 2)[1];
                } else {
                    value = "";
                }

                if(key.length() + value.length() > 1) {
                    fileContents.add(new LangLineItem(key, value));
                    continue;
                }
            }

            if(fileType == fileTypeEnum.TXT) {
                fileContents.add(new LineItem(line));
                continue;
            }

            System.out.println("WOAH THERE!! Looks like the line \"" + line + "\" didn't get picked up by any of the checks and is going to be made into a "
                    + lineItemTypeEnums.UNKNOWN_LINE_ITEM + "!");
            fileContents.add(new UnknownLineItem(line));
        }

        input.close();
    }

    public int getFileLength() {
        return fileContents.size();
    }

    public Object passLineItemObject(int index) {
        return fileContents.get(index);
    }


}
