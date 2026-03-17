package org.wildedit;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LineItem implements LineItemType, LineItemContainerReturn {

    private final LineItemTypeEnums type = LineItemTypeEnums.LINE_ITEM;
    private final String oldContents;
    private String contents;

    public LineItem(String oldContents) {
        this.oldContents = oldContents;
    }

    public LineItemTypeEnums getType() {
        return LineItemTypeEnums.valueOf(String.valueOf(type));
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

    public HBox getContainer() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        Label item1 = new Label(oldContents);
        item1.setPrefWidth(400);
        item1.setWrapText(true);

        TextField item2 = new TextField();
        item2.setPrefWidth(600);
        item2.setText(contents);
        item2.textProperty().addListener((e, oldValue, newValue) -> setContents(newValue));

        box.getChildren().addAll(item2, item1);
        return box;
    }
}
