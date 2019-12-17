package compiler.ast;

import compiler.ir.Push;
import compiler.semantic.SemanticException;


public interface S {
    Push evaluate() throws SemanticException;
}
