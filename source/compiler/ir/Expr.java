package compiler.ir;

import compiler.lexer.Token;
import compiler.symbols.Type;

public class Expr extends Node {
    /* member variables */
    public Token op;
    public Type type;
    /* constructors */
    Expr(Token op, Type type) {
        this.op = op;
        this.type = type;
    }
    /* member functions */
    public Expr gen() {
        return this;
    }
    public Expr reduce() {
        return this;
    }
    public void jumping(int _true, int _false) {
        emitjumps(toString(), _true, _false);
    }
    public void emitjumps(String test, int _true, int _false) {
        if (_true != 0 && _false != 0) {
            emit("if " + test + " goto L" + _true);
            emit("goto L" + _false);
        }
        else if (_true != 0) emit("if " + test + " goto L" + _true);
        else if (_false != 0) emit("iffalse "+ test + " goto L" + _false);
    }
    @Override
    public String toString() {
        return op.toString();
    }
}
