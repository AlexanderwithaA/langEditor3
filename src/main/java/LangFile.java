import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class LangFile {

    String name;
    TreeMap<String,String> fileTreeMap;

    public LangFile(String fileName, BufferedReader inputStream) {
        name = fileName;
        populateTreeMap(inputStream);
    }

    private void populateTreeMap(BufferedReader inputStream) {
        fileTreeMap = new TreeMap<>();
        try {
            String line;
            while((line = inputStream.readLine()) != null) {
                if(!line.isBlank()) {
                    fileTreeMap.put(line.split("=", 2)[0],line.split("=", 2)[1]);
                }
            }
            inputStream.close();
        } catch(IOException error){
            System.out.println(error);
        }
    }

//    //giving people SqLite flashbacks
//    public void dropAllValues() {
//        fileTreeMap.replaceAll((k, v) -> "");
//    }

    public List<Object> getTreeMap() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for(Map.Entry<String,String> item : fileTreeMap.entrySet()) {
            keys.add(item.getKey());
            values.add(item.getValue());
        }

        return Arrays.asList(keys,values);
    }

    public String getValue(String key) {
        return fileTreeMap.get(key);
    }

    public String getValue(int idx) {
        String key = getInitialValue()[0];
        for(int i = 0; i < idx; i++) {
            key = nextKeyValue(key)[0];
        }
        return fileTreeMap.get(key);
    }

    public void setValue(String key, String value) {
        if (fileTreeMap.containsKey(key)) {
            fileTreeMap.put(key, value);
        }
    }

    public String[] getInitialValue() {
        if (!fileTreeMap.isEmpty()) {
            return fileTreeMap.firstEntry().toString().split("=", 2);
        }
        return new String[] {null,null};
    }

    // next two methods return null if they try to read off the end of the filetreemap.
    public String[] nextKeyValue(String from) {
        if (fileTreeMap.containsKey(from) && fileTreeMap.higherEntry(from) != null) {
            return fileTreeMap.higherEntry(from).toString().split("=", 2);
        }
        return new String[] {null,null};
    }

    public String[] previousKeyValue(String from) {
        if (fileTreeMap.containsKey(from) && fileTreeMap.lowerEntry(from) != null) {
            return fileTreeMap.lowerEntry(from).toString().split("=", 2);
        }
        return new String[] {null,null};
    }


    // returns subkeys of the searched key/key snippet. Returns 1 copy of each key 1 level deeper or in the special case
    // of an empty search returns each super key in the file. A null return signifies that a full key has been reached
    // and as such may be used to get the key-value.
    // Keep in mind, you can get the case of "options.difficulty" which has both an associated value and subkeys.
    public Set getSubKey(String search) {
        int depth = search.replaceAll("[^.]","").length();
        if(!search.isEmpty()) {
            depth++;
        }
        int index;
        Set<String> returnedKeyValues = new HashSet<>();
        boolean insideSearch = false;

        for(Map.Entry<String,String> entry : fileTreeMap.entrySet()) {
            if(entry.getKey().startsWith(search)) {
                insideSearch = true;
                index = 0;
                String temp = entry.getKey() + ".";

                for(int i = 0; i <= depth; i++) {
                    index = temp.indexOf(".",index) + 1;
                }
                if (index > 0) {
                    returnedKeyValues.add(entry.getKey().substring(0,index - 1));
                }
            } else if(insideSearch) {
                break;
            }
        }
        return returnedKeyValues;
    }
}