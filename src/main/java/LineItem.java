public class LineItem implements LineItemType {

    private final lineItemTypeEnums type = lineItemTypeEnums.LINE_ITEM;
    private final String oldContents;
    private String contents;

    public LineItem(String oldContents1) {
        oldContents = oldContents1;
    }

    public String getType() {
        return type.toString();
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
