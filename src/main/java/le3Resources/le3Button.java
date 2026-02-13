package le3Resources;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

public class le3Button extends Button {
    public le3Button() {
        super();
    }

    public le3Button(String text) {
        super(text);
    }

    public le3Button(String text, Node graphic) {
        super(text,graphic);
    }

    private void addListener() {
        EventHandler<InputEvent> handler = new EventHandler<InputEvent>() {
            public void handle(InputEvent e)
            {
                l.setText("   button   selected    ");
            }
        };

        super.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
    }
}
