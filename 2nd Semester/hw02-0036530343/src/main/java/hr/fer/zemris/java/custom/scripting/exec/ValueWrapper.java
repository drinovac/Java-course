package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

public class ValueWrapper {

    public Object value = 0;
    private Type type;

    public ValueWrapper(Object value) {

        if (value instanceof String && value.toString().endsWith("\"")) {
            value = ((String) value).substring(1, value.toString().length() - 1);
        }

        if(value instanceof String) {
            if(((String) value).endsWith("000")) {
                this.value = value;
                return;
            }
          try {
              this.value =  Integer.parseInt((String) value);
              type = Type.INTEGER;
          } catch (Exception exception) {
              try {
                  this.value = Double.parseDouble((String) value);
                  type = Type.DOUBLE;
              } catch (Exception ignored) {
                  this.value = value;
              }
          }
        } else {
            if (value instanceof Integer) {
                type = Type.INTEGER;
            } else if (value instanceof Double) {
                type = Type.DOUBLE;
            }
            this.value = value;
        }
    }

    public Object getValue() {
        return value;
    }
    public double doubleValue() {
        if(this.value instanceof Integer) {
            return (double) (Integer) value;
        } else if (this.value instanceof Double) {
            return (double) (Double) value;
        } else if (this.value == null) {
            return 0;
        }
        throw new RuntimeException();
    }

    public void setValue(Object value) {
        getType(value);
        this.value = value;
    }

    public void add(Object incValue) {
        operation(incValue, (x,y) -> x + y);
    }

    public void subtract(Object decValue) {
        operation(decValue, (x, y) -> x - y);
    }
    public void multiply(Object mulValue) {
        operation(mulValue, (x, y) -> x * y);
    }
    public void divide(Object divValue) {
        operation(divValue, (x, y) -> x / y);
    }

    public int numCompare(Object withValue) {
        ValueWrapper inValue = new ValueWrapper(withValue);
        return Double.compare(doubleValue(), inValue.doubleValue());
    }

    private void operation(Object value, BiFunction<Double, Double, Double> function) {

        ValueWrapper valueWrapper = new ValueWrapper(value);

        Double result = function.apply(this.doubleValue(), valueWrapper.doubleValue());

        Type objectType = getType(value);

        if(objectType == Type.INTEGER && this.type == Type.INTEGER && result.intValue() == result) {
            this.value = result.intValue();
        } else {
            this.value = result;
        }

    }

    @Override
    public String toString() {
        if(value instanceof String && value.toString().endsWith("\"")) {
            return value.toString().substring(1, value.toString().length() - 1);
        } else if(value == null) {
            return "";
        } else if (value.toString().endsWith(".0")) {
            return value.toString().substring(0, value.toString().length() - 2);
        }
        return value.toString();
    }

    private Type getType(Object value) {
        if(value instanceof String) {
            try {
                Integer.parseInt((String) value);
                return Type.INTEGER;
            } catch (Exception exception) {
                try {
                    Double.parseDouble((String) value);
                    return  Type.DOUBLE;
                } catch (Exception ignored) {
                    return null;
                }
            }
        } else if(value instanceof Integer) {
            return Type.INTEGER;
        } else if (value instanceof Double) {
            return Type.DOUBLE;
        }
        return null;
    }

    enum Type {
        INTEGER, DOUBLE
    }
}
