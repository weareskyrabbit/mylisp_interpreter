package lisp;

/* package-private */ class Boolean implements S {
    private Boolean(boolean value) {
        this.value = value;
    }
    private final boolean value;
    static final Boolean TRUE = new Boolean(true) { @Override public String toString() { return "#t"; } };
    static final Boolean FALSE = new Boolean(false) { @Override public String toString() { return "#f"; } };
}
