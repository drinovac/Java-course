package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentSplitter;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class represents implementation of ShellCommand for command mkdir.
 */
public class MkdirCommand implements ShellCommand {
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
            env.writeln("Mkdir command expects single argument.");
            return ShellStatus.CONTINUE;
        }

        if(splitter.hasSecondArg()) {
            env.writeln("Mkdir command takes only one argument.");
            return ShellStatus.CONTINUE;
        }

        String dirName = splitter.getFirstArg();

        try {
            Files.createDirectories(Paths.get(dirName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ShellStatus.CONTINUE;
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "mkdir";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("The mkdir command takes a single argument: directory name, and creates the appropriate directory structure.");
    }
}
