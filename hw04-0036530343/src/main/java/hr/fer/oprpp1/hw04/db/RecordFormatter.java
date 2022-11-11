package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecordFormatter {

    public static List<StudentRecord> getRecords(QueryParser parser, StudentDatabase db) {
        List<StudentRecord> list = new ArrayList<>();

        if(parser.isDirectQuery()) {
            StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
            list.add(r);
        } else {
            for(StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
                list.add(r);
            }
        }

        return list;
    }

    public static List<String> format(List<StudentRecord> list) {

        List<String> return_list = new ArrayList<>();

        int max_lastName = 0;
        int max_firstName = 0;

        for(StudentRecord rec : list) {
            if(rec.getFirstName().length() > max_firstName) {
                max_firstName = rec.getFirstName().length();
            }
            if(rec.getLastName().length() > max_lastName) {
                max_lastName = rec.getLastName().length();
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("+");
        for(int i = 0; i < 12; i++) {
            stringBuilder.append("=");
        }
        stringBuilder.append("+");
        for(int i = 0, h = max_lastName + 2; i < h; i++) {
            stringBuilder.append("=");
        }
        stringBuilder.append("+");
        for(int i = 0, h = max_firstName + 2; i < h; i++) {
            stringBuilder.append("=");
        }
        stringBuilder.append("+");
        for(int i = 0; i < 3; i++) {
            stringBuilder.append("=");
        }
        stringBuilder.append("+");

        for(StudentRecord rec: list) {
            StringBuilder s = new StringBuilder();

            s.append("| ");
            s.append(rec.getJmbag());
            s.append(" | ");
            s.append(rec.getLastName());
            for(int i = 0; i < max_lastName - rec.getLastName().length(); i++) {
                s.append(" ");
            }
            s.append(" | ");
            s.append(rec.getFirstName());
            for(int i = 0; i < max_firstName - rec.getFirstName().length(); i++) {
                s.append(" ");
            }
            s.append(" | ");
            s.append(rec.getFinalGrade());
            s.append(" |");

            return_list.add(s.toString());
        }

        if(return_list.size() > 0) {
            return_list.add(0,stringBuilder.toString());
            return_list.add(stringBuilder.toString());
        }

        int size = return_list.size() == 0 ? 0 : return_list.size() - 2;

        return_list.add("Records selected: " + size);

        return return_list;
    }
}
