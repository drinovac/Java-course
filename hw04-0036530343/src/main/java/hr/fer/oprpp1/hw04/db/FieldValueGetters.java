package hr.fer.oprpp1.hw04.db;

/**
 * This class represents field values with implementation of IFielaValueGetter interface for each value
 * of record.
 */
public class FieldValueGetters {

    public static final IFieldValueGetter FIRST_NAME = new IFieldValueGetter() {
        @Override
        public String get(StudentRecord record) {
            return record.getFirstName();
        }
    };

    public static final IFieldValueGetter LAST_NAME = new IFieldValueGetter() {
        @Override
        public String get(StudentRecord record) {
            return record.getLastName();
        }
    };

    public static final IFieldValueGetter JMBAG = new IFieldValueGetter() {
        @Override
        public String get(StudentRecord record) {
            return record.getJmbag();
        }
    };

}
