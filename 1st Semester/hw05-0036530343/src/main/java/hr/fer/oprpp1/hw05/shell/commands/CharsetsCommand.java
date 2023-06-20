package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentSplitter;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * This class represents implementation of ShellCommand for command charsets.
 */
public class CharsetsCommand implements ShellCommand {
    /**
     * This method executes command with given arguments.
     * @param env
     * @param arguments
     * @return ShellStatus.CONTINUE if execution is done successfully
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        ArgumentSplitter splitter = new ArgumentSplitter(arguments);

        if(splitter.hasFirstArg() || splitter.hasSecondArg()) {
            env.writeln("The syntax of the command is incorrect.");
            return ShellStatus.CONTINUE;
        }

        SortedMap<String, Charset> map = Charset.availableCharsets();

        for(Map.Entry<String, Charset> entry: map.entrySet()) {
            env.writeln(entry.getKey());
        }

        return ShellStatus.CONTINUE;
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "charsets";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("Command charsets takes no arguments and lists names of supported charsets for your Java platform.");
        list.add("A single charset name is written per line.");
        return list;
    }
}
