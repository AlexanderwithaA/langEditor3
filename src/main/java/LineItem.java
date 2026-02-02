import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LineItem implements LineItemType {

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

    public Label formatted_GetOldContents() {
        return new Label(oldContents);
    }

    public TextField formatted_GetContents() {
        TextField temp = new TextField();
        temp.setPromptText(contents);
        return temp;
    }

}
