package compiler.lexer;

public class Tag {
    /* static variables */
    public static final int
    QUOTE   = 256, LAMBDA = 257, NIL    = 258, TRUE    = 269, FALSE   = 270,
    IF      = 271, DEFINE = 272, SET_E  = 273, CAR     = 274, CDR     = 275,
    CONS    = 276, LIST   = 277, EQ_Q   = 278, EQUAL_Q = 279, PAIR_Q  = 280,
    DISPLAY = 281, LET    = 282, LET_A  = 283, LETREC  = 284, BEGIN   = 285,
    LE      = 286, GE     = 287, MINUS  = 288, QUIT    = 289, NUMBER  = 290,
    REAL    = 291, SYMBOL = 292, STRING = 293, TEMP    = 294, EOF     = 295;
}
