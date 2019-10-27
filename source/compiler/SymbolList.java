package compiler;

import java.util.HashMap;
import java.util.Map;

public class SymbolList {
    final SymbolList enclosing;
    public final Map<String, LocalVariable> symbols;
    private int size;
    public SymbolList(final SymbolList enclosing) {
        this.enclosing = enclosing;
        this.symbols = new HashMap<>();
        this.size = 0;
    }
    void declare(final String name) {
        if (!symbols.containsKey(name)) {
            symbols.put(name, new LocalVariable(name, size++));
        } else {
            System.out.println("`" + name + "` is already declared");
            System.exit(1);
        }
    }
    boolean declared(final String name) {
        if (symbols.containsKey(name)) {
            return true;
        } else if (enclosing != null) {
            return enclosing.declared(name);
        } else {
            return false;
        }
    }
    LocalVariable get(final String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        } else if (enclosing != null) {
            return enclosing.get(name);
        } else {
            System.out.println("`" + name + "` is undeclared");
            System.exit(1);
        }
        return null;
    }
}
