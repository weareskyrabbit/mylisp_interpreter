package compiler.token;

public class Token {
    public final int line;
    public final int offset;
    public final int tag;
    public Token(int line, int offset, int tag) {
        this.line = line;
        this.offset = offset;
        this.tag = tag;
    }
    @Override
    public String toString() {
        return "" + (char) tag;
    }
    public static final Token EOF = new Token(0, 0, Tag.EOF);
}