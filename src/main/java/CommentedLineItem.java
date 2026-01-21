public class CommentedLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.COMMENTED_LINE_ITEM;
    private final String contents;

    public CommentedLineItem(String contents) {
        if(!contents.isBlank() && contents.startsWith("#")) {
            contents = contents.substring(1);
        }
        this.contents = contents;
    }

    public String getType() {
        return type.toString();
    }

    public String getContents() {
        return contents;
    }
}
