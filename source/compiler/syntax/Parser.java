package compiler.syntax;

import compiler.ast.*;
import compiler.ast.S;
import compiler.token.*;
import compiler.token.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/* RDP: Recursive Descent Parsing */
public class Parser {
    private Token[] tokens;
    private int t; // tokens[t]: current token
    private Map<String, Symbol> symbols;
    private boolean match(int tag) {
        return tokens[t].tag == tag;
    }
    private Token consume(int tag) throws SyntaxException {
        if (match(tag)) {
            return tokens[t++];
        } else {
            throw new SyntaxException();
        }
    }
    private String consume_symbol() throws SyntaxException {
        Identifier tmp = (Identifier) consume(Tag.SYMBOL);
        return tmp.getName();
    }
    public S[] parse(Token[] tokens) throws SyntaxException {
        this.tokens = tokens;
        this.t = 0;
        this.symbols = new HashMap<>();
        Vector<S> result = new Vector<>();
        while (t < tokens.length) {
            result.add(s());
        }
        return result.toArray(new S[0]);
    }
    private S s() throws SyntaxException {
        if (match('(')) {
            consume('(');
            S left = s();
            if (match('.')) {
                consume('.');
                S right = s();
                consume(')');
                return new Tuple(left, right); // (a . b)
            } else {
                return in_list(left); // (a b c)
            }
        } else {
            return atom(); // a
        }
    }
    private S in_list(S left) throws SyntaxException {
        if (match(')')) {
            consume(')');
            return new Tuple(left, new Nil());
        } else {
            S right = s();
            return new Tuple(left, in_list(right));
        }
    }
    private S atom() throws SyntaxException {
        if (match(Tag.NUMBER)) {
            return (S) consume(Tag.NUMBER);
        } else if (match(Tag.SYMBOL)) {
            String name = consume_symbol();
            if (!symbols.containsKey(name)) {
                symbols.put(name, Symbol.NIL);
            }
            return symbols.get(name);
        } else {
            throw new SyntaxException();
        }
        // TODO nil boolean
    }
}