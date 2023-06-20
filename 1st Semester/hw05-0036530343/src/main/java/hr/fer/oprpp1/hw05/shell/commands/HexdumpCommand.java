package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ArgumentSplitter;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents implementation of ShellCommand for command hexdump.
 */
public class HexdumpCommand implements ShellCommand {
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
            env.writeln("Hexdump command expects argument.");
            return ShellStatus.CONTINUE;
        }

        if(splitter.hasSecondArg()) {
            env.writeln("Hexdump command expects a single argument.");
            return ShellStatus.CONTINUE;
        }

        Path path = Paths.get(splitter.getFirstArg());


        if(Files.isDirectory(path)) {
            env.writeln("Given argument cannot be directory.");
            return ShellStatus.CONTINUE;
        }

        try (InputStream is = Files.newInputStream(path)) {
            int i = 0;
            while(true) {
                byte[] buff = new byte[16];
                StringBuilder sb = new StringBuilder();
                StringBuilder nameBuilder = new StringBuilder();
                sb.append(String.format("%08d:", 10 * i++));
                int r = is.read(buff);
                if (r < 1) break;
                for(int j = 0; j < 16; j++) {
                    if(buff[j] == 0) {
                        if(j % 8 == 0 && j > 0) {
                            sb.append("|  ");
                        } else {
                            sb.append("   ");
                        }
                    } else {
                        if(j % 8 == 0 && j > 0) {
                            sb.append(String.format("|%02X", buff[j]));
                        } else {
                            sb.append(String.format(" %02X", buff[j]));
                        }
                    }

                    if((char) buff[j] == 0) {
                        nameBuilder.append(" ");
                    } else if((char) buff[j] < 32 || (char) buff[j] > 127) {
                        nameBuilder.append('.');
                    } else {
                        nameBuilder.append((char) buff[j]);
                    }

                }
                sb.append(" | ");
                sb.append(nameBuilder);
                env.writeln(sb.toString());
            }

            return ShellStatus.CONTINUE;

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
        return "hexdump";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> list = new LinkedList<>();
        list.add("The hexdump command expects a single argument: file name, and produces hex-output");
        return list;
    }
}
