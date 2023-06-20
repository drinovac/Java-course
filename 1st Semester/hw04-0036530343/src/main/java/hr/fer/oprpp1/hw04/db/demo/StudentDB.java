package hr.fer.oprpp1.hw04.db.demo;

import hr.fer.oprpp1.hw04.db.QueryParser;
import hr.fer.oprpp1.hw04.db.RecordFormatter;
import hr.fer.oprpp1.hw04.db.StudentDatabase;
import hr.fer.oprpp1.hw04.db.StudentRecord;

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
            String[] showingParams = null;

            if(query.substring(5).contains("showing")) {
                showingParams =  query.substring(5).split("showing")[1].split(",");

                for(int i = 0; i< showingParams.length; i++) {
                    showingParams[i] = showingParams[i].substring(1);
                }
            }


            QueryParser parser = new QueryParser(query.substring(5, query.length()));



            List<StudentRecord> records = RecordFormatter.getRecords(parser, studentDatabase);
            List<String> output = RecordFormatter.format(records, showingParams);
            output.forEach(System.out::println);

        }

    }
}
