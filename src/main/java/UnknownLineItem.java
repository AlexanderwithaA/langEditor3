public class UnknownLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.UNKNOWN_LINE_ITEM;
    private final String contents;

    public UnknownLineItem(String contents) {
        this.contents = contents;
    }

    public String getType() {
        return type.toString();
    }

    public String getContents() {
        return contents;
    }
}
