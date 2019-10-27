package compiler.token;

// TODO implement type system
public class Type extends Word {
    public int width = 0;
    public Type(String lexeme, int tag, int width) {
        super(lexeme, tag);
        this.width = width;
    }
    public static final Type
        Int        = new Type("int",       Tag.BASIC, 4), // TODO rename
        Float      = new Type("float",     Tag.BASIC, 8), // TODO rename
        Byte       = new Type("Byte",      Tag.BASIC, 1), // TODO implement
        Character  = new Type("Character", Tag.BASIC, 1),
        Boolean    = new Type("Boolean",   Tag.BASIC, 1);
    public static boolean numeric(Type type) {
        return type == Type.Character || type == Type.Float || type == Type.Int;
    }
    public static Type max(Type type1, Type type2) {
        if (!numeric(type1) || !numeric(type2)) return null; //TODO throw Exception
        else if (type1 == Type.Float || type2 == Type.Float) return Type.Float;
        else if (type1 == Type.Int || type2 == Type.Int) return Type.Int;
        else return Type.Character;
    }
}