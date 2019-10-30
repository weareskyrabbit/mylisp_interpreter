package compiler;

import compiler.lexer.Lexer;
import compiler.parser.Parser;

import java.io.IOException;

public class Compiler {
    public static void compile(String  path) throws IOException {
        new Parser(new Lexer(path)).program();
        System.out.write('\n');
    }
}
