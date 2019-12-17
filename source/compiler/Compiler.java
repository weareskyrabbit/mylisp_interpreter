package compiler;

import compiler.ast.S;
import compiler.lexical.Tokenizer;
import compiler.semantic.SemanticException;
import compiler.syntax.Parser;
import compiler.syntax.SyntaxException;
import compiler.token.Token;

import java.io.IOException;

public class Compiler {
    public static void compile(String  path) throws IOException {
        Token[] tokens = new Tokenizer().tokenize(path);
        S[] result = null;
        try {
            result = new Parser().parse(tokens);
        } catch (SyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            for (S s: result) {
                System.out.println(s.evaluate());
            }
        } catch (SemanticException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
