package hr.fer.oprpp1.hw04.db;

/**
 * This class represents tuple for expression that is used for filtering student records from database.
 */
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
