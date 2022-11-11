package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

public class QueryParser {

    private boolean directQuery = false;

    private List<ConditionalExpression> query;

    public QueryParser(String input) {

        String[] splitted = input.split("(?i)and");

        query = new ArrayList<>();

        for(String s : splitted) {
            QueryLexer lex = new QueryLexer(s);
            directQuery = lex.isDirect();
            query.add(lex.getConditionalExpression());
        }

    }

    public boolean isDirectQuery() {
        return this.query.size() == 1 && directQuery;
    }

    public String getQueriedJMBAG() {
        if(isDirectQuery()) {
            return this.query.get(0).getStringLiteral();
        } else {
            throw new IllegalStateException();
        }

    }

    public List<ConditionalExpression> getQuery() {
        return this.query;
    }

}
