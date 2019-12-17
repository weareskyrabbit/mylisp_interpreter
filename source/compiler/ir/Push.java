package compiler.ir;

public class Push<T> implements Instruction {
    Immediate<T> operand;
    public Push(Immediate<T> operand) {
        this.operand = operand;
    }
    public T getOperand() {
        return operand.value;
    }
}
