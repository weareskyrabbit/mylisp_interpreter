package compiler.lexer;

public class Real extends Token {
    /* member variables */
    public final float value;
    /* constructors */
    public Real(float value) {
        super(Tag.REAL);
        this.value = value;
    }
    /* member functions */
    @Override
    public String toString() {
        return "" + value;
    }
}
