import org.jline.terminal.Terminal;
import org.jline.utils.Signals;

import java.io.IOException;

public class Main {

    static LangFileCollection langFileAccessor = new LangFileCollection();

    public static void main(String[] args) throws IOException {
        TerminalHandler terminal  = new TerminalHandler();
        terminal.createOutPutWindow();
        terminal.testOutPut();
        terminal.inputPrompt();

//        Object handle = Signals.register("INT", () -> {
//            System.out.println("Caught SIGINT");
//            // Perform cleanup
//        });



    }
}
