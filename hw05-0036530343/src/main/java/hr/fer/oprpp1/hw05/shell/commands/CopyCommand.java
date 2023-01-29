package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents implementation of ShellCommand for command copy.
 */
public class CopyCommand implements ShellCommand {
    /**
     * This method executes command with given arguments.
     * @param env
     * @param arguments
     * @return ShellStatus.CONTINUE if execution is done successfully
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        ArgumentSplitter splitter = new ArgumentSplitter(arguments);

        if(!splitter.hasFirstArg() || !splitter.hasSecondArg()) {
            env.writeln("Copy command expects two arguments.");
            return ShellStatus.CONTINUE;
        }

        Path source = Paths.get(splitter.getFirstArg());
        Path destination = Paths.get(splitter.getSecondArg());

        if(Files.isDirectory(source)) {
            env.writeln("Source path cannot be directory.");
            return ShellStatus.CONTINUE;
        }

        if (Files.isDirectory(destination)) {
            destination = Paths.get(destination + "/" +  source.getFileName().toString());
        }

        if(Files.exists(destination)) {
            env.writeln("File already exists. Overwrite current file?");
            env.writeln("(y/n)");
            String in;
            try {
                in = env.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(!in.trim().equals("y")) {
                return ShellStatus.CONTINUE;
            }
        }


        try (InputStream is = Files.newInputStream(source);
             OutputStream os = Files.newOutputStream(destination)) {

            byte[] buff = new byte[1024];
            while(true) {
                int r = is.read(buff);
                if(r < 1) break;
                os.write(buff, 0, r);
            }

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
        return "copy";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("The copy command expects two arguments: source file name and destination file name (i.e. paths and names).");
        list.add("Is destination file exists, shell should ask user is it allowed to overwrite it.");
        list.add("Your copy command must work only with files (no directories).");
        list.add("If the second argument is directory, shell should assume that user wants to copy the original file into that directory using the original file name.");
        return list;
    }
}
