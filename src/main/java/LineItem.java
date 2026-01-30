public class LineItem implements LineItemType {

    private final lineItemTypeEnums type = lineItemTypeEnums.LINE_ITEM;
    private final String oldContents;
    private String contents;

    public LineItem(String oldContents) {
        this.oldContents = oldContents;
    }

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public String getOldContents() {
        return oldContents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String newContents) {
        contents = newContents;
    }


}
