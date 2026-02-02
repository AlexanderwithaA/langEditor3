import javafx.scene.control.Separator;

import javax.swing.*;

public class BlankLineItem implements LineItemType{

    private final lineItemTypeEnums type = lineItemTypeEnums.BLANK_LINE_ITEM;

    public lineItemTypeEnums getType() {
        return lineItemTypeEnums.valueOf(String.valueOf(type));
    }

    public Separator formatted_Get() {
        return new Separator();
    }
}
