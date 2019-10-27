package compiler;

import compiler.token.*;
import compiler.token.Number;

import java.util.HashMap;
import java.util.Map;

public class Tokenizer {
    private char[] input;
    private int position;
    private int line;
    private int offset; // TODO
    private Map<String, Word> words;
    private TokenizerState state;

    Tokenizer(final String input) {
        this.input = input.toCharArray();
        this.position = 0;
        this.line = 1;
        this.offset = 0;
        this.state = TokenizerState.STANDARD;
        this.words = new HashMap<>();
        reserve(new Word("if",     Tag.IF));
        reserve(new Word("else",   Tag.ELSE));
        reserve(new Word("while",  Tag.WHILE));
        reserve(new Word("do",     Tag.DO));
        reserve(new Word("break",  Tag.BREAK));
        reserve(new Word("return", Tag.RET));
        reserve(new Word("print",  Tag.PRINT));
        reserve(new Word("int",    Tag.INT));
    }
    private void reserve(final Word word) {
        words.put(word.lexeme, word);
    }
    Token tokenize() {
        if (position >= input.length) {
            return Token.EOF;
        }
        switch (state) {
            case STANDARD:
                while (Character.isWhitespace(input[position])) {
                    if (input[position] == '\n') {
                        line++; offset = 0;
                    }
                    position++;
                }
                switch (input[position]) {
                    case '&':
                        position++;
                        if (input[position] == '&') {
                            position++;
                            return Word.and; // &&
                        } else {
                            return new Token(line, offset, '&');
                        }
                    case '|':
                        position++;
                        if (input[position] == '|') {
                            position++;
                            return Word.or; // ||
                        } else {
                            return new Token(line, offset, '|');
                        }
                    case '=':
                        position++;
                        if (input[position] == '=') {
                            position++;
                            return Word.eq; // ==
                        } else {
                            return new Token(line, offset, '=');
                        }
                    case '!':
                        position++;
                        if (input[position] == '=') {
                            position++;
                            return Word.ne; // !=
                        } else {
                            return new Token(line, offset, '!');
                        }
                    case '<':
                        position++;
                        if (input[position] == '=') {
                            position++;
                            return Word.le; // <=
                        } else {
                            return new Token(line, offset, '<');
                        }
                    case '>':
                        position++;
                        if (input[position] == '=') {
                            position++;
                            return Word.ge; // >=
                        } else {
                            return new Token(line, offset, '>');
                        }
                    case '/':
                        position++;
                        if (input[position] == '/') {
                            position++;
                            state = TokenizerState.SHORT_COMMENT;
                            return tokenize();
                        } else if (input[position] == '*') {
                            position++;
                            state = TokenizerState.LONG_COMMENT;
                            return tokenize();
                        } else {
                            return new Token(line, offset, '/');
                        }
                    case '\"':
                        position++;
                        state = TokenizerState.STRING;
                        return tokenize();
                }
                if (Character.isDigit(input[position])) {
                    int int_value = 0;
                    do {
                        int_value = 10 * int_value + Character.digit(input[position], 10);
                        position++;
                    } while (Character.isDigit(input[position]));
                    if (input[position] != '.') {
                        return new Number(line, offset, int_value);
                    }

                    float float_value = int_value;
                    for (int i = 0; ; i++) {
                        position++;
                        if (!Character.isDigit(input[position])) break;
                        float_value += Character.digit(input[position], 10) / Math.pow(10, i + 1);
                    }
                    return new Real(line, offset, float_value);
                } else if (Character.isLetter(input[position])) {
                    StringBuilder builder = new StringBuilder();
                    do {
                        builder.append(input[position]);
                        position++;
                    } while (Character.isLetterOrDigit(input[position]));
                    String lexeme = builder.toString();
                    if (words.containsKey(lexeme)) {
                        return words.get(lexeme);
                    }
                    Word word = new Word(line, offset, lexeme, Tag.SYMBOL);
                    words.put(lexeme, word);
                    return word;
                } else {
                    Token token = new Token(line, offset, input[position]);
                    position++;
                    return token;
                }
            case STRING:
                StringBuilder builder = new StringBuilder();
                while (input[position] != '\"') {
                    builder.append(input[position]);
                    if (input[position] == '\n') {
                        line++; offset = 0;
                    }
                    position++;
                }
                position++;
                state = TokenizerState.STANDARD;
                return new String_(line, offset, builder.toString());
            case SHORT_COMMENT:
                while (input[position] != '\n') {
                    position++;
                }
                line++; offset = 0;
                position++;
                state = TokenizerState.STANDARD;
                return tokenize();
            case LONG_COMMENT:
                while (input[position] != '*' || input[position + 1] != '/') {
                    if (input[position] == '\n') {
                        line++; offset = 0;
                    }
                    position++;
                }
                position += 2;
                state = TokenizerState.STANDARD;
                return tokenize();
        }
        return tokenize();
    }
}
