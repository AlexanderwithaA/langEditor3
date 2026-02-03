import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LineItem implements LineItemType, LineItemContainerReturn {

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

    public HBox getContainer() {
        HBox box = new HBox();
        HBox.setMargin(box, new Insets(0,10,0,10));
        box.setAlignment(Pos.CENTER);

        Label item1 = new Label(oldContents);
        item1.setMaxWidth(200);
        item1.setMinWidth(200);
        item1.setWrapText(true);

        TextField item2 = new TextField();
        item2.setMaxWidth(200);
        item2.setMinWidth(200);
        item2.setPromptText(contents);

        box.getChildren().addAll(item1, item2);
        return box;
    }
}
