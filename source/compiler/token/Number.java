package compiler.token;

public class Number extends Token {
    public final int value;
    public Number(int line, int offset, int value) {
        super(line, offset, Tag.NUM);
        this.value = value;
    }
    @Override
    public String toString() {
        return "" + value;
    }
}
