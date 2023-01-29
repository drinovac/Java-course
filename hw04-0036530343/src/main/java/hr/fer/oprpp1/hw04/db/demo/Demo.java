package hr.fer.oprpp1.hw04.db.demo;

import hr.fer.oprpp1.hw04.db.*;

import java.io.IOException;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws IOException {

        StudentDatabase base = new StudentDatabase("database.txt");
        StudentRecord rec = base.forJMBAG("0000000001");
        System.out.println(rec.getJmbag());



        IComparisonOperator oper = ComparisonOperators.LIKE;
        System.out.println(oper.satisfied("Zagreb", "Aba*"));  // false
        System.out.println(oper.satisfied("AAA", "AA*AA"));    // false
        System.out.println(oper.satisfied("AAAA", "AA*AA"));   // true

        System.out.println();

        StudentRecord record = rec;
        System.out.println("First name: " + FieldValueGetters.FIRST_NAME.get(record));
        System.out.println("Last name: " + FieldValueGetters.LAST_NAME.get(record));
        System.out.println("JMBAG: " + FieldValueGetters.JMBAG.get(record));

        System.out.println();

        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Akš*",
                ComparisonOperators.LIKE
        );

        boolean recordSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record), // returns lastName from given record
                expr.getStringLiteral()             // returns "Bos*"
        );

        System.out.println(recordSatisfies);


        QueryParser qp1 = new QueryParser(" jmbag       =\"0123456789\"    ");
        System.out.println("isDirectQuery(): " + qp1.isDirectQuery()); // true
        System.out.println("jmbag was: " + qp1.getQueriedJMBAG()); // 0123456789
        System.out.println("size: " + qp1.getQuery().size()); // 1
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        System.out.println("isDirectQuery(): " + qp2.isDirectQuery()); // false //
        //System.out.println(qp2.getQueriedJMBAG()); // would throw!
        System.out.println("size: " + qp2.getQuery().size()); // 2



        System.out.println();

        StudentDatabase db = base;
        QueryParser parser = new QueryParser("firstName>\"A\" and lastName LIKE \"B*ć\"");
        if(parser.isDirectQuery()) {
            StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());

            System.out.println(r.getJmbag() + " " + r.getLastName() + " " + r.getFirstName());

        } else {
            for(StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
                System.out.println(r.getJmbag() + " " + r.getLastName() + " " + r.getFirstName());
            }
        }


    }

}
