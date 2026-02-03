import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LangLineItem implements LineItemType {

    private final lineItemTypeEnums type = lineItemTypeEnums.LANG_LINE_ITEM;
    private final String key;
    private final String oldValue;
    private String newValue;

    public LangLineItem(String key, String oldValue) {
        this.key = key;
        this.oldValue = oldValue;
    }

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
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

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}