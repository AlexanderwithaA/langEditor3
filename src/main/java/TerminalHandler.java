import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal.Signal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class TerminalHandler {

    static Terminal terminal;
    static PrintWriter writer;
    static Reader reader;

    public static void createOutPutWindow() throws IOException {
        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        writer = terminal.writer();
        reader = terminal.reader();

        terminal.handle(Signal.INT, signal -> {
            // Handle Ctrl+C
            System.out.println("success");
        });
        terminal.handle(Signal.WINCH, signal -> {
            System.out.println("terminal rows is " + terminal.getSize().getRows() + " and " + terminal.getSize().getColumns() + " columns");
        });

        terminal.handle(Signal.TSTP, signal -> {
            // Handle Ctrl+Z (suspend)
            terminal.pause();
        });

    }

    public void testOutPut() {
        writer.println("waoh");
        writer.println("woah!");
        writer.println("man wth!!");
        writer.flush();
    }



//    Object handle = Signals.register("INT", () -> {
//        System.out.println("Caught SIGINT");
//        // Perform cleanup
//    });
}
