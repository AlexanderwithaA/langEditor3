import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
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

public class Editor {

    static Terminal terminal;
    static PrintWriter writer;
    static LineReader reader;
    static int terminalRows;
    static int terminalColumns;
    static int lineOffset;

    LangFile file;
    Display keyBox;
    Display valueBox;
    static List<AttributedString> keyScreenBuffer = new ArrayList<>();
    static List<AttributedString> valueScreenBuffer = new ArrayList<>();

    Editor(LangFile temp) {
        file = temp;
    }

    public void initializeEditor() throws IOException {
        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        keyBox = new Display(terminal, false);
        valueBox = new Display(terminal, false);
        writer = terminal.writer();
        reader = LineReaderBuilder.builder().terminal(terminal).build();

        terminalRows = terminal.getSize().getRows();
        terminalColumns = terminal.getSize().getColumns();
        lineOffset = 0;

        initializeScreenBuffer();
        print();

//        terminal.handle(Signal.INT, signal -> {
//            // Handle Ctrl+C
//            System.out.println("success");
//        });
        terminal.handle(Signal.WINCH, signal -> {
            terminalRows = terminal.getSize().getRows();
            terminalColumns = terminal.getSize().getColumns();
            initializeScreenBuffer();
            print();
        });

//        terminal.handle(Signal.TSTP, signal -> {
//            // Handle Ctrl+Z (suspend)
//            terminal.pause();
//        });

    }

    private void clearScreen() {
        terminal.puts(InfoCmp.Capability.clear_screen);
        writer.flush();
    }

//    public void inputPrompt() {
//        String prompt = "Path or directory: ";
//
//        writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
//        writer.flush();
//        String input = reader.readLine(prompt);
//
//        while(!(input.equalsIgnoreCase("done") || input.equalsIgnoreCase("y"))) {
//            langFileAccessor.populateCollection(input.replaceAll("\"",""));
//            writer.println();
//            writer.println("Input filepath of a .LANG formatted file or a folder containing such files. Type \"Done\" to complete file input.");
//            writer.flush();
//            input = reader.readLine(prompt);
//        }
//    }

//    public void fileSelect() {
//        int i = 0;
//        int input;
//        for(; i < langFileAccessor.getFileNames().length; i++) {
//            writer.println(i + ") " + langFileAccessor.getFileNames()[i]);
//        }
//        writer.println();
//        writer.flush();
//        try {
//            input = Integer.parseInt(reader.readLine("Enter a number between " + 0 + " and " + (i - 1) + " to load a file: "));
//        } catch(NumberFormatException e) {
//            clearScreen();
//            terminal.puts(Capability.cursor_address, 8, 0);
//            writer.println("Must be an integer!");
//            terminal.puts(Capability.cursor_address, 0, 0);
//            writer.flush();
//            fileSelect();
//            return;
//        }
//        if(input < 0 || input > i) {
//            clearScreen();
//            terminal.puts(Capability.cursor_address, 8, 0);
//            writer.println("Must be within range!");
//            terminal.puts(Capability.cursor_address, 0, 0);
//            writer.flush();
//            fileSelect();
//            return;
//        }
//        file = langFileAccessor.loadFile(langFileAccessor.getFileNames()[input]);
//    }

    private void print() {
        clearScreen();
//        keyBox.resize(terminalRows, terminalColumns / 4);
//        keyBox.update(keyScreenBuffer, 1);
        terminal.puts(Capability.cursor_address, 20, 20);
        valueBox.resize(terminalRows, terminalColumns - (terminalColumns / 4));
        valueBox.update(valueScreenBuffer, 1);
        terminal.puts(Capability.cursor_address, 0, 0);
        writer.println(terminalRows + ":" + terminalColumns + " >=(*>");
        writer.flush();
    }

    private void initializeScreenBuffer() {
        String keyBuffer = file.getInitialValue()[0];
        int i = 0;
        while(i < lineOffset) {
            keyBuffer = file.nextKeyValue(keyBuffer)[0];
            i++;
        }

        for(i = 0; i < terminalRows; i++) {
            keyScreenBuffer.add(new AttributedString(file.nextKeyValue(keyBuffer)[0]));
            valueScreenBuffer.add(new AttributedString(file.nextKeyValue(keyBuffer)[1]));
            keyBuffer = file.nextKeyValue(keyBuffer)[0];
            if(keyBuffer == null) {
                break;
            }
        }
    }

}
