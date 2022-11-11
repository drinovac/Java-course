package hr.fer.oprpp1.custom.scripting.elems;

public class ElementVariable extends Element {

    private String name;

    public ElementVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @return
     */
    @Override
    public String asText() {
        return name;
    }
}
