package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
/**
 * This class represents implementation of ShellCommand for command tree.
 */
public class TreeCommand implements ShellCommand {
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
            env.writeln("Tree command expects single argument.");
            return ShellStatus.CONTINUE;
        }

        if(splitter.hasSecondArg()) {
            env.writeln("Tree command takes only one argument.");
            return ShellStatus.CONTINUE;
        }

        Path path = Paths.get(splitter.getFirstArg());

        if(!Files.isDirectory(path)) {
            env.writeln("Given path must be directory.");
            return ShellStatus.CONTINUE;
        }

        listDir(env, path, 0);

        return ShellStatus.CONTINUE;

    }

    /**
     * This method is used for recursively directory listing.
     * @param env
     * @param dir
     * @param spacing
     */
    private void listDir(Environment env, Path dir, int spacing) {
        try {
            Files.list(dir).forEach(path -> {

                if(Files.isDirectory(path)) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < spacing; i++) {
                        sb.append("-");
                    }
                    sb.append(path.getFileName().toString());
                    env.writeln(sb.toString());
                    listDir(env, path, spacing + 2);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "tree";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("The tree command expects a single argument: directory name and prints a tree (each directory level shifts output two charatcers to the right)");
    }
}
