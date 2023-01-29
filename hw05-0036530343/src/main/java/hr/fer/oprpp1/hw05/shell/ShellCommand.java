package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * Interface that implements all ShellCommands.
 */
public interface ShellCommand {
    /**
     * This method is called when we want to execute command.
     * @param env
     * @param arguments
     * @return
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * This method returns command name.
     * @return
     */
    String getCommandName();

    /**
     * This method returns command description as List.
     * @return
     */
    List<String> getCommandDescription();
}
