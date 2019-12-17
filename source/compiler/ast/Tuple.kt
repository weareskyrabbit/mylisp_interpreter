package compiler.ast

import compiler.ir.Immediate
import compiler.ir.Push
import compiler.semantic.SemanticException
import java.util.function.Function

@Suppress("UNCHECKED_CAST")
class Tuple<T, R>(private val car: S, private val cdr: S) : S {
    override fun toString(): String {
        return "($car.$cdr)"
    }
    @Throws(SemanticException::class)
    override fun evaluate(): Push {
        val tmp0 = car.evaluate()
        val tmp1 = cdr.evaluate()
        if (!(tmp0 is Function<*, *> && tmp1 is T)) {
            throw SemanticException()
        }
        val tmp2 = tmp0 as Function<T, R>
        val tmp3 = tmp1 as T
        val tmp4 = tmp2.apply(tmp3)
        return Push(Immediate(tmp4))
    }
    fun apply(): T {

    }
}