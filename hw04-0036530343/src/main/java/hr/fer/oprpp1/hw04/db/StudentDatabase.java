package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents database of student records
 */
public class StudentDatabase {

    /**
     * Map for storing records.
     */
    Map<Integer, StudentRecord> studentRecords;

    /**
     * List for reading all lines.
     */
    List<String> list;

    /**
     * Constructor
     * @param path Path of database file
     * @throws IOException
     */
    public StudentDatabase(String path) throws IOException {

        list = Files.readAllLines(Path.of(path));

        this.studentRecords = new HashMap<>();

        for(String s: list) {
            String[] array = s.split("\t");

            if(studentRecords.get(Integer.parseInt(array[0])) != null) {
                System.out.println("Student with this JMBAG already exists in database");
                System.exit(0);
            } else if(Integer.parseInt(array[3]) < 1 || Integer.parseInt(array[3]) > 5) {
                System.out.println("Student with jmbag " + array[0] + " has invalid final grade");
                System.exit(0);
            }

            StudentRecord rec = new StudentRecord(array[0], array[1], array[2], Integer.parseInt(array[3]));
            studentRecords.put(Integer.parseInt(array[0]), rec);
        }


    }

    /**
     * This method gets record in O(1)
     * @param jmbag
     * @return Student record with specified jmbag
     */
    public StudentRecord forJMBAG(String jmbag) {
        return studentRecords.get(Integer.parseInt(jmbag));
    }

    /**
     * This method returns new list of student records that remain after filter
     * @param filter
     * @return List of student records
     */
    public List<StudentRecord> filter(IFilter filter) {

        List<StudentRecord> list = new ArrayList<>();

        for(StudentRecord rec: studentRecords.values()) {
            if(filter.accepts(rec)) {
                list.add(rec);
            }
        }

        return list;
    }




}
