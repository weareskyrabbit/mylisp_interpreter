package compiler.parser;

import compiler.lexer.*;
import compiler.symbols.Array;
import compiler.symbols.Closure;
import compiler.symbols.Type;

import java.io.IOException;

public class Parser {
    /* member variables */
    private Lexer lex;
    private Token look;
    Closure top = null;
    int used = 0;
    /* constructors */
    public Parser(Lexer lexer) throws IOException {
        lex = lexer;
        move();
    }
    /* member functions */
    void move() throws IOException {
        look = lex.scan();
    }
    void error(String message) {
        throw new Error("near line " + lex.line + ": " + message);
    }
    void match(int tag) throws IOException {
        if (look.tag == tag) move();
        else error("syntax error");
    }
    // program -> block
    public void program() throws IOException {
        Stmt stmt = block();
        int begin = stmt.newlabel();
        int after = stmt.newlabel();
        stmt.emitlabel(begin);
        stmt.gen(begin, after);
        stmt.emitlabel(after);
    }
    Stmt block() throws IOException { // block -> { decls stmts }
        match('{');
        Closure savedClosure = top;
        top = new Closure(top);
        decls();
        Stmt stmt = stmts();
        match('}');
        top = savedClosure;
        return stmt;
    }
    void decls() throws IOException { // D -> type ID
        while (look.tag == Tag.BASIC) {
            Type type = type();
            Token token = look;
            match(Tag.ID);
            match(';');

            Id id = new Id((Word) token, type, used);
            top.put(token, id);
            used += type.width;
        }
    }
    Type type() throws IOException {
        Type type = (Type) look;
        match(Tag.BASIC);
        if (look.tag != '[') return type; // T -> basic
        else return dims(type);
    }
    Type dims(Type type) throws IOException{
        match('[');
        Token token = look;
        match(Tag.NUM);
        match(']');
        if (look.tag == '[') type = dims(type);
        return new Array(((Num) token).value, type);
    }
    Stmt stmts() throws IOException{
        if (look.tag == '}') return Stmt.Null;
        else return new Seq(stmt(), stmts());
    }
    Stmt stmt() throws IOException {
        Expr expr; Stmt stmt, stmt1, stmt2, savedStmt;
        switch (look.tag) {
            case ';':
                move();

                return Stmt.Null;
            case Tag.IF:
                match(Tag.IF); match('('); expr = bool(); match(')'); stmt1 = stmt();
                if (look.tag != Tag.ELSE) return new If(expr, stmt1);
                match(Tag.ELSE); stmt2 = stmt();

                return new Else(expr, stmt1, stmt2);
            case Tag.WHILE:
                While while_node = new While();
                savedStmt = Stmt.Enclosing; Stmt.Enclosing = while_node;

                match(Tag.WHILE); match('('); expr = bool(); match(')'); stmt1 = stmt();

                while_node.init(expr, stmt1);
                Stmt.Enclosing = savedStmt;
                return while_node;
            case Tag.DO:
                Do do_node = new Do();
                savedStmt = Stmt.Enclosing; Stmt.Enclosing = do_node;

                match(Tag.DO); stmt1 = stmt(); match(Tag.WHILE); match('('); expr = bool(); match(')'); match(';');

                do_node.init(stmt1, expr);
                Stmt.Enclosing = savedStmt;
                return do_node;
            case Tag.BREAK:
                match(Tag.BREAK); match(';');

                return new Break();
            case '{':
                return block();
            default:
                return assign();
        }
    }
    Stmt assign() throws IOException {
        Stmt stmt; Token token = look;

        match(Tag.ID);

        Id id = top.get(token);
        if (id == null) error(token.toString() + " undeclared");

        if (look.tag == '=') { // S -> id = E
            move(); stmt = new Set(id, bool());
        } else { // S -> L = E
            Access access = offset(id); match('='); stmt = new SetElem(access, bool());
        }
        match(';');

        return stmt;
    }
    Expr bool() throws IOException {
        Expr expr = join();
        while (look.tag == Tag.OR) {
            Token token = look;
            move();
            expr = new Or(token, expr, join());
        }
        return expr;
    }
    Expr join() throws IOException {
        Expr expr = equality();
        while(look.tag == Tag.AND) {
            Token token = look;
            move();
            expr = new And(token, expr, equality());
        }
        return expr;
    }
    Expr equality() throws IOException {
        Expr expr = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            Token token = look;
            move();
            expr = new Rel(token, expr, rel());
        }
        return expr;
    }
    Expr rel() throws IOException {
        Expr expr = expr();
        switch (look.tag) {
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token token = look;
                move();
                return new Rel(token, expr, expr());
            default:
                return expr;
        }
    }
    Expr expr() throws IOException {
        Expr expr = term();
        while (look.tag == '+' || look.tag == '-') {
            Token token = look;
            move();
            expr = new Arith(token, expr, term());
        }
        return expr;
    }
    Expr term() throws IOException {
        Expr expr = unary();
        while (look.tag == '*' || look.tag == '/') {
            Token token = look;
            move();
            expr = new Arith(token, expr, unary());
        }
        return expr;
    }
    Expr unary() throws IOException {
        if (look.tag == '-') {
            move();
            return new Unary(Word.minus, unary());
        } else if (look.tag == '!') {
            Token token = look;
            move();
            return new Not(token, unary());
        } else return factor();
    }
    Expr factor() throws IOException {
        Expr expr = null;
        switch (look.tag) {
            case '(':
                move(); expr = bool(); match(')');

                return expr;
            case Tag.NUM:
                expr = new Constant(look, Type.Int);

                move(); return expr;
            case Tag.REAL:
                expr = new Constant(look, Type.Float);

                move(); return expr;
            case Tag.TRUE:
                move(); return Constant.True;
            case Tag.FALSE:
                move(); return Constant.False;
            case Tag.ID:
                Id id = top.get(look);
                if (id == null) error(look.toString() + " undeclared");
                move();
                if (look.tag != '[') return id;
                else return offset(id);
            default:
                error("syntax error");
                return expr;
        }
    }
    Access offset(Id array) throws IOException { // I -> [E] | [E] I
        Expr loc; Expr index, width, expr1, expr2; Type type = array.type;

        match('['); index = bool(); match(']'); // I -> [ E ]

        type = ((Array) type).of;
        width = new Constant(type.width);
        expr1 = new Arith(new Token('*'), index, width);
        loc = expr1;

        while (look.tag == '[') { // I -> [ E ] I
            match('['); index = bool(); match(']');

            type = ((Array) type).of;
            width = new Constant(type.width);
            expr1 = new Arith(new Token('*'), index, width);
            expr2 = new Arith(new Token('+'), loc, expr1);
            loc = expr2;
        }
        return new Access(array, loc, type);
    }
}