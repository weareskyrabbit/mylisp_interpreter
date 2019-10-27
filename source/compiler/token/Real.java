package compiler.token;

public class Real extends Token {
    public final float value;
    public Real(int line, int offset, float value) {
        super(line, offset, Tag.REAL);
        this.value = value;
    }
    @Override
    public String toString() {
        return "" + value;
    }
}
