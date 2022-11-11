package hr.fer.oprpp1.hw04.db;

import java.util.List;

public class QueryFilter implements IFilter {

    private List<ConditionalExpression> conditionalExpressionList;

    public QueryFilter(List<ConditionalExpression> conditionalExpressionList) {
        this.conditionalExpressionList = conditionalExpressionList;
    }

    @Override
    public boolean accepts(StudentRecord record) {
        int cnt = 0;
        for(ConditionalExpression expression: conditionalExpressionList) {
            if(expression.getComparisonOperator().satisfied(
                    expression.getFieldGetter().get(record),
                    expression.getStringLiteral()
            )) {
                cnt++;
            }
        }
        return cnt == conditionalExpressionList.size();
    }
}
