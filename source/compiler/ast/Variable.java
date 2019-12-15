package compiler.ast;

public class Variable implements S {
    public String type;
    public String name;
    public Variable (String type, String name) {
        this.type = type;
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("Variable(%s)", name);
    }
}
