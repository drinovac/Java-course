package hr.fer.oprpp1.hw04.db;

public class ConditionalExpression {

    private IFieldValueGetter fieldGetter;
    private String stringLiteral;
    private IComparisonOperator comparisonOperator;

    public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.stringLiteral = stringLiteral;
        this.comparisonOperator = comparisonOperator;
    }

    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    public String getStringLiteral() {
        return stringLiteral;
    }

    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
