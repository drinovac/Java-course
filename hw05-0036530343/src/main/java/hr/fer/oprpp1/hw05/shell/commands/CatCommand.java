package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents implementation of ShellCommand for command cat.
 */
public class CatCommand implements ShellCommand {

    /**
     * This method executes command with given arguments.
     * @param env
     * @param arguments
     * @return ShellStatus.CONTINUE if execution is done successfully
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        ArgumentSplitter splitter = new ArgumentSplitter(arguments);

        if(!splitter.hasFirstArg()) {
            env.writeln("Cat command expects one argument.");
            return ShellStatus.CONTINUE;
        }


        Path path = Paths.get(splitter.getFirstArg());
        Charset charset;
        try {
            charset = splitter.hasSecondArg() ? Charset.forName(splitter.getSecondArg()) : Charset.defaultCharset();
        } catch (UnsupportedCharsetException exception) {
            env.writeln("Charset does not exist");
            return ShellStatus.CONTINUE;
        }

        if(!Files.exists(path)) {
            env.writeln("File does not exist.");
            return ShellStatus.CONTINUE;
        }

        try(BufferedReader br = Files.newBufferedReader(path, charset)) {

            while(true) {
                String redak = br.readLine();
                if (redak == null) break;
                env.writeln(redak);
            }

        } catch (IOException exception) {

        }

        return ShellStatus.CONTINUE;
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "cat";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("Command cat takes one or two arguments.");
        list.add("The first argument is path to some file and is mandatory.");
        list.add("The second argument is charset name that should be used to interpret chars from bytes.");
        list.add("If not provided, a default platform charset should be used (see java.nio.charset.Charset class for details).");
        list.add("This command opens given file and writes its content to console.");
        return list;
    }
}
