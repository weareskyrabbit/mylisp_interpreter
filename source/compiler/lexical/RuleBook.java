package compiler.lexical;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RuleBook<T> {
	Set<Rule<T>> rules = new HashSet<>();
	public RuleBook setRule(Rule<T> rule) {
		rules.add(rule);
		return this;
	}
	public Optional<State> next(State state, T input) {
		return rules.stream()
				.filter(rule -> rule.isApplicableTo(state, input))
				.map(Rule::next)
				.findFirst();
	}
}
