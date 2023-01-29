package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.io.*;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class represents ShellEnviornment which is used for communication with user.
 */
public class ShellEnviornment implements Environment {

    /**
     * Commands storage.
     */
    private SortedMap<String, ShellCommand> sortedMap;
    /**
     * Prompt symbol if input span across multiple lines.
     */
    private Character multilineSymbol = '|';
    /**
     * Propmt symbol.
     */
    private Character promptSymbol = '>';
    /**
     * Symbol used to inform shell that more lines as expected.
     */
    private Character morelinesSymbol = '\\';

    /**
     * Constructor that fills command storage with its definition.
     */
    public ShellEnviornment() {
        System.out.println("Welcome to MyShell v 1.0");
        sortedMap = new TreeMap<>();
        putCommands();
    }

    /**
     * This method fills command storage.
     */
    private void putCommands() {
        ShellCommand charset = new CharsetsCommand();
        sortedMap.put(charset.getCommandName(), charset);
        ShellCommand cat = new CatCommand();
        sortedMap.put(cat.getCommandName(), cat);
        ShellCommand copy = new CopyCommand();
        sortedMap.put(copy.getCommandName(), copy);
        ShellCommand ls = new LsCommand();
        sortedMap.put(ls.getCommandName(), ls);
        ShellCommand mkdir = new MkdirCommand();
        sortedMap.put(mkdir.getCommandName(), mkdir);
        ShellCommand tree = new TreeCommand();
        sortedMap.put(tree.getCommandName(), tree);
        ShellCommand exit = new ExitCommand();
        sortedMap.put(exit.getCommandName(), exit);
        ShellCommand symbol = new SymbolCommand();
        sortedMap.put(symbol.getCommandName(), symbol);
        ShellCommand hexdump = new HexdumpCommand();
        sortedMap.put(hexdump.getCommandName(), hexdump);
        ShellCommand help = new HelpCommand();
        sortedMap.put(help.getCommandName(), help);
    }

    /**
     * This method reads line from input.
     * @return Input String
     * @throws ShellIOException
     * @throws IOException
     */
    @Override
    public String readLine() throws ShellIOException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();

        while(line.endsWith(morelinesSymbol.toString())) {
            sb.append(line, 0, line.length() - 1);
            write(multilineSymbol + " ");
            line = br.readLine();
        }

        sb.append(line);
        return sb.toString();

    }
    /**
     * This method writes text to output.
     * @param text
     * @throws ShellIOException
     */
    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    /**
     * This method writes line of text to output.
     * @param text
     * @throws ShellIOException
     */
    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    /**
     * This method returns commands.
     * @return commands as unmodified SortedMap
     */
    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(sortedMap);
    }

    @Override
    public Character getMultilineSymbol() {
        return this.multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        this.multilineSymbol = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return this.promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.promptSymbol = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return this.morelinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.morelinesSymbol = symbol;
    }
}
