import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CommentedLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.COMMENTED_LINE_ITEM;
    private final String contents;

    public CommentedLineItem(String contents) {
        if(!contents.isBlank() && contents.startsWith("#")) {
            contents = contents.substring(1);
        }
        this.contents = contents;
    }

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public String getContents() {
        return contents;
    }

    public HBox getContainer() {
        HBox box = new HBox();

        Label item1 = new Label(contents);

        box.getChildren().add(item1);
        return box;
    }
}
