package hr.fer.oprpp1.hw04.db;

/**
 * This is functional interface that decides if student record is acceptable
 */
public interface IFilter {
    public boolean accepts(StudentRecord record);
}
