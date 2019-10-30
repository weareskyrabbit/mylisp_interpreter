package compiler.lexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class Lexer {
    /* member variables */
    InputStreamReader reader;
    char peek = ' ';
    Hashtable<String, Word> words = new Hashtable<>();

    /* constructors */
    public Lexer(String path) {
        try {
            reader = new InputStreamReader(new FileInputStream(path));
        } catch (IOException e) {
            System.out.println(path + " is unavailable: switching to prompt mode");
            reader = new InputStreamReader(System.in);
        }

        reserve(new Word("quote", Tag.QUOTE));
        reserve(new Word("lambda", Tag.LAMBDA));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("define", Tag.DEFINE));
        reserve(new Word("set!", Tag.SET_E));
        reserve(new Word("car", Tag.CAR));
        reserve(new Word("cdr", Tag.CDR));
        reserve(new Word("cons", Tag.CONS));
        reserve(new Word("list", Tag.LIST));
        reserve(new Word("eq?", Tag.EQ_Q));
        reserve(new Word("equal?", Tag.EQUAL_Q));
        reserve(new Word("pair?", Tag.PAIR_Q));
        reserve(new Word("display", Tag.DISPLAY));
        reserve(new Word("let", Tag.LET));
        reserve(new Word("let*", Tag.LET_A));
        reserve(new Word("letrec", Tag.LETREC));
        reserve(new Word("begin", Tag.BEGIN));
        reserve(new Word("QUIT", Tag.QUIT));
        reserve(Word.True);
        reserve(Word.False);
        reserve(Word.Nil);
//        reserve(Type.Int);
//        reserve(Type.Char);
//        reserve(Type.Bool);
//        reserve(Type.Float);
    }

    /* member functions */
    void reserve(Word word) {
        words.put(word.lexeme, word);
    }

    void readch() throws IOException {
        peek = (char) reader.read();
    }

    boolean readch(char next) throws IOException {
        readch();
        if (peek != next) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for (; ; readch()) {
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line++;
            else break;
        }
        switch (peek) {
            case '<':
                if (readch('=')) return Word.le;
                else return new Token('<');
            case '>':
                if (readch('=')) return Word.ge;
                else return new Token('>');
        }
        if (Character.isDigit(peek)) {
            int int_value = 0;
            do {
                int_value = 10 * int_value + Character.digit(peek, 10);
                readch();
            } while (Character.isDigit(peek));
            if (peek != '.') return new Number(int_value);

            float float_value = int_value;
            for (int i = 0; ; i++) {
                readch();
                if (!Character.isDigit(peek)) break;
                float_value += Character.digit(peek, 10) / Math.pow(10, i + 1);
            }
            return new Real(float_value);
        }
        if (Character.isLetter(peek)) {
            StringBuilder builder = new StringBuilder();
            do {
                builder.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek) || peek == '-' || peek == '/'
                    || peek == '!' || peek == '?');
            String lexeme = builder.toString();
            if (words.contains(lexeme)) return words.get(lexeme);
            Word word = new Word(lexeme, Tag.SYMBOL);
            words.put(lexeme, word);
            return word;
        }
        Token token = new Token(peek);
        peek = ' ';
        return token;
    }

    /* static variables */
    public static int line = 1;
}
