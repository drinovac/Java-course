package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class StudentDatabaseTest {

    @Test
    public void testForJMBAG() throws IOException {
        class AlwaysTrue implements IFilter{
            @Override
            public boolean accepts(StudentRecord record) {
                return true;
            }
        }
        class AlwaysFalse implements IFilter{
            @Override
            public boolean accepts(StudentRecord record) {
                return false;
            }
        }

        StudentDatabase base = new StudentDatabase("database.txt");

        assertEquals(base.list.size(), base.filter(new AlwaysTrue()).size());
        assertEquals(0, base.filter(new AlwaysFalse()).size());

    }

    @Test
    public void testComarasionOperatorLESS() {
        IComparisonOperator oper = ComparisonOperators.LESS;
        assertTrue(oper.satisfied("Ante", "Borna"));
    }

    @Test
    public void testComarasionOperatorLESS_OR_EQUALS() {
        IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
        assertTrue(oper.satisfied("Ante", "Borna"));
        assertTrue(oper.satisfied("Ante", "Ante"));
    }

    @Test
    public void testComarasionOperatorGREATER() {
        IComparisonOperator oper = ComparisonOperators.GREATER;
        assertTrue(oper.satisfied("Borna", "Ante"));
    }

    @Test
    public void testComarasionOperatorGREATER_OR_EQUALS() {
        IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
        assertTrue(oper.satisfied("Borna", "Ante"));
        assertTrue(oper.satisfied("Borna", "Borna"));
    }

    @Test
    public void testComarasionOperatorEQUALS() {
        IComparisonOperator oper = ComparisonOperators.EQUALS;
        assertTrue(oper.satisfied("Borna", "Borna"));
    }

    @Test
    public void testComarasionOperatorNOT_EQUALS() {
        IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
        assertTrue(oper.satisfied("Borna", "Ante"));
    }

    @Test
    public void testComarasionOperatorLIKE() {
        IComparisonOperator oper = ComparisonOperators.LIKE;
        assertTrue(oper.satisfied("AAAA", "AA*AA"));
        assertFalse(oper.satisfied("Zagreb", "Aba*"));
        assertFalse(oper.satisfied("AAA", "AA*AA"));
    }

    @Test
    public void testFieldValueGettersFIRST_NAME() throws IOException {
        StudentDatabase base = new StudentDatabase("database.txt");
        StudentRecord record = base.forJMBAG("0000000001");

        assertEquals("Marin", FieldValueGetters.FIRST_NAME.get(record));
        assertEquals("Akšamović", FieldValueGetters.LAST_NAME.get(record));
        assertEquals("0000000001", FieldValueGetters.JMBAG.get(record));
    }

    @Test
    public void testConditionalExpression() throws IOException {
        StudentDatabase base = new StudentDatabase("database.txt");
        StudentRecord record = base.forJMBAG("0000000001");

        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Akš*",
                ComparisonOperators.LIKE
        );

        assertTrue(expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record), // returns lastName from given record
                expr.getStringLiteral()             // returns "Bos*"
        ));
    }

    @Test
    public void testQueryParser() {
        QueryParser qp1 = new QueryParser(" jmbag       =\"0123456789\"    ");

        assertTrue(qp1.isDirectQuery());
        assertEquals("0123456789", qp1.getQueriedJMBAG());
        assertEquals(1, qp1.getQuery().size());

        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        assertFalse(qp2.isDirectQuery());
        assertThrows(IllegalStateException.class, () -> qp2.getQueriedJMBAG());
        assertEquals(2, qp2.getQuery().size());


    }

    @Test
    public void testQueryFilter() throws IOException {
        StudentDatabase base = new StudentDatabase("database.txt");
        QueryParser parser = new QueryParser("jmbag=\"0000000003\"");

        List<StudentRecord> list = base.filter(new QueryFilter(parser.getQuery()));

        assertEquals(list.get(0).getJmbag(), "0000000003");
        assertEquals(list.get(0).getFirstName(),  "Andrea");
        assertEquals(list.get(0).getLastName(),  "Bosnić");
        assertEquals(list.get(0).getFinalGrade(), 4);

    }

}
