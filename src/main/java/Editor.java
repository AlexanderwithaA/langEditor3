import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
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

import static org.jline.keymap.KeyMap.del;
import static org.jline.keymap.KeyMap.key;

public class Editor {

    Terminal terminal;
    PrintWriter writer;
    LineReader reader;
    BindingReader bindingReader;

    int lineOffset;
    int lastWidth;
    int cursorLR;
    int cursorUD;
    int delimiter;
    KeyMap<Operation> keyMap = new KeyMap<>();

    enum Operation {LEFT, RIGHT, UP, DOWN;};

    Size terminalSize;
    LangFile file;
    Display keyValueBox;
    Editor(LangFile temp) {
        file = temp;
    }

    List<Object> treeMapData;
    List<AttributedString> parsedTreeMapData;

    public void initializeEditor() throws IOException {
        terminal = TerminalBuilder.builder().name("BTA Language Pack Editor").system(true).type("ansi").build();
        keyValueBox = new Display(terminal, false);
        writer = terminal.writer();
        reader = LineReaderBuilder.builder().terminal(terminal).build();
        bindingReader = new BindingReader(terminal.reader());
        treeMapData = file.getTreeMap();
        delimiter = 10;

        keyMap.bind(Operation.LEFT, key(terminal, Capability.key_left));
        keyMap.bind(Operation.RIGHT, key(terminal, Capability.key_right));
        keyMap.bind(Operation.UP, key(terminal, Capability.key_up));
        keyMap.bind(Operation.DOWN, key(terminal, Capability.key_down));

        terminalSize = terminal.getSize();
        if(terminalSize.getColumns() == 0) {
            terminalSize.setColumns(10);
            terminalSize.setRows(10);
        }
        keyValueBox.resize(terminalSize.getRows(),terminalSize.getColumns());
        lineOffset = 0;
        lastWidth = terminalSize.getColumns();

        //print();
        screenDisplay();

//        terminal.handle(Signal.INT, signal -> {
//            // Handle Ctrl+C
//            System.out.println("success");
//        });

        terminal.handle(Signal.WINCH, signal -> {
            terminalSize = terminal.getSize();

            if(Math.abs(terminalSize.getColumns() - lastWidth) > 5) {
                lastWidth = terminalSize.getColumns();
                delimiter = terminalSize.getColumns()/4;
                if(delimiter < 10) {
                    delimiter = 10;
                }
                keyValueBox.resize(terminalSize.getRows(),terminalSize.getColumns());
                //print();
                screenDisplay();
            }
        });

//        terminal.handle(Signal.TSTP, signal -> {
//            // Handle Ctrl+Z (suspend)
//            terminal.pause();
//        });

        while (true) {
            Operation op = bindingReader.readBinding(keyMap);

            switch(op) {
                case LEFT:
                    if(!(cursorLR < delimiter + 2)) {
                        cursorLR--;
                    }
                    break;
                case RIGHT:
                    if(!(cursorLR > terminalSize.getColumns())) {
                        cursorLR++;
                    }
                    break;
                case UP:
                    if(cursorUD < 2 && lineOffset > 0) {
                        lineOffset--;
                        screenDisplay();
                    } else if(cursorUD > 0) {
                        cursorUD--;
                    }
                    break;
                case DOWN:
                    if(cursorUD > terminalSize.getRows() - 4 && lineOffset < parsedTreeMapData.size() - 1 - terminalSize.getRows()) {
                        lineOffset++;
                        screenDisplay();
                    } else if(cursorUD < terminalSize.getRows() - 2) {
                        cursorUD++;
                    }
                    break;
            }

            terminal.puts(Capability.cursor_address, 0,0);
            writer.print(cursorUD + ":" + cursorLR + " ");
            terminal.puts(Capability.cursor_address, cursorUD, cursorLR);

            terminal.flush();
        }

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

//    public void lineEdit(int line) {
//        terminal.puts(InfoCmp.Capability.cursor_address, delimiter, line);
//    }

//    private void print() {
//        clearScreen();
//        List<Object> temp = file.getTreeMap();
//        delimiter = terminalSize.getColumns()/4;
//        keyValueBox.resize(file.fileTreeMap.size(), terminalSize.getColumns());
//        keyValueBox.update(reparser(delimiter, (List<String>) temp.getFirst(), (List<String>) temp.getLast()), 0);
//        writer.flush();
//
//        if(cursorLR < delimiter + 2) {
//            cursorLR = delimiter + 2;
//        }
//    }

    private List<AttributedString> reparser(int delimiterPos, List<String> keys, List<String> values) {
        List<AttributedString> reparseOutput = new ArrayList<>();
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < keys.size(); i++) {
            temp.append(keys.get(i).substring(0,Math.min(keys.get(i).length(),delimiterPos - 1)));
            temp.append(" ");

                if (temp.length() % 2 == 1) {
                    temp.append(" ");
                }
                temp.repeat("- ", Math.max((delimiterPos - temp.length() + 1) / 2, 0));

            temp.append(values.get(i));

            reparseOutput.add(new AttributedString(temp));

            temp.setLength(0);
        }
        return reparseOutput;
    }

    private void screenDisplay() {
        parsedTreeMapData = reparser(delimiter, (List<String>) treeMapData.getFirst(), (List<String>) treeMapData.getLast());

        clearScreen();

        keyValueBox.update(parsedTreeMapData.subList(Math.max(0,lineOffset),Math.min(lineOffset + terminalSize.getRows(), parsedTreeMapData.size() - 1) - 1),0);
        writer.flush();
    }
}
