package hr.fer.oprpp1.custom.scripting.elems;

public class ElementFunction extends Element {

    private String name;

    public ElementFunction(String name) {
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
        return this.name;
    }
}
