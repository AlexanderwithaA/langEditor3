import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class TerminalHandler {

    Terminal terminal;

    public void createOutPutWindow() throws IOException {
        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
    }

}
