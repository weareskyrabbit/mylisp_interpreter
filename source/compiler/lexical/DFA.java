package compiler.lexical;

/* lexical.DFA: Deterministic Finite Automaton */
public class DFA<T> {
	private State state;
	final private RuleBook<T> rules;
	public DFA(State begin, RuleBook<T> rules) {
		this.state = begin;
		this.rules = rules;
	}
	public DFA input(T input) {
		state = rules.next(state, input).orElseThrow(IllegalArgumentException::new);
		return this;
	}
	public State getState() {
		return state;
	}
}

