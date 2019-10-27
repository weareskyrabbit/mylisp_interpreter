package compiler;

import ast.S;
import io.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    public static void compile(String  path) throws IOException {
        List<S> s_exp = new ArrayList<>();
        try {
            s_exp = new Parser().parse(path);
        } catch (ParsingException e) {
            System.out.println(e.toString());
        }
        for (S s : s_exp) {
            Writer.use(path + ".s", w -> w.write(s.toString()));
        }
    }
}
