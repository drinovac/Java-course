package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentSplitter;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.List;

/**
 * This class represents implementation of ShellCommand for command symbol.
 */
public class SymbolCommand implements ShellCommand {
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
            env.writeln("Symbol command expects argument.");
            return ShellStatus.CONTINUE;
        }

        String first = splitter.getFirstArg();
        String second = splitter.getSecondArg();

        if(!splitter.hasSecondArg()) {
            if(first.equals("PROMPT")) {
                env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
            } else if (first.equals("MORELINES")) {
                env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
            } else if (first.equals("MULTILINE")) {
                env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
            } else {
                env.writeln("Wrong input.");
            }

        } else if (splitter.hasSecondArg()) {
            if (first.equals("PROMPT")) {
                env.writeln("Symbol for PROMPT changed from '" + env.getPromptSymbol() + "' to '" + second + "'");
                env.setPromptSymbol(second.toCharArray()[0]);
            } else if (first.equals("MORELINES")) {
                env.writeln("Symbol for MORELINES changed from '" + env.getMorelinesSymbol() + "' to '" + second + "'");
                env.setMorelinesSymbol(second.toCharArray()[0]);
            } else if (first.equals("MULTILINE")) {
                env.writeln("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + "' to '" + second + "'");
                env.setMultilineSymbol(second.toCharArray()[0]);
            } else {
                env.writeln("Wrong input.");
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
        return "symbol";
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
