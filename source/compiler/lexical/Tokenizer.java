package compiler.lexical;

import compiler.token.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

public class Tokenizer {
    private int raw;
    private int column;
    private static final List<Character> whitespaces = List.of(
            ' ', '\t', '\n', '\r'
    );
    private static final List<Character> alphabets = List.of(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z');
    private static final List<Character> numbers = List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    );
    private static final List<Character> signs = List.of(
            '!', '$', '%', '&', '(', ')', '*', '+', ',', '-',
            '.', '/', ':', ';', '<', '=', '>', '?', '@', '[',
            '\\', ']', '^', '_', '`', '{', '|', '}', '~'
    );
    private static final List<String> keywords = List.of(
            "true", "false"
    );
    static enum LexicalState implements State {
        WHITESPACE,
        SYMBOL {
            @Override public String toString() { return "SYMBOL"; }
        } ,
        NUMBER {
            @Override public String toString() { return "NUMBER"; }
        },
        STRING{
            @Override public String toString() { return "STRING"; }
        },
        COMMENT
    }
    public Token[] tokenize(String path) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path + ".lisp"));
        final Vector<Token> tokens = new Vector<>();
        final RuleBook<Character> rules = new RuleBook<>();
        final DFA<Character> dfa = new DFA<>(LexicalState.WHITESPACE, rules);
        final StringBuilder builder = new StringBuilder();

        /* rules */
        for (char c: whitespaces) {
            rules.setRule(new Rule<>(LexicalState.WHITESPACE, c, LexicalState.WHITESPACE));
            rules.setRule(new Rule<>(LexicalState.SYMBOL, c,LexicalState.WHITESPACE) {
                @Override public State next() {
                    tokens.add(new Symbol(builder.toString()));
                    builder.delete(0, builder.length());
                    return super.next(); }
            });
            rules.setRule(new Rule<>(LexicalState.NUMBER, c,LexicalState.WHITESPACE)  {
                @Override public State next() {
                    tokens.add(new NumberLiteral(builder.toString()));
                    builder.delete(0, builder.length());
                    return super.next(); }
            });
            rules.setRule(new Rule<>(LexicalState.STRING, c, LexicalState.STRING) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.COMMENT, c, LexicalState.COMMENT));
        }
        for (char c: alphabets) {
            rules.setRule(new Rule<>(LexicalState.WHITESPACE, c, LexicalState.SYMBOL) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.SYMBOL, c, LexicalState.SYMBOL) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.NUMBER, c, LexicalState.SYMBOL) {
                @Override public State next() {
                    tokens.add(new Token(Tag.NUMBER));
                    builder.delete(0, builder.length()).append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.STRING, c, LexicalState.STRING) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.COMMENT, c, LexicalState.COMMENT));
        }
        for (char c: numbers) {
            rules.setRule(new Rule<>(LexicalState.WHITESPACE, c, LexicalState.NUMBER) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.SYMBOL, c, LexicalState.SYMBOL) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.NUMBER, c, LexicalState.NUMBER) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.STRING, c, LexicalState.STRING) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.COMMENT, c, LexicalState.COMMENT));
        }
        for (char c: signs) {
            rules.setRule(new Rule<>(LexicalState.WHITESPACE, c, LexicalState.WHITESPACE) {
                @Override
                public State next() {
                    tokens.add(new Token(c));
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.SYMBOL, c, LexicalState.WHITESPACE) {
                @Override
                public State next() {
                    tokens.add(new Symbol(builder.toString()));
                    builder.delete(0, builder.length());
                    tokens.add(new Token(c));
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.NUMBER, c, LexicalState.WHITESPACE) {
                @Override
                public State next() {
                    tokens.add(new NumberLiteral(builder.toString()));
                    builder.delete(0, builder.length());
                    tokens.add(new Token(c));
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.STRING, c, LexicalState.STRING) {
                @Override public State next() {
                    builder.append(c);
                    return super.next();
                }
            });
            rules.setRule(new Rule<>(LexicalState.COMMENT, c, LexicalState.COMMENT));
        }
        rules.setRule(new Rule<>(LexicalState.WHITESPACE, '"', LexicalState.STRING));
        rules.setRule(new Rule<>(LexicalState.STRING, '"', LexicalState.WHITESPACE) {
            @Override public State next() {
                tokens.add(new StringLiteral(builder.toString()));
                builder.delete(0, builder.length());
                return super.next();
            }
        });
        rules.setRule(new Rule<>(LexicalState.WHITESPACE, '#', LexicalState.COMMENT));
        rules.setRule(new Rule<>(LexicalState.COMMENT, '#', LexicalState.WHITESPACE));

        /* loop to tokenize */
        while (true) {
            int tmp = reader.read();
            if (tmp < 0) break;
            if (tmp == '\n') { raw++; column = 0; }
            else { column++; }
            dfa.input((char) tmp);
        }
        return tokens.toArray(new Token[0]);
    }
}
