package hr.fer.zemris.java.custom.scripting.elems;

public class ElementConstantInteger extends Element {

    private int value;

    public ElementConstantInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * @return
     */
    @Override
    public String asText() {
        return String.valueOf(this.getValue());
    }
}
