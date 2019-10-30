package compiler.ir;

import compiler.lexer.Word;
import compiler.symbols.Type;

public class Symbol extends Expr {
    /* member functions */
    public int offset;
    /* constructors */
    public Symbol(Word symbol, Type type, int offset) {
        super(symbol, type);
        this.offset = offset;
    }
}
