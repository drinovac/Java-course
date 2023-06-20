package hr.fer.oprpp1.hw05.shell;

import java.io.IOException;
import java.util.SortedMap;

public interface Environment {
    /**
     * This method reads line from input.
     * @return Input String
     * @throws ShellIOException
     * @throws IOException
     */
    String readLine() throws ShellIOException, IOException;

    /**
     * This method writes text to output.
     * @param text
     * @throws ShellIOException
     */
    void write(String text) throws ShellIOException;

    /**
     * This method writes line of text to output.
     * @param text
     * @throws ShellIOException
     */
    void writeln(String text) throws ShellIOException;

    /**
     * This method returns commands.
     * @return commands as unmodified SortedMap
     */
    SortedMap<String, ShellCommand> commands();
    Character getMultilineSymbol();
    void setMultilineSymbol(Character symbol);
    Character getPromptSymbol();
    void setPromptSymbol(Character symbol);
    Character getMorelinesSymbol();
    void setMorelinesSymbol(Character symbol);

}
