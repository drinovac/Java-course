package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentSplitter;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

/**
 * This class represents implementation of ShellCommand for command help.
 */
public class HelpCommand implements ShellCommand {
    /**
     * This method executes command with given arguments.
     * @param env
     * @param arguments
     * @return ShellStatus.CONTINUE if execution is done successfully
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        ArgumentSplitter splitter = new ArgumentSplitter(arguments);

        if(splitter.hasSecondArg()) {
            env.writeln("Help command expects only one argument.");
            return ShellStatus.CONTINUE;
        }

        String command = splitter.getFirstArg();

        SortedMap<String, ShellCommand> map = env.commands();

        if(!splitter.hasFirstArg()) {
            map.forEach((k,v) -> env.writeln(k));
        } else {
            try {
                map.get(command).getCommandDescription().forEach(element -> {
                    env.writeln(element);
                });
            } catch (NullPointerException exception) {
                env.writeln("Command given as arguments does not exists.");
                return ShellStatus.CONTINUE;
            }
        }
        return ShellStatus.CONTINUE;
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "help";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("If started with no arguments, it lists names of all supported commands.");
        list.add("If started with single argument, it prints name and the description of selected command");
        return null;
    }
}
