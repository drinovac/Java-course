package hr.fer.oprpp1.custom.scripting.elems;

public class ElementFunction extends Element {

    private String name;

    public ElementFunction(String name) {
        this.name = name;
    }


    /**
     * @return
     */
    @Override
    public String asText() {
        return this.name;
    }
}
