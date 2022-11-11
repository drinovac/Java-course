package hr.fer.oprpp1.hw04.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class StudentDB {

    public static void main(String[] args) throws IOException {

        StudentDatabase studentDatabase = new StudentDatabase("database.txt");

        while(true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(">> ");
            String query = br.readLine();

            if(query.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if(!query.split(" ")[0].equals("query")) {
                System.out.println("Input must start with \"query\"");
                break;
            }


            QueryParser parser = new QueryParser(query.substring(5, query.length()));



            List<StudentRecord> records = RecordFormatter.getRecords(parser, studentDatabase);
            List<String> output = RecordFormatter.format(records);
            output.forEach(System.out::println);

        }

    }
}
