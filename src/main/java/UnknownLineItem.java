import javafx.scene.control.Label;

public class UnknownLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.UNKNOWN_LINE_ITEM;
    private final String contents;

    public UnknownLineItem(String contents) {
        this.contents = contents;
    }

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public String getContents() {
        return contents;
    }

    public Label formatted_GetContents() {
        return new Label(contents);
    }
}
