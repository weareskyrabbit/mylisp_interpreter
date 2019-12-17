package compiler.token;

import compiler.ast.S;
import compiler.ir.Immediate;
import compiler.ir.Push;

/* example: "hoge" "piyo" */
public class StringLiteral extends Token implements S {
    private String value;
    public StringLiteral(String value) {
        super(Tag.STRING);
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return String.format("String(%s)", value);
    }
    @Override
    public Push evaluate() {
        return new Push(new Immediate<>(value));
    }
}
