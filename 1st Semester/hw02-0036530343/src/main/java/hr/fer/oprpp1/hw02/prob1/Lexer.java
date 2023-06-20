package hr.fer.oprpp1.hw02.prob1;

/**
 * This class represents lexer that reads input string and parses it.
 */
public class Lexer {

    /**
     * Input text split into char array.
     */
    private char[] data;      // ulazni tekst
    /**
     * Reference to current token.
     */
    private Token token;      // trenutni token
    /**
     * Index of first unprocessed char.
     */
    private int currentIndex; // indeks prvog neobrađenog znaka

    private LexerState state;
    // konstruktor prima ulazni tekst koji se tokenizira

    /**
     * Constructor that initilizes Lexer with input string.
     * @param text Input string
     */
    public Lexer(String text) {
        this.data = text.toCharArray();
        this.currentIndex = 0;
        this.state = LexerState.BASIC;
    }

    // generira i vraća sljedeći token
    // baca LexerException ako dođe do pogreške

    /**
     * This method generates new instance of token.
     * It throws LexerException if error is occurred.
     *
     * @return Token object of next token.
     */
    public Token nextToken() {

        if (data.length - currentIndex == 0) {
            Token tok = new Token(TokenType.EOF, null);

            this.token = tok;
            currentIndex++;

            return tok;

        } else if (data.length - currentIndex == -1 || data[data.length-1] == '\\') {
            throw new LexerException();
        }

        TokenType type = null;
        Object value = null;
        String s = "";

        if(this.state == LexerState.BASIC) {

            if(data[currentIndex] == '\\') {
                if(Character.isLetter(data[++currentIndex])) {
                    throw new LexerException();
                }
                type = TokenType.WORD;
                while(currentIndex != data.length && (Character.isLetterOrDigit(data[currentIndex]) || data[currentIndex] == '\\')) {
                    if(data[currentIndex] == '\\') {
                        currentIndex++;
                    }
                    s += data[currentIndex++];
                }
                value = s;
            } else if (Character.isLetter(data[currentIndex])) {
                type = TokenType.WORD;
                while(currentIndex != data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\')) {
                    if(data[currentIndex] == '\\') {
                        currentIndex++;
                    }
                    s += data[currentIndex++];
                }
                value = s;
            } else if (Character.isDigit(data[currentIndex])) {
                type = TokenType.NUMBER;
                while(currentIndex != data.length && Character.isDigit(data[currentIndex])) {
                    s += data[currentIndex++];
                }
                if(s.length() > 10) {
                    throw new LexerException();
                }
                value = Long.valueOf(s);
            } else if ((data[currentIndex] >= '!' && data[currentIndex] <= '/')
                    || (data[currentIndex] >= ':' && data[currentIndex] <= '@')) {

                if(data[currentIndex] == '#') {
                    //this.state = LexerState.EXTENDED;
                }
                type = TokenType.SYMBOL;

                value = Character.valueOf(data[currentIndex++]);

            } else {
                while(data[currentIndex] <= ' ' && (data.length != currentIndex+1)) {
                    if(data.length == currentIndex+2) {
                        type = TokenType.EOF;
                        value = null;
                    }
                    currentIndex++;
                }

            }



        } else {

            if(data[currentIndex] <= ' ') {

                while(data[currentIndex] <= ' ' && (data.length != currentIndex+1)) {

                    if(data.length == currentIndex+2) {
                        type = TokenType.EOF;
                        value = null;
                    }
                    currentIndex++;
                }

            } else {

                while (currentIndex < this.data.length && data[currentIndex] != '#' && data[currentIndex] > ' ') {
                    s += data[currentIndex++];
                }
                if(s == ""){
                    type = TokenType.SYMBOL;
                    value = Character.valueOf(data[currentIndex++]);
                } else {
                    type = TokenType.WORD;
                    value = s;
                }
                if(data[currentIndex - 1] == '#')  this.state = LexerState.BASIC;
            }



        }
        if(type == null) {
            return this.nextToken();
        }
        Token tok = new Token(type, value);
        this.token = tok;
        return tok;

    }

    // vraća zadnji generirani token; može se pozivati
    // više puta; ne pokreće generiranje sljedećeg tokena

    /**
     * This method returns reference to this token.
     * @return Private attribute token
     */
    public Token getToken() {
        return this.token;
    }

    public void setState(LexerState state) {
        if(state == null) {
            throw new NullPointerException();
        }
        this.state = state;
    }
}
