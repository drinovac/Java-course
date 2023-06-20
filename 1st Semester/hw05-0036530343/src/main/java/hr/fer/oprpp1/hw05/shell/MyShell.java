package hr.fer.oprpp1.hw05.shell;

import java.io.IOException;

/**
 * This class represents program that starts MyShell.
 */
public class MyShell {

    public static void main(String[] args) throws IOException {

        ShellEnviornment env = new ShellEnviornment();
        ShellStatus status = ShellStatus.CONTINUE;
        while (status == ShellStatus.CONTINUE){

            System.out.print(env.getPromptSymbol() + " ");
            String l = env.readLine();

            String[] arr = l.split(" ");
            try {
                ShellCommand command = env.commands().get(arr[0]);
                String arguments = arr.length > 1 ? l.substring(arr[0].length() + 1) : "";
                status = command.executeCommand(env, arguments);
            } catch (NullPointerException exception) {
                env.writeln("'" + arr[0] + "' is not recognized as command.");
            }

        }

    }

}
