package hr.fer.oprpp1.custom.scripting.elems;

public class ElementString extends Element {

    private String value;

    public ElementString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * @return
     */
    @Override
    public String asText() {
        return this.value;
    }
}
