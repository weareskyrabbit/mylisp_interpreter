package compiler.token;

import compiler.ast.S;

/* example: hoge, piyo */
public class Symbol extends Token implements S {
    private String name;
    public Symbol(String name) {
        super(Tag.SYMBOL);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return String.format("Symbol(%s)", name);
    }
}
