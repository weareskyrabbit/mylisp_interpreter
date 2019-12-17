package compiler.ast;

import compiler.ir.Immediate;
import compiler.ir.Push;
import compiler.semantic.SemanticException;

public class Symbol<T> implements S {
    public String name;
    public T value;
    public Symbol(String name) {
        this.name = name;
    }
    public Symbol<T> assign(T value) {
        this.value = value;
        return this;
    }
    @Override
    public String toString() {
        return String.format("Symbol(%s)", name);
    }
    @Override
    public Push evaluate() throws SemanticException {
        return new Push(new Immediate<>(value));
    }
    public static final Symbol NIL = null; // TODO
}
