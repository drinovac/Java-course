package hr.fer.oprpp1.hw04.db;

import hr.fer.oprpp1.hw04.db.IComparisonOperator;

public class ComparisonOperators {

    public static final IComparisonOperator LESS = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            return value1.compareTo(value2) < 0 ;
        }
    };

    public static final IComparisonOperator LESS_OR_EQUALS = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            return value1.compareTo(value2) <= 0 ;
        }
    };


    public static final IComparisonOperator GREATER = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            return value1.compareTo(value2) > 0 ;
        }
    };

    public static final IComparisonOperator GREATER_OR_EQUALS = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            return value1.compareTo(value2) >= 0 ;
        }
    };

    public static final IComparisonOperator EQUALS = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            return value1.compareTo(value2) == 0 ;
        }
    };

    public static final IComparisonOperator NOT_EQUALS = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            return value1.compareTo(value2) != 0 ;
        }
    };

    public static final IComparisonOperator LIKE = new IComparisonOperator() {
        @Override
        public boolean satisfied(String value1, String value2) {
            if(value2.contains("*")) {
                if(value2.indexOf('*') == 0) {
                    return value1.endsWith(value2.substring(1));
                } else if(value2.indexOf('*') == value2.length() - 1) {
                    return value1.startsWith(value2.substring(0,value2.length()-1));
                } else {
                    String[] splitted = value2.split("\\*");
                    if(splitted.length != 2) return false;
                    return value1.startsWith(splitted[0]) && value1.endsWith(splitted[1]) && value1.length() + 1 >= value2.length();
                }

            } else {
                return value1.startsWith(value2);
            }

        }
    };
}
