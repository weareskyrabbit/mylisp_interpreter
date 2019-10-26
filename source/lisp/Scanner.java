package lisp;

import static lisp.Lisp.*;

import java.io.IOException;

/* package-private */ class Scanner {
    /* package-private */ Scanner(java.io.Reader reader) {
        this.reader = reader;
        get();
    }
    /* package-private */ static final S EOF_OBJECT = new S(){};
    private static final int EOF = -1;
    private static final S DOT = new S(){};

    private final java.io.Reader reader;
    private int ch;


    private int get() {
        if (ch == EOF)
            return EOF;
        try {
            return ch = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isSpace(int ch) {
        return Character.isWhitespace(ch);
    }

    private static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }

    private static boolean isDelimiter(int ch) {
        switch (ch) {
            case '\'': case '(': case ')':
            case ',':
            case '"':
                return true;
        }
        return false;
    }

    private static boolean isSymbol(int ch) {
        return ch != EOF && !isSpace(ch) && !isDelimiter(ch);
    }

    private S readList() {
        Tuple.Builder builder = Tuple.builder();
        while (true) {
            skipSpaces();
            switch (ch) {
                case ')':
                    get();
                    return builder.build();
                case EOF:
                    throw new RuntimeException("')' expected");
                default:
                    S r = readObject();
                    if (r == DOT) {
                        S last = read();
                        skipSpaces();
                        if (ch != ')')
                            throw new RuntimeException("')' expected");
                        get();
                        builder.last(last);
                        return builder.build();
                    }
                    builder.tail(r);
                    break;
            }
        }
    }

    private S readSymbol(StringBuilder sb) {
        while (isSymbol(ch)) {
            sb.append((char)ch);
            get();
        }
        String s = sb.toString();
        switch (s) {
            case "#t": return TRUE;
            case "#f": return FALSE;
            default: return symbol(sb.toString());
        }
    }

    private S readNumber(StringBuilder sb) {
        while (isDigit(ch)) {
            sb.append((char)ch);
            get();
        }
        return Number.of(sb.toString());
    }

    private S readAtom() {
        int first = ch;
        StringBuilder sb = new StringBuilder();
        sb.append((char)first);
        get();
        switch (first) {
            case '+': case '-':
                return isDigit(ch) ? readNumber(sb) : readSymbol(sb);
            case '.':
                return isSymbol(ch) ? readSymbol(sb) : DOT;
            default:
                return isDigit(first) ? readNumber(sb) : readSymbol(sb);
        }
    }

    private void skipSpaces() {
        while (isSpace(ch))
            get();
    }

    private S readObject() {
        skipSpaces();
        switch (ch) {
            case EOF:
                return EOF_OBJECT;
            case '(':
                get();
                return readList();
            case '\'':
                get();
                return list(QUOTE, read());
            default:
                return readAtom();
        }
    }

    /* package-private */ S read() {
        S r = readObject();
        if (r == DOT)
            throw new RuntimeException("unexpected dot");
        return r;
    }
}