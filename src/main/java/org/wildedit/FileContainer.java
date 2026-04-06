package org.wildedit;

import org.wildedit.lineItem.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileContainer {

    enum fileTypeEnum {
        LANG,
        TXT
    }

    private final List<Object> fileContents = new ArrayList<>();
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

            //System.out.println(line);

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
                String value;

                //where the key gets defined (I'm having to relearn this rn) if key == lowercase key.... huh? was I on crack?
                //if(line.split("=", 2)[0].equals(line.split("=", 2)[0].toLowerCase())) {
                    key = line.split("=", 2)[0];
                //}

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
                    + LineItemTypeEnums.UNKNOWN_LINE_ITEM + "!");
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

    public boolean areYouModified() {
        for (Object item : fileContents) {
            if (item instanceof LineItemType) {
                switch (((LineItemType) item).getType()) {
                    case UNKNOWN_LINE_ITEM:
                        break;
                    case BLANK_LINE_ITEM:
                        break;
                    case LINE_ITEM:
                        if(item instanceof LineItem && ((LineItem) item).getContents() != null && !((LineItem) item).getContents().isBlank()) {
                            return true;
                        }
                        break;
                    case LANG_LINE_ITEM:
                        if(item instanceof LangLineItem && ((LangLineItem) item).getNewValue() != null && !((LangLineItem) item).getNewValue().isBlank()) {
                            return true;
                        }
                        break;
                    case COMMENTED_LINE_ITEM:
                        break;
                }
            }
        }

        return false;
    }

    public List<String> returnDiffClone() {
        List<String> diffContents = new ArrayList<>();

        for (Object item : fileContents) {
            if (item instanceof LineItemType) {
                switch (((LineItemType) item).getType()) {
                    case UNKNOWN_LINE_ITEM:
                        break;
                    case BLANK_LINE_ITEM:
                        break;
                    case LINE_ITEM:
                        if(item instanceof LineItem && ((LineItem) item).getContents() != null && !((LineItem) item).getContents().isBlank()) {
                            diffContents.add(((LineItem) item).getContents());
                        }
                        break;
                    case LANG_LINE_ITEM:
                        if(item instanceof LangLineItem && ((LangLineItem) item).getNewValue() != null && !((LangLineItem) item).getNewValue().isBlank()) {
                            diffContents.add(((LangLineItem) item).getKey() + "=" + ((LangLineItem) item).getNewValue());
                        }
                        break;
                    case COMMENTED_LINE_ITEM:
                        break;
                }
            }
        }

        return diffContents;
    }


}
