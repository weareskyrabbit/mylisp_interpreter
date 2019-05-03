package lisp;

public interface List extends S {
    /* public static final */ List NIL = new List() {
        public String toString() {
            return "()";
        }
    };
}
