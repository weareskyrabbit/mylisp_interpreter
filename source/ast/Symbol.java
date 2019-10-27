package ast;

public class Symbol implements S {
    private String value;
    public Symbol(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
