package compiler;

public class LocalVariable {
    public final String name;
    public int offset;
    private int version;
    LocalVariable(final String name, final int offset) {
        this.name = name;
        this.offset = offset;
        this.version = 0;
    }
    @Override
    public String toString() {
        return name + version;
    }
    int update() {
        return version++;
    }
}
