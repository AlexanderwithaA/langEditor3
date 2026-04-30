package org.wildedit.lineItem;

import javafx.scene.layout.HBox;

public class BlankLineItem implements LineItemType, LineItemContainerReturn {

    private final LineItemTypeEnums type = LineItemTypeEnums.BLANK_LINE_ITEM;

    public LineItemTypeEnums getType() {
        return LineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public Boolean areYouFilled() {return false;}

    public HBox getContainer() {
        return new HBox();
    }
}
