package interpreter;

import java.io.InputStreamReader;

public class Interpreter {
    private Interpreter() {}

    /* package-private */ static final Symbol QUOTE = Symbol.of("quote");
    /* package-private */ static final Symbol LAMBDA = Symbol.of("lambda");
    /* package-private */ static final List NIL = List.NIL;
    /* package-private */ static final Boolean TRUE = Boolean.TRUE;
    /* package-private */ static final Boolean FALSE = Boolean.FALSE;

    private static final Environment ENVIRONMENT = new Environment(null);

    static {
        ENVIRONMENT.define(QUOTE, (Applicable) (args, env, cont) -> cont.apply(args.car()));

        ENVIRONMENT.define(symbol("if"), (Applicable) (args, env, cont) ->
                args.car().eval(env, x ->
                        x != FALSE ? args.cdr().car().eval(env, cont)
                                : args.cdr().cdr() != NIL ? args.cdr().cdr().car().eval(env, cont)
                                : cont.apply(FALSE)));

        ENVIRONMENT.define(symbol("lambda"), (Applicable) (args, env, cont) ->
                cont.apply(new Closure(args.car(), args.cdr(), env)));

        ENVIRONMENT.define(symbol("define"), (Applicable) (args, env, cont) ->
                args.car() instanceof Tuple
                        ? cont.apply(env.define((Symbol)args.car().car(),
                        new Closure(args.car().cdr(), args.cdr(), env)))
                        : args.cdr().car().eval(env, x ->
                        cont.apply(env.define((Symbol)args.car(), x))));

        ENVIRONMENT.define(symbol("set!"), (Applicable) (args, env, cont) ->
                args.cdr().car().eval(env, x -> cont.apply(env.set((Symbol)args.car(), x))));
    }

    static {
        ENVIRONMENT.define(symbol("car"), (Procedure) (args, cont) -> cont.apply(args.car().car()));
        ENVIRONMENT.define(symbol("cdr"), (Procedure) (args, cont) -> cont.apply(args.car().cdr()));
        ENVIRONMENT.define(symbol("cons"), (Procedure) (args, cont) -> cont.apply(cons(args.car(), args.cdr().car())));
        ENVIRONMENT.define(symbol("list"), (Procedure) (args, cont) -> cont.apply(args));
        ENVIRONMENT.define(symbol("eq?"), (Procedure) (args, cont) -> cont.apply(bool(args.car() == args.cdr().car())));
        ENVIRONMENT.define(symbol("equal?"), (Procedure) (args, cont) -> cont.apply(bool(args.car().equals(args.cdr().car()))));
        ENVIRONMENT.define(symbol("pair?"), (Procedure) (args, cont) -> cont.apply(bool(args.car() instanceof Tuple)));
        ENVIRONMENT.define(symbol("display"), (Procedure) (args, cont) -> {
            for (; args instanceof Tuple; args = args.cdr())
                System.out.print(args.car() + " ");
            System.out.println();
            return cont.apply(FALSE);
        });
        ENVIRONMENT.define(symbol("call/cc"), (Procedure) (args, cont) ->
                ((Procedure)args.car()).apply(list(new Continuation(cont)), cont));
    }

    private static int compare(S a, S b) {
        return ((Comparable)a).compareTo((Comparable)b);
    }

    static {
        ENVIRONMENT.define(symbol("="), (Procedure) (args, cont) -> cont.apply(bool(args.car().equals(args.cdr().car()))));
        ENVIRONMENT.define(symbol("<"), (Procedure) (args, cont) -> cont.apply(bool(compare(args.car(), args.cdr().car()) < 0)));
        ENVIRONMENT.define(symbol("<="), (Procedure) (args, cont) -> cont.apply(bool(compare(args.car(), args.cdr().car()) <= 0)));
        ENVIRONMENT.define(symbol(">"), (Procedure) (args, cont) -> cont.apply(bool(compare(args.car(), args.cdr().car()) > 0)));
        ENVIRONMENT.define(symbol(">="), (Procedure) (args, cont) -> cont.apply(bool(compare(args.car(), args.cdr().car()) >= 0)));
        ENVIRONMENT.define(symbol("+"), (Procedure) (args, cont) -> cont.apply(args.reduce(Number.ZERO, (a, b) -> ((Number)a).plus((Number)b))));
        ENVIRONMENT.define(symbol("-"), (Procedure) (args, cont) -> cont.apply(args.reduce(Number.ZERO, (a, b) -> ((Number)a).minus((Number)b))));
        ENVIRONMENT.define(symbol("*"), (Procedure) (args, cont) -> cont.apply(args.reduce(Number.ONE, (a, b) -> ((Number)a).mult((Number)b))));
        ENVIRONMENT.define(symbol("/"), (Procedure) (args, cont) -> cont.apply(args.reduce(Number.ONE, (a, b) -> ((Number)a).div((Number)b))));
    }

    private static S letStar(S vars, S body) {
        return vars == NIL
                ? list(cons(LAMBDA, cons(NIL, body)))
                : list(cons(LAMBDA, list(list(vars.car().car()),
                letStar(vars.cdr(), body))),
                vars.car().cdr().car());
    }

    static {
        ENVIRONMENT.define(symbol("let"), (Expandable) args ->
                args.car() instanceof Tuple
                        ? cons(cons(LAMBDA, cons(args.car().map(x -> x.car()), args.cdr())),
                        args.car().map(x -> x.cdr().car()))
                        : list(list(LAMBDA,     // named let
                        list(args.car()),
                        list(symbol("set!"), args.car(),
                                cons(LAMBDA,
                                        cons(args.cdr().car().map(x -> x.car()),
                                                args.cdr().cdr()))),
                        cons(args.car(), args.cdr().car().map(x -> x.cdr().car()))),
                        FALSE));

        ENVIRONMENT.define(symbol("let*"), (Expandable) args ->
                letStar(args.car(), args.cdr()));

        ENVIRONMENT.define(symbol("letrec"), (Expandable) args ->
                cons(cons(LAMBDA, cons(args.car().map(x -> x.car()),
                        append(args.car().map(x -> cons(symbol("set!"), x)), args.cdr()))),
                        args.car().map(x -> FALSE)));

        ENVIRONMENT.define(symbol("begin"), (Expandable) args -> letStar(NIL, args));
    }

    /* package-private */ static Symbol symbol(String name) {
        return Symbol.of(name);
    }

    /* package-private */ static Tuple cons(S car, S cdr) {
        return new Tuple(car, cdr);
    }

    /* package-private */ static List list(S... args) {
        List r = NIL;
        for (int i = args.length - 1; i >= 0; --i)
            r = new Tuple(args[i], r);
        return r;
    }

    private static Boolean bool(boolean value) {
        return value ? TRUE : FALSE;
    }

    private static S append(S... lists) {
        Tuple.Builder b = Tuple.builder();
        for (S list : lists)
            for (; list instanceof Tuple; list = list.cdr())
                b.tail(list.car());
        return b.build();
    }

    private static S eval(S s) {
        return s.eval(ENVIRONMENT, x -> x);
    }

    // static S read(String s) {
    //     return new Scanner(new StringReader(s)).read();
    // }
    public static void interpret(Scanner scanner) {
        boolean redirect = System.console() == null;
        String prompt = "> ";
        while (true) {
            if (!redirect) {
                System.out.print(prompt);
                System.out.flush();
            }
            if (scanner == null)
                scanner = new Scanner(new InputStreamReader(System.in));
            try {
                S input = scanner.read();
                if (input == Scanner.EOF_OBJECT || input == Symbol.of("quit"))
                    break;
                S evaled = eval(input);
                System.out.println(evaled);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}