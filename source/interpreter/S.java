package interpreter;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public interface S {

    /* public */ default S eval(Environment environment, Continue cont) { return cont.apply(this); }
    /* public */ default S apply(S args, Environment environment, Continue cont) { throw new RuntimeException("Cannot apply " + args + " to " + this); }
    /* public */ default S car() { throw new RuntimeException("Cannot get car for " + this); }
    /* public */ default S cdr() { throw new RuntimeException("Cannot get cdr for " + this); }

    /* public */ default S map(UnaryOperator<S> f) {
        Tuple.Builder b = Tuple.builder();
        for (S e = this; e instanceof Tuple; e = e.cdr())
            b.tail(f.apply(e.car()));
        return b.build();
    }

    /* public */ default S reduce(S unit, BinaryOperator<S> f) {
        S prev = null;
        S r = unit;
        int i = 0;
        for (S e = this; e instanceof Tuple; e = e.cdr()) {
            switch (i++) {
                case 0: prev = e.car(); break;
                case 1: r = prev; break;
            }
            r = f.apply(r, e.car());
        }
        return r;
    }

}
