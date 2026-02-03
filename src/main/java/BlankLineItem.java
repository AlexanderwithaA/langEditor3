import javafx.scene.layout.HBox;

public class BlankLineItem implements LineItemType, LineItemContainerReturn {

    private final lineItemTypeEnums type = lineItemTypeEnums.BLANK_LINE_ITEM;

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public HBox getContainer() {
        return new HBox();
    }
}
