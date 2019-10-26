package lisp;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    /* package-private */ Environment(Environment next) {
        this.next = next;
    }
    private final Map<Symbol, S> map = new HashMap<>();
    private final Environment next;

    /* package-private */ S get(Symbol key) {
        return find(this, key).map.get(key);
    }
    /* package-private */ S set(Symbol key, S value) {
        find(this, key).map.put(key, value);
        return value;
    }
    /* package-private */ S define(Symbol key, S value) {
        map.put(key, value);
        return value;
    }
    @Override
    public String toString() {
        return map.toString() + (next != null ? " -> " + next : "");
    }

    private static Environment find(Environment environment, Symbol key) {
        for (Environment e = environment; e != null; e = e.next)
            if (e.map.containsKey(key))
                return e;
        throw new RuntimeException("Variable " + key + " not found");
    }
}