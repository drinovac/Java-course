package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class represents implementation of ShellCommand for command ls.
 */
public class LsCommand implements ShellCommand {
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
            env.writeln("Ls command takes one argument");
            return ShellStatus.CONTINUE;
        }

        if(splitter.hasSecondArg()) {
            env.writeln("Ls command takes only one argument.");
            return ShellStatus.CONTINUE;
        }

        Path pathArg = Paths.get(splitter.getFirstArg());

        if(!Files.isDirectory(pathArg)) {
            env.writeln("Given argument must be directory");
            return ShellStatus.CONTINUE;
        }

        try {
            Files.list(pathArg).forEach(path -> {

                StringBuilder sb = new StringBuilder();

                sb.append(drwx(path));
                sb.append(" ");
                sb.append(sizeInBytes(path));
                sb.append(" ");
                sb.append(creationDateTime(path));
                sb.append(" ");
                sb.append(fileName(path));

                env.writeln(sb.toString());

            });
            return ShellStatus.CONTINUE;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method extracts file name from path.
     * @param path
     * @return
     */
    private String fileName(Path path) {
        return path.getFileName().toString();
    }

    /**
     * This method returns creation date and time of file on given path.
     * @param path
     * @return
     */
    private String creationDateTime(Path path) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
        BasicFileAttributes attributes = null;
        try {
            attributes = faView.readAttributes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileTime fileTime = attributes.creationTime();
        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
        return formattedDateTime;
    }

    /**
     * This method calculates size of file in bytes.
     * @param path
     * @return
     */
    private String sizeInBytes(Path path) {
        long size;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return String.format("%10s", size);
    }

    /**
     * This method checks file's attributes
     * @param path
     * @return
     */
    private String drwx(Path path) {
        StringBuilder sb = new StringBuilder();

        char d = Files.isDirectory(path) ? 'd' : '-';
        sb.append(d);
        char r = Files.isReadable(path) ? 'r' : '-';
        sb.append(r);
        char w = Files.isWritable(path) ? 'w' : '-';
        sb.append(w);
        char x = Files.isExecutable(path) ? 'x' : '-';
        sb.append(x);

        return sb.toString();
    }
    /**
     * This method returns command name.
     * @return
     */
    @Override
    public String getCommandName() {
        return "ls";
    }
    /**
     * This method returns command description as List.
     * @return
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Command ls takes a single argument – directory – and writes a directory listing (not recursive).");
    }
}
