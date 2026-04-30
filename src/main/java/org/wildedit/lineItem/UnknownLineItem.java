package org.wildedit.lineItem;

public class UnknownLineItem implements LineItemType{

    private final LineItemTypeEnums type = LineItemTypeEnums.UNKNOWN_LINE_ITEM;
    private final String contents;

    public UnknownLineItem(String contents) {
        this.contents = contents;
    }

    public LineItemTypeEnums getType() {
        return LineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public Boolean areYouFilled() {return false;}

    public String getContents() {
        return contents;
    }
}
