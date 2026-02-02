import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    public Label formatted_GetContents() {
        return new Label(contents);
    }
}
