package lisp;

public interface Procedure extends Applicable {
    /* public */ S apply(S args, Continue cont);

    /* public */ static S evlis(S args, Environment environment, Continue cont) {
        return args instanceof Tuple
                ? args.car().eval(environment, x -> evlis(args.cdr(), environment, y -> cont.apply(new Tuple(x, y))))
                : cont.apply(List.NIL);
    }

    @Override
    /* public */ default S apply(S args, Environment environment, Continue cont) {
        return evlis(args, environment, x -> apply(x, cont));
    }
}