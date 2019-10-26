package lisp;

public interface Expandable extends Applicable {
    /* public */ S expand(S args);

    @Override
    /* public */ default S apply(S args, Environment environment, Continue cont) {
        return expand(args).eval(environment, cont);
    }
}