import lisp.S;
import lisp.Scanner;
import lisp.Symbol;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
    public static void main(String[] args) throws IOException {
        boolean redirect = System.console() == null;
        String prompt = "> ";
        Scanner scanner = null;
        if (args.length > 1) {
            System.out.println("Usage: lisp [-i] [-c] <file>");
            System.exit(1);
        } else if (args.length == 1) {
            scanner = new Scanner(new InputStreamReader(new FileInputStream(args[0])));
        }
        while (true) {
            if (!redirect) {
                System.out.print(prompt);
                System.out.flush();
            }
            if (scanner == null)
                scanner = new Scanner(new InputStreamReader(System.in));
            try {
                S input = scanner.read();
                if (input == Scanner.EOF_OBJECT || input == Symbol.of("quit"))
                    break;
                S evaled = eval(input);
                System.out.println(evaled);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
