package compiler.token;

public class Token {
    public int tag;
    public Token(int tag) { this.tag = tag; }
    @Override
    public String toString() {
        if (tag < 256) return "" + (char) tag;
        else return "";
    }
}
