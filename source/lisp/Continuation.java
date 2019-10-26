package lisp;

public class Continuation implements Procedure {
    /* package-private */ Continuation(Continue cont) {
        this.cont = cont;
    }
    private final Continue cont;
    @Override
    public S apply(S args, Continue cont) {
        return this.cont.apply(args.car());
    }

}
