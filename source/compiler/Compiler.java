package compiler;

import compiler.ast.S;
import compiler.lexical.Tokenizer;
import compiler.syntax.Parser;
import compiler.syntax.ParsingException;
import compiler.token.Token;

import java.io.IOException;

public class Compiler {
    public static void compile(String  path) throws IOException {
        Token[] tokens = new Tokenizer().tokenize(path);
        try {
            S[] result = new Parser().parse(tokens);
            for (S s: result) {
                System.out.println(s);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }
}
