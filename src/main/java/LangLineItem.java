public class LangLineItem implements LineItemType {

    private final lineItemTypeEnums type = lineItemTypeEnums.LANG_LINE_ITEM;
    private final String key;
    private final String oldValue;
    private String newValue;

    public LangLineItem(String key1, String oldValue1) {
        key = key1;
        oldValue = oldValue1;
    }

    public String getType() {
        return type.toString();
    }

    public String getKey() {
        return key;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue1) {
        newValue = newValue1;
    }
}