package compiler.token;

public class String_ extends Token {
    public final String value;
    public String_(int line, int offset, final String value) {
        super(line, offset, Tag.STR);
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
