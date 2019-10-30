package compiler.symbols;

import compiler.lexer.Token;
import compiler.ir.Symbol;

import java.util.Hashtable;

public class Closure {
    /* member variables */
    private Hashtable<Token, Symbol> table;
    protected Closure previous;
    /* constructors */
    public Closure(Closure previous) {
        table = new Hashtable<>();
        this.previous = previous;
    }
    /* member functions */
    public void put(Token token, Symbol symbol) {
        table.put(token, symbol);
    }
    public Symbol get(Token token) {
        for(Closure closure = this; closure != null; closure = closure.previous) {
            if (closure.table.contains(token)) return closure.table.get(token);
        }
        return null; //TODO throw Exception
    }
}
