package lisp;

public interface Applicable extends S {
    @Override
    /* public */ S apply(S args, Environment environment, Continue cont);
}