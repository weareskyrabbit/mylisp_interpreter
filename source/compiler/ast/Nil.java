package compiler.ast;

public class Nil implements S {
    @Override
    public String toString() {
        return "()";
    }
    @Override
    public Symbol evaluate() {
        return null;
    }
}
