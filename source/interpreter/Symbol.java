package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Symbol implements S {
    private Symbol(String name) {
        this.name = name;
    }
    private static final Map<String, Symbol> map = new HashMap<>();
    private final String name;

    @Override
    public S eval(Environment environment, Continue cont) {
        return cont.apply(environment.get(this));
    }
    @Override
    public String toString() {
        return name;
    }

    /* package-private */ static Symbol of(String name) {
        return map.computeIfAbsent(name, Symbol::new);
    }
}