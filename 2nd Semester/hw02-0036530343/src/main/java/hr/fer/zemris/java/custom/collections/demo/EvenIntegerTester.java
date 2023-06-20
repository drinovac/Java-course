package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

public class EvenIntegerTester implements Tester {

    /**
     * This method checks if some object is acceptable or not.
     *
     * @param obj Object we want to check
     * @return True if object is acceptable, false otherwise
     */
    @Override
    public boolean test(Object obj) {
        if(!(obj instanceof Integer)) return false;
        Integer i = (Integer)obj;
        return i % 2 == 0;
    }

}
