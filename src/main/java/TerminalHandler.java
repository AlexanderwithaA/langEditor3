import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal.Signal;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp;
import org.jline.utils.InfoCmp.Capability;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TerminalHandler {

    LangFileCollection langFileAccessor = Main.langFileAccessor;
    LangFile loadedFile;

    static Terminal terminal;
    static PrintWriter writer;
    static int terminalRows;
    static int terminalColumns;
    static LineReader reader;
    static DefaultParser parser;

    static List<AttributedString> textBuffer = new ArrayList<>();
    static int offset;

    public void initialize() throws IOException {
        parser = new DefaultParser();
        parser.setEscapeChars(null);

        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        writer = terminal.writer();
        reader = LineReaderBuilder.builder().terminal(terminal).parser(parser).build();
        terminalRows = terminal.getSize().getColumns();
        terminalColumns = terminal.getSize().getRows();
        offset = 0;



        terminal.handle(Signal.INT, signal -> {
            // Handle Ctrl+C
            System.out.println("success");
        });
        terminal.handle(Signal.WINCH, signal -> {
            terminalRows = terminal.getSize().getRows();
            terminalColumns = terminal.getSize().getColumns();
            initializeScreenBuffer();
            editor();
        });

        terminal.handle(Signal.TSTP, signal -> {
            // Handle Ctrl+Z (suspend)
            terminal.pause();
        });

    }

    public void clearScreen() {
        terminal.puts(InfoCmp.Capability.clear_screen);
        writer.flush();
    }

    public void inputPrompt() {
        String prompt = "Path or directory: ";

        writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
        writer.flush();
        String input = reader.readLine(prompt);

        while(!(input.equalsIgnoreCase("done") || input.equalsIgnoreCase("y"))) {
            langFileAccessor.populateCollection(input.replaceAll("\"",""));
            writer.println();
            writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
            writer.flush();
            input = reader.readLine(prompt);
        }
    }

    public void fileSelect() {
        int i = 0;
        int input;
        for(; i < langFileAccessor.getFileNames().length; i++) {
            writer.println(i + ") " + langFileAccessor.getFileNames()[i]);
        }
        writer.println();
        writer.flush();
        try {
            input = Integer.parseInt(reader.readLine("Enter a number between " + 0 + " and " + (i - 1) + " to load a file: "));
        } catch(NumberFormatException e) {
            clearScreen();
            terminal.puts(Capability.cursor_address, 8, 0);
            writer.println("Must be an integer!");
            terminal.puts(Capability.cursor_address, 0, 0);
            writer.flush();
            fileSelect();
            return;
        }
        if(input < 0 || input > i) {
            clearScreen();
            terminal.puts(Capability.cursor_address, 8, 0);
            writer.println("Must be within range!");
            terminal.puts(Capability.cursor_address, 0, 0);
            writer.flush();
            fileSelect();
            return;
        }
        loadedFile = langFileAccessor.loadFile(langFileAccessor.getFileNames()[input]);
    }

    public void editor() {
        Display textBox = new Display(terminal, true);
        textBox.update(textBuffer, 0,true);
    }

    public void initializeScreenBuffer() {
        String keyBuffer = loadedFile.getInitialValue()[0];
        int i = 0;
        while(i < offset) {
            keyBuffer = loadedFile.nextKeyValue(keyBuffer);
        }

        textBuffer.add(new AttributedString(loadedFile.nextKeyValue(keyBuffer)));
    }

}
