package hr.fer.zemris.java.custom.scripting.elems;

public class ElementVariable extends Element {

    private String name;

    public ElementVariable(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    @Override
    public String asText() {
        return name;
    }
}
