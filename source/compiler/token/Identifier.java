package compiler.token;

/* example: hoge, piyo */
public class Identifier extends Token {
    private String name;
    public Identifier(String name) {
        super(Tag.SYMBOL);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return String.format("Identifier(%s)", name);
    }
}
