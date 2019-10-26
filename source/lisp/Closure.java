package lisp;

import static lisp.Lisp.*;

public class Closure implements Procedure {

    private S parms;
    private S body;
    private Environment environment;

    /* package-private */ Closure(S parms, S body, Environment environment) {
        this.parms = parms;
        this.body = body;
        this.environment = environment;
    }

    private static void pairlis(S parms, S args, Environment environment) {
        for (; parms instanceof Tuple; parms = parms.cdr(), args = args.cdr())
            environment.define((Symbol)parms.car(), args.car());
        if (parms != NIL)
            environment.define((Symbol)parms, args);
    }

    private static S progn(S body, Environment environment, Continue cont) {
        if (body.cdr() == NIL)
            return body.car().eval(environment, cont);
        return body.car().eval(environment, x -> progn(body.cdr(), environment, cont));
    }

    @Override
    public S apply(S args, Continue cont) {
        Environment n = new Environment(environment);
        pairlis(parms, args, n);
        return progn(body, n, cont);
    }

    @Override
    public String toString() {
        return cons(symbol("closure"), cons(parms, body)).toString();
    }
}