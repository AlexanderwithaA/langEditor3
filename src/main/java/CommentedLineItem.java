public class CommentedLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.COMMENTED_LINE_ITEM;
    private final String contents;

    public CommentedLineItem(String contents1) {
        if(!contents1.isBlank() && contents1.startsWith("#")) {
            contents1 = contents1.substring(1);
        }
        contents = contents1;
    }

    public String getType() {
        return type.toString();
    }

    public String getContents() {
        return contents;
    }
}
