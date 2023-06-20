package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.*;
import hr.fer.oprpp1.custom.collections.Collection;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // create collection:
        /*SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);

        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana



        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        System.out.printf("Veličina: %d%n", examMarks.size());*/


        Collection<String> prva = new ArrayIndexedCollection<>();
        Collection<Object> druga = new ArrayIndexedCollection<>();
        prva.add("Ivo");
        prva.add("Ivka");
        prva.copyTransformedIntoIfAllowed(druga, Object::hashCode, n -> Integer.valueOf((String) n) % 2==0);
    }


}