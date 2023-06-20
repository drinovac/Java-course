package hr.fer.zemris.java.custom.scripting.elems;

public class ElementString extends Element {

    private String value;

    public ElementString(String value) {
        this.value = value;
    }

    /**
     * @return
     */
    @Override
    public String asText() {
        return this.value;
    }
}
