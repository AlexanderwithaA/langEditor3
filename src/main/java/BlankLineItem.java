public class BlankLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.BLANK_LINE_ITEM;

    public String getType() {
        return type.toString();
    }
}
