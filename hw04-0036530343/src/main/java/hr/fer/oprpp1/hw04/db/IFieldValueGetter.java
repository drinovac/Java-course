package hr.fer.oprpp1.hw04.db;

/**
 * This is functional interface that gets some value from record
 */
public interface IFieldValueGetter {

     String get(StudentRecord record);
}
