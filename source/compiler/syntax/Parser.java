package compiler.syntax;

import compiler.ast.*;
import compiler.ast.S;
import compiler.token.*;
import compiler.token.Symbol;

import java.util.Vector;

/* RDP: Recursive Descent Parsing */
public class Parser {
    private Token[] tokens;
    private int t; // tokens[t]: current token
    private Vector<Variable> variables;
    private boolean match(int tag) {
        return tokens[t].tag == tag;
    }
    private Token consume(int tag) throws ParsingException {
        if (match(tag)) {
            return tokens[t++];
        } else {
            throw new ParsingException();
        }
    }
    private String consume_symbol() throws ParsingException {
        Symbol tmp = (Symbol) consume(Tag.SYMBOL);
        return tmp.getName();
    }
    public S[] parse(Token[] tokens) throws ParsingException {
        this.tokens = tokens;
        this.t = 0;
        this.variables = new Vector<>();
        Vector<S> result = new Vector<>();
        while (t < tokens.length) {
            result.add(s());
        }
        return result.toArray(new S[0]);
    }
    private S s() throws ParsingException {
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
    private S in_list(S left) throws ParsingException {
        if (match(')')) {
            consume(')');
            return new Tuple(left, new Nil());
        } else {
            S right = s();
            return new Tuple(left, in_list(right));
        }
    }
    private S atom() throws ParsingException {
        if (match(Tag.NUMBER)) {
            return (S) consume(Tag.NUMBER);
        } else if (match(Tag.SYMBOL)) {
            String name = consume_symbol();
            if (variables.stream()
                        .anyMatch(v -> v.name.equals(name))) {
                return variables.stream()
                        .filter(v -> v.name.equals(name))
                        .findFirst()
                        .orElseThrow();
            } else {
                Variable tmp = new Variable("int", name);
                variables.add(tmp);
                return tmp;

            }
        } else {
            throw new ParsingException();
        }
        // TODO nil boolean
    }
}