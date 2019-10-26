package interpreter;

public interface Procedure extends Applicable {
    /* public */ S apply(S args, Continue cont);

    /* public */ static S evals(S args, Environment environment, Continue cont) {
        return args instanceof Tuple
                ? args.car().eval(environment, x -> evals(args.cdr(), environment, y -> cont.apply(new Tuple(x, y))))
                : cont.apply(List.NIL);
    }

    @Override
    /* public */ default S apply(S args, Environment environment, Continue cont) {
        return evals(args, environment, x -> apply(x, cont));
    }
}