package org.wildedit.lineItem;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CommentedLineItem implements LineItemType, LineItemContainerReturn {

    private final LineItemTypeEnums type = LineItemTypeEnums.COMMENTED_LINE_ITEM;
    private final String contents;

    public CommentedLineItem(String contents) {
        if(!contents.isBlank() && contents.startsWith("#")) {
            contents = contents.substring(1);
        }
        this.contents = contents;
    }

    public LineItemTypeEnums getType() {
        return LineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public String getContents() {
        return contents;
    }

    public HBox getContainer() {
        HBox box = new HBox();

        Label item1 = new Label(contents);
        item1.setPrefWidth(1000);
        item1.setWrapText(true);

        box.getChildren().add(item1);
        return box;
    }
}
