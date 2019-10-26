package lisp;

public class Number implements S, Comparable<Number> {
    private Number(int value) {
        this.value = value;
    }
    private Number(String value) {
        this.value = Integer.parseInt(value);
    }
    private final int value;

    /* package-private */ Number plus(Number n) { return of(value + n.value); }
    /* package-private */ Number minus(Number n) { return of(value - n.value); }
    /* package-private */ Number mult(Number n) { return of(value * n.value); }
    /* package-private */ Number div(Number n) { return of(value / n.value); }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Number && ((Number)obj).value == value;
    }
    @Override
    public int compareTo(Number o) {
        return Integer.compare(value, o.value);
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /* package-private */ static Number of(int value) { return new Number(value); }
    /* package-private */ static Number of(String value) { return new Number(value); }
    /* package-private */ static final Number ZERO = of(0);
    /* package-private */ static final Number ONE = of(1);
}