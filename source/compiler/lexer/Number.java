package compiler.lexer;

public class Number extends Token {
    /* member variables */
    public final int value;
    /* constructors */
    public Number(int value) {
        super(Tag.NUMBER);
        this.value = value;
    }
    /* member functions */
    @Override
    public String toString() {
        return "" + value;
    }
}
