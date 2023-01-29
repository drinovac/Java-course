package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

/**
 * This class represents implementation of ShellCommand for command exit.
 */
public class ExitCommand implements ShellCommand {
    /**
     * This method executes command with given arguments.
     * @param env
     * @param arguments
     * @return ShellStatus.CONTINUE if execution is done successfully
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "exit";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        return null;
    }
}
