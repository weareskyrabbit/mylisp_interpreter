package compiler;

import ast.Nil;
import ast.S;
import ast.Tuple;
import compiler.token.*;
import io.Reader;

import java.io.IOException;
import java.util.*;

public class Parser {
    private String path;
    private String input;
    private Tokenizer tokenizer;
    private Token now;
    private SymbolList current;
    private List<String> strings;
    /* private Node pointer; */

    public Parser() {}
    private boolean match(final int tag) {
        return now.tag == tag;
    }
    private void consume(final int tag) throws ParsingException {
        if (match(tag)) {
            now = tokenizer.tokenize();
        } else {
            if (tag < 0xff && now.tag < 0xff) {
                throw new ParsingException(path + ":" + now.line + ":expecting `" + (char) tag +
                        "`, but found `" + (char) now.tag + "`");
            } else if (tag < 0xff) {
                throw new ParsingException(path + ":" + now.line + ":expecting `" + (char) tag +
                        "`, but found `" + now.tag + "`");
            } else if (now.tag < 0xff) {
                throw new ParsingException(path + ":" + now.line + ":expecting `" + tag +
                        "`, but found `" + (char) now.tag + "`");
            } else {
                throw new ParsingException(path + ":" + now.line + ":expecting `" + tag +
                        "`, but found `" + now.tag + "`");
            }
        }
    }
    private int integer() throws ParsingException {
        final int value;
        if (match(Tag.NUM)) {
            value = ((compiler.token.Number)now).value;
            now = tokenizer.tokenize();
        } else {
            throw new ParsingException(path + ":" + now.line + ":expecting `NUM`, but found `" + now.tag + "`");
        }
        return value;
    }
    private String symbol() throws ParsingException {
        final String id;
        if (match(Tag.SYMBOL)) {
            id = ((Word)now).lexeme;
            now = tokenizer.tokenize();
        } else {
            throw new ParsingException(path + ":" + now.line + ":expecting `ID`, but found `" + now.tag + "`");
        }
        return id;
    }
    private String string() throws ParsingException {
        final String string;
        if (match(Tag.STR)) {
            string = ((compiler.token.String_)now).value;
            if (!strings.contains(string)) {
                strings.add(string);
            }
            now = tokenizer.tokenize();
        } else {
            throw new ParsingException(path + ":" + now.line + ":expecting `STR`, but found `" + now.tag + "`");
        }
        return string;
    }
    public List<String> strings() {
        return strings;
    }
    /* package-private */ List<S> parse(final String path) throws IOException, ParsingException {
        this.path = path;
        this.input = Reader.use(path + ".lisp", Reader::read);
        this.tokenizer = new Tokenizer(input);
        this.now = tokenizer.tokenize();
        this.current = null;
        this.strings = new ArrayList<>();

        Vector<S> s = new Vector<>();
        while (match(Tag.NUM) || match('(')) s.add(s());
        return s;
    }
    private S s() throws ParsingException {
        if (match(Tag.NUM)) {
            int value = integer();
            return new ast.Number(value);
        } else if (match(Tag.SYMBOL)) {
            String value = symbol();
            return new ast.Symbol(value);
        } else {
            consume('(');
            if (match(')')) {
                consume(')');
                return new Nil();
            } else {
                S car = s();
                if (match('.')) {
                    consume('.');
                    return new Tuple(car, s());
                } else {
                    return list(car);
                }
            }
        }
    }
    private Tuple list(S car) throws ParsingException {
        if (match(')')) {
            consume(')');
            return new Tuple(car, new Nil());
        }
        return new Tuple(car, list(s()));
    }
    public static String tab(int tab) {
        // TODO improve tab system
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tab; i++) {
            builder.append(' ');
        }
        return builder.toString();
    }
}
