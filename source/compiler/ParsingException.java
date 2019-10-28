package compiler;

public class ParsingException extends Exception {
    private final String message;
    ParsingException(final String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return message;
    }
}
