package compiler.ast;

import compiler.ir.Immediate;
import compiler.ir.Push;
import compiler.semantic.SemanticException;

import java.util.function.Function;

public class Tuple implements S {
    private S car, cdr;
    public Tuple(S car, S cdr) {
        this.car = car;
        this.cdr = cdr;
    }
    @Override
    public String toString() {
        return "(" + car.toString() + "." + cdr.toString() + ")";
    }
    @Override
    public Push evaluate() throws SemanticException {
        car.evaluate();
        cdr.evaluate();

        if (!(tmp0 instanceof Function<?, ?>)) {
            throw new SemanticException();
        }
        Function<?, ?> tmp2 = (Function<?, ?>) tmp0;
        R tmp4 = tmp2.apply(tmp1);
        return new Push(new Immediate<>(tmp4));
    }
    public T apply() {

    }
}
