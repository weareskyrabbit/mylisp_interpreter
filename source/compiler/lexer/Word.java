package compiler.lexer;

public class Word extends Token {
    /* member variables */
    public String lexeme = "";
    /* constructors */
    public Word(String lexeme, int tag) {
        super(tag);
        this.lexeme = lexeme;
    }
    /* member functions */
    @Override
    public String toString() {
        return lexeme;
    }
    /* static variables */
    public static final Word
        le    = new Word("<=", Tag.LE),       ge    = new Word(">=", Tag.GE),
        minus = new Word("minus", Tag.MINUS), temp  = new Word("$", Tag.TEMP),
        True  = new Word("#t", Tag.TRUE),   False = new Word("#f", Tag.FALSE),
        Nil   = new Word("nil", Tag.NIL);

}
