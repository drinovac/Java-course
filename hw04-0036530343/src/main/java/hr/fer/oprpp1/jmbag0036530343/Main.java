package hr.fer.oprpp1.jmbag0036530343;

public class Main {
    public static void main(String[] args) {

        String jmbag =  "jmbag=\"0000000003\"";

        String[] splitted = jmbag.split("(?i)and");

        for( String s : splitted) {
            System.out.println(s + "i");
        }

    }
}