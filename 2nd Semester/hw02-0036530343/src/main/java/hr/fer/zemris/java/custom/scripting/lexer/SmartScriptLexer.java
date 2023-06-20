package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

public class SmartScriptLexer {

    private char[] data;

    private SmartScriptToken token;

    private SmartScriptLexerState state;
    private int currentIndex;


    public SmartScriptLexer(String text) {
        Objects.requireNonNull(text, "Data ne smije biti null!");

        this.data = text.toCharArray();
        this.state = SmartScriptLexerState.TEXT;
    }

    public SmartScriptToken nextToken() {
        if(state == SmartScriptLexerState.TAG) {
            return parseTag();
        } else if(state == SmartScriptLexerState.TEXT) {
            return parseText();
        } else {
            throw new SmartScriptLexerException("State nije poznat");
        }

    }

    private SmartScriptToken parseText() {
        if (token != null && token.getType() == SmartScriptTokenType.EOF) {
            throw new SmartScriptLexerException();
        }

        StringBuilder sb = new StringBuilder();

        while (true) {
            if (currentIndex == data.length) {
                break;
            }

            if (data[currentIndex] == '\\') {
                currentIndex++;
                checkIndex();

                if (data[currentIndex] == '\\' || data[currentIndex] == '{') {
                    sb.append(data[currentIndex++]);
                } else {
                    throw new SmartScriptLexerException("Wrong escaping");
                }
            } else if (data[currentIndex] == '{') {

                if(sb.length() > 0) {
                    break;
                }

                sb.append(data[currentIndex++]);
                checkIndex();

                if(data[currentIndex] == '$') {
                    sb.append(data[currentIndex++]);
                    break;
                } else {
                    throw new SmartScriptLexerException();
                }
            } else {
                sb.append(data[currentIndex++]);
            }

        }

        if (sb.length() == 0) {
            token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
        } else {
            if (sb.toString().equals("{$")) {
                token = new SmartScriptToken(SmartScriptTokenType.BEGINTAG, sb.toString());
            } else {
                token = new SmartScriptToken(SmartScriptTokenType.TEXT, sb.toString());
            }
        }

        return token;
    }

    private SmartScriptToken parseTag() {

        if (token.getType() == SmartScriptTokenType.EOF && token != null) {
            throw new SmartScriptLexerException();
        }

        StringBuilder sb = new StringBuilder();
        SmartScriptTokenType currentType = SmartScriptTokenType.EOF;

        boolean insideQuotes = false;

        skipSpace();

        if (currentIndex < data.length && data[currentIndex] == '=') {
            token = new SmartScriptToken(SmartScriptTokenType.TEXT, "=");
            currentIndex++;
            return token;
        }

        while (true) {

            if (data.length == currentIndex) {
                break;
            }

            if(insideQuotes) {

                if (data[currentIndex] == '\\') {
                    currentIndex++;
                    checkIndex();

                    if(data[currentIndex] == '\\' || data[currentIndex] == '"') {
                        sb.append(data[currentIndex++]);
                    } else if (data[currentIndex] == 'n') {
                        sb.append("\n");
                        currentIndex++;
                    } else if (data[currentIndex] == 'r') {
                        sb.append("\r");
                        currentIndex++;
                    } else if (data[currentIndex] == 't') {
                        sb.append("\t");
                        currentIndex++;
                    } else {
                        throw new SmartScriptLexerException();
                    }

                } else if (data[currentIndex] == '"') {
                    sb.append(data[currentIndex++]);
                    break;
                } else {
                    sb.append(data[currentIndex++]);
                }

            } else if (data[currentIndex] == '$') {

                if (currentType != SmartScriptTokenType.EOF) {
                    break;
                }

                sb.append(data[currentIndex++]);
                checkIndex();

                if (data[currentIndex] == '}') {
                    sb.append(data[currentIndex++]);
                    currentType = SmartScriptTokenType.ENDTAG;
                    break;
                } else {
                    throw new SmartScriptLexerException();
                }
            } else if (data[currentIndex] == '"') {

                if (currentType == SmartScriptTokenType.EOF) {
                    currentType = SmartScriptTokenType.TEXT;
                } else if (currentType != SmartScriptTokenType.TEXT) {
                    break;
                }
                insideQuotes = true;
                sb.append(data[currentIndex++]);
            } else if (data[currentIndex] == '.' && currentType == SmartScriptTokenType.INTEGER) {
                sb.append(data[currentIndex++]);
                currentType = SmartScriptTokenType.DOUBLE;
            } else if (Character.isDigit(data[currentIndex])) {

                if (currentType == SmartScriptTokenType.EOF) {
                    currentType = SmartScriptTokenType.INTEGER;
                } else if (!(currentType == SmartScriptTokenType.INTEGER
                        || currentType == SmartScriptTokenType.DOUBLE)) {
                    break;
                }

                sb.append(data[currentIndex++]);
            } else if (data[currentIndex] <= ' ') {
                break;
            } else {
                if(currentType == SmartScriptTokenType.EOF) {
                    currentType = SmartScriptTokenType.TEXT;
                } else if (currentType != SmartScriptTokenType.TEXT) {
                    break;
                }

                sb.append(data[currentIndex++]);
            }

        }

        if (currentType == SmartScriptTokenType.TEXT) {
            token = new SmartScriptToken(currentType, sb.toString());
        } else if(currentType == SmartScriptTokenType.INTEGER) {
            try {
                int num = Integer.parseInt(sb.toString());
                token = new SmartScriptToken(currentType, num);
            } catch (NumberFormatException exc) {
                throw new SmartScriptLexerException();
            }
        } else if (currentType == SmartScriptTokenType.DOUBLE) {
            try {
                double num = Double.parseDouble(sb.toString());
                token = new SmartScriptToken(currentType, num);
            } catch (NumberFormatException exc) {
                throw new SmartScriptLexerException();
            }
        } else if (currentType  == SmartScriptTokenType.ENDTAG) {
            token = new SmartScriptToken(currentType, sb.toString());
        } else if (currentType == SmartScriptTokenType.EOF) {
            token = new SmartScriptToken(currentType, sb.toString());
        }

        return token;
    }

    private void checkIndex() {
        if (currentIndex >= data.length) {
            throw new SmartScriptLexerException("Current index out of range");
        }
    }


    private void skipSpace() {
        while (true) {
            if (currentIndex < data.length && data[currentIndex] <= ' ') {
                currentIndex++;
            } else {
                break;
            }
        }
    }

    public SmartScriptLexerState getState() {
        return state;
    }

    public void setState(SmartScriptLexerState state) {
        this.state = state;
    }
}
