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
    private void error(String message) throws ParsingException {
        throw new ParsingException(String.format("\u001b[00;33m%s:%d %s\u001b[00m\n%s\n"
                + " ".repeat(Math.max(0, now.offset)) + '^',
                path, now.line, message, input.split("\n")[now.line]));
    }
    private String tagToString(int tag) throws ParsingException {
        if (tag <= 0xff) return "" + (char) tag;
        switch (tag) {
            case 256: return "&&";
            case 257: return "[BASIC]";
            case 258: return "break";
            case 259: return "do";
            case 260: return "else";
            case 261: return "==";
            case 262: return "False";
            case 263: return ">=";
            case 264: return "[SYMBOL]";
            case 265: return "if";
            case 266: return "[INDEX]";
            case 267: return "<=";
            case 268: return "-";
            case 269: return "!=";
            case 270: return "[NUMBER]";
            case 271: return "||";
            case 272: return "[REAL]";
            case 273: return "[TEMPORALLY]";
            case 274: return "True";
            case 275: return "while";
            case 276: return "return";
            case 277: return "print";
            case 278: return "int";
            case 279: return "[STRING]";
            case 280: return "[EOF]";
            default: error("unknown tag " + tag); return "";
        }
    }
    private boolean match(final int tag) {
        return now.tag == tag;
    }
    private void consume(final int tag) throws ParsingException {
        if (match(tag)) {
            now = tokenizer.tokenize();
        } else {
            error(String.format("expecting `%s`, but found `%s`", tagToString(tag), tagToString(now.tag)));
        }
    }
    private int integer() throws ParsingException {
        int value = 0;
        if (match(Tag.NUM)) {
            value = ((compiler.token.Number)now).value;
            now = tokenizer.tokenize();
        } else {
            error("expecting `NUMBER`, but found `" + now.tag + "`");
        }
        return value;
    }
    private String symbol() throws ParsingException {
        String id = "";
        if (match(Tag.SYMBOL)) {
            id = ((Word)now).lexeme;
            now = tokenizer.tokenize();
        } else {
            error("expecting `SYMBOL`, but found `" + now.tag + "`");
        }
        return id;
    }
    private String string() throws ParsingException {
        String string = "";
        if (match(Tag.STR)) {
            string = ((compiler.token.String_)now).value;
            if (!strings.contains(string)) {
                strings.add(string);
            }
            now = tokenizer.tokenize();
        } else {
            error("expecting `STRING`, but found `" + now.tag + "`");
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
