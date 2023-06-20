package hr.fer.oprpp1.hw05.shell;


/**
 * This class represents input splitter.
 */
public class ArgumentSplitter {

    /**
     * First argument in input.
     */
    private String firstArg;
    /**
     * Second argument in input.
     */
    private String secondArg;
    /**
     * Current index in input
     */
    private int currentIndex;
    /**
     * Input in char array.
     */
    private char[] data;

    /**
     * Constructor that calls method for input parsing.
     * @param arguments Input
     */
    public ArgumentSplitter(String arguments) {

        if(arguments.length() != 0){
            parseArguments(arguments);
        }

    }

    /**
     * This method parses input.
     * @param arguments Input
     */
    private void parseArguments(String arguments) {
        this.data = arguments.toCharArray();

        StringBuilder first = new StringBuilder();

        skipSpaces();

        if (data[currentIndex] == '\"') {
            currentIndex++;

            while (currentIndex < data.length && data[currentIndex] != '\"') {
                first.append(data[currentIndex++]);
            }
            currentIndex++;
        } else {

            while (currentIndex < data.length && data[currentIndex] > ' ') {
                first.append(data[currentIndex++]);
            }

        }
        firstArg = first.toString();

        StringBuilder second = new StringBuilder();

        if(currentIndex >= data.length) {
            return;
        }
        skipSpaces();

        if (data[currentIndex] == '\"') {
            currentIndex++;

            while (currentIndex < data.length && data[currentIndex] != '\"') {
                second.append(data[currentIndex++]);
            }
        } else {

            while (currentIndex < data.length && data[currentIndex] > ' ') {
                second.append(data[currentIndex++]);
            }

        }

        secondArg = second.toString();
    }

    /**
     * This method skips spaces in input.
     */
    private void skipSpaces() {
        while(data[currentIndex] <= ' ') {
            currentIndex++;
        }
    }

    public String getFirstArg() {
        return firstArg;
    }
    public String getSecondArg() {
        return secondArg;
    }
    public boolean hasSecondArg() {
        return secondArg != null;
    }
    public boolean hasFirstArg() {
        return firstArg != null;
    }
}
