package compiler.vm;

import compiler.ir.Immediate;

import java.util.Stack;
import java.util.function.Function;

public class VirtualMachine {
    private static final Immediate[] constant_pool = { new Immediate<>("Hello, World!") };
    private static Stack<Immediate> runtime_stack;
    public static void main(String[] args) {
        runtime_stack = new Stack<>();
        // runtime_stack.push(new Immediate<Function<Integer, Integer>>(x -> x + 1));
        // 1 + 2 * 3
        pushi(1);
        pushi(2);
        pushi(3);
        imul();
        iadd();
        disp();
        // "Hello, World!"
        pushc(0);
        disp();
        // not false or 100 > 50
        pushb(false);
        pushi(100);
        pushi(50);
        igt();
        bor();
        bnot();
        disp();
        // pop null
        // locals: iload istore
        // invoke=call
        // iret
        // new setf getf sets gets ineg
    }
    private static void pushc(int index) {
        runtime_stack.push(constant_pool[index]);
    }
    private static void pushi(int value) {
        runtime_stack.push(new Immediate<>(value));
    }
    private static void pushb(boolean value) {
        runtime_stack.push(new Immediate<>(value));
    }
    private static void eq() {
        Immediate right = runtime_stack.pop();
        Immediate left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value.equals(right.value)));
    }
    private static void iadd() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value + right.value));
    }
    private static void isub() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value - right.value));
    }
    private static void imul() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value * right.value));
    }
    private static void idiv() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value / right.value));
    }
    private static void ilt() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value < right.value));
    }
    private static void ile() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value <= right.value));
    }
    private static void igt() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value > right.value));
    }
    private static void ige() {
        Immediate<Integer> right = runtime_stack.pop();
        Immediate<Integer> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value >= right.value));
    }
    private static void band() {
        Immediate<Boolean> right = runtime_stack.pop();
        Immediate<Boolean> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value && right.value));
    }
    private static void bor() {
        Immediate<Boolean> right = runtime_stack.pop();
        Immediate<Boolean> left = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(left.value || right.value));
    }
    private static void bnot() {
        Immediate<Boolean> top = runtime_stack.pop();
        runtime_stack.push(new Immediate<>(!top.value));
    }
    private static void call() { /* pointer of function */ }
    private static void disp() {
        System.out.println(runtime_stack.pop().value);
    }
}
