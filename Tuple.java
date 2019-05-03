package lisp;

public class Tuple implements List {
    /* package_private */ Tuple(S car, S cdr) {
        this.car = car;
        this.cdr = cdr;
    }
    private S car, cdr;

    @Override public S car() { return car; }
    @Override public S cdr() { return cdr; }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple))
            return false;
        Tuple o = (Tuple)obj;
        return o.car.equals(car) && o.cdr.equals(cdr);
    }
    @Override
    public S eval(Environment environment, Continue cont) {
        return car.eval(environment, x -> x.apply(cdr, environment, cont));
    }
    @Override
    public String toString() {
        if (cdr instanceof Tuple && cdr.cdr() == NIL) {
            if (car == Lisp.QUOTE) return "'" + cdr.car();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(car);
        S e = cdr;
        for (; e instanceof Tuple; e = e.cdr())
            sb.append(" ").append(e.car());
        if (e != NIL)
            sb.append(" . ").append(e);
        sb.append(")");
        return sb.toString();
    }

    public static class Builder {
        S head = NIL;
        S tail = NIL;

        public Builder head(S e) {
            head = new Tuple(e, head);
            if (!(tail instanceof Tuple))
                tail = head;
            return this;
        }
        /* package-private */ Builder tail(S e) {
            if (tail instanceof Tuple)
                tail = ((Tuple) tail).cdr = new Tuple(e, tail.cdr());
            else
                head = tail = new Tuple(e, tail);
            return this;
        }
        /* package-private */ Builder last(S e) {
            if (tail instanceof Tuple)
                ((Tuple) tail).cdr = e;
            else
                head = tail = e;
            return this;
        }
        /* package-private */ S build() {
            return head;
        }
    }
    /* package-private */ static Builder builder() { return new Builder(); }
}