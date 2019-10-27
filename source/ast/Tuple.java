package ast;

public class Tuple implements S {
    /* package-private */ S car, cdr;
    public Tuple(S car, S cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @Override
    public String toString() {
        return "(" + car.toString() + "." + cdr.toString() + ")";
    }
}
