import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LangLineItem implements LineItemType, LineItemContainerReturn {

    private final lineItemTypeEnums type = lineItemTypeEnums.LANG_LINE_ITEM;
    private final String key;
    private final String oldValue;
    private String newValue;

    public LangLineItem(String key, String oldValue) {
        this.key = key;
        this.oldValue = oldValue;
    }

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public String getKey() {
        return key;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public HBox getContainer() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        Label item1 = new Label(key);
        item1.setPrefWidth(300);
        item1.setWrapText(true);

        Label item2 = new Label(oldValue);
        item2.setPrefWidth(300);
        item2.setWrapText(true);

        TextField item3 = new TextField();
        item3.setPrefWidth(400);
        item3.setText(newValue);
        item3.textProperty().addListener((observable, oldValue, newValue) -> {
            setNewValue(newValue);
        });

        box.getChildren().addAll(item1, item3, item2);
        return box;
    }
}