package hr.fer.zemris.java;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileName = "src/main/webapp/WEB-INF/glasanje-definicija.txt";

        List<String> lines = Files.readAllLines(Paths.get(fileName));

        for (String line: lines) {
            String[] parts = line.split("\\t");

            for(String part: parts) {
                System.out.println(part);
            }
        }


    }
}