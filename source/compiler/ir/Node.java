package compiler.ir;

import compiler.lexer.Lexer;

public class Node {
    /* member variables */
    int lexline = 0;
    /* constructors */
    Node() {
        lexline = Lexer.line;
    }
    /* member functions */
    void error(String message) {
        throw new Error("near line " + lexline + ": " + message);
    }
    public int newlabel() {
        return ++labels;
    }
    public void emitlabel(int index) {
        System.out.print("L" + index + ":");
    }
    public void emit(String code) {
        System.out.println("\t" + code);
    }
    /* static variables */
    static int labels = 0;
}
