package compiler.lexer;

public class Token {
    /* member variables */
    public final int tag;
    /* constructors */
    public Token(int tag) {
        this.tag = tag;
    }
    /* member functions */
    @Override
    public String toString() {
        return "" + (char) tag;
    }
}