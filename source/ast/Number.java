package ast;

public class Number implements S {
    private int value;
    public Number(int value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "" + value;
    }
}
