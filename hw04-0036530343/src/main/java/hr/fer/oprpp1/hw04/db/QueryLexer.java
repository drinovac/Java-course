package hr.fer.oprpp1.hw04.db;

public class QueryLexer {

    private String input;

    private ConditionalExpression conditionalExpression;

    private IFieldValueGetter fieldValueGetter;

    private String stringLiteral;
    private IComparisonOperator comparisonOperator;

    private boolean isJmbag = false;
    private boolean isEquals = false;
    private char[] array;
    private int index = 0;
    public QueryLexer(String input) {
        array = input.toCharArray();

        skipSpaces();

        String leftValue = "";

        while ((array[index] >= 'A' && array[index] <= 'Z') || (array[index] >= 'a' && array[index] <= 'z')) {
            leftValue += array[index++];
        }
        if(leftValue.equals("firstName")) {
            fieldValueGetter = FieldValueGetters.FIRST_NAME;
        } else if(leftValue.equals("lastName")) {
            fieldValueGetter = FieldValueGetters.LAST_NAME;
        } else if(leftValue.equals("jmbag")) {
            fieldValueGetter = FieldValueGetters.JMBAG;
            isJmbag = true;
        } else {
            System.out.println("Wrong input");
            System.exit(0);
        }

        skipSpaces();

        if(array[index] == '>' && array[index + 1] != '=') {
            comparisonOperator = ComparisonOperators.GREATER;
        } else if(array[index] == '<' && array[index + 1] != '=') {
            comparisonOperator = ComparisonOperators.LESS;
        } else if(array[index] == '>' && array[index + 1] == '=') {
            comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
        } else if(array[index] == '<' && array[index + 1] == '=') {
            comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
        } else if(array[index] == '=') {
            comparisonOperator = ComparisonOperators.EQUALS;
            isEquals = true;
        } else if(array[index] == '!' && array[index + 1] == '=') {
            comparisonOperator = ComparisonOperators.NOT_EQUALS;
        } else if(array[index] == 'L') {
            while(array[index] > 'A' && array[index] < 'Z') {
                index++;
            }
            index--;
            comparisonOperator = ComparisonOperators.LIKE;
        } else {
            System.out.println("Wrong input");
            System.exit(0);
        }

        index++;

        skipSpaces();

        stringLiteral = "";

        index++;

        while(array[index] != '\"') {
            stringLiteral += array[index++];
        }

    }

    private void skipSpaces() {
        while(array[index] == ' ') {
            index++;
        }
    }

    public ConditionalExpression getConditionalExpression() {
        return new ConditionalExpression(fieldValueGetter, stringLiteral, comparisonOperator);
    }

    public boolean isDirect() {
        return isEquals && isJmbag;
    }
}
