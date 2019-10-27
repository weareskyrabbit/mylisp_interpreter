import compiler.Compiler;
import interpreter.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lisp {
    public static void main(String[] args) throws IOException {
        InputStreamReader reader = null;
        if (args.length == 0) {
            // prompt mode
            boolean redirect = System.console() == null;
            String prompt = "> ";
            while (true) {
                if (!redirect) {
                    System.out.print(prompt);
                    System.out.flush();
                }
                if (reader == null) reader = new InputStreamReader(System.in);
                Interpreter.interpret(reader);
            }
        } else if (args.length == 1) {
            // interpreter mode
            reader = new InputStreamReader(new FileInputStream(args[0] + ".lisp"));
            while (true) {
                Interpreter.interpret(reader);
            }
        } else {
            for (String path : args) {
                // compiler mode
                Compiler.compile(path);
            }
        }
    }
}