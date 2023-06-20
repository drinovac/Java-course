package hr.fer.zemris.java.custom.scripting.exec;

import java.util.*;

public class ObjectMultistack {

    Map<String, MultistackEntry> map;

    public ObjectMultistack() {
        this.map = new HashMap<>();
    }

    public ValueWrapper peek(String name) {
        return map.get(name).getValue();
    }

    public void push(String name, ValueWrapper value) {
        MultistackEntry entry = new MultistackEntry(value, map.get(name));
        map.put(name, entry);
    }

    public ValueWrapper pop(String name) {
        MultistackEntry value = map.get(name);
        map.put(name, value.next);
        return value.getValue();
    }

    public boolean isEmpty(String name) {
        return map.get(name) != null;
    }


    static class MultistackEntry {
        ValueWrapper value;
        MultistackEntry next;

        public MultistackEntry(ValueWrapper value, MultistackEntry next) {
            this.value = value;
            this.next = next;
        }

        public ValueWrapper getValue() {
            return value;
        }

        public MultistackEntry getNext() {
            return next;
        }


    }

}
