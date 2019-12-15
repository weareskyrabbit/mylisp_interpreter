package compiler.token;

import compiler.ast.S;

/* example: 100, -50 */
public class NumberLiteral extends Token implements S {
    private int value;
    public NumberLiteral(String value) {
        super(Tag.NUMBER);
        this.value = Integer.parseInt(value);
    }
    public int getValue() {
        return value;
    }
    @Override
    public String toString() {
        return String.format("Number(%d)", value);
    }
}
