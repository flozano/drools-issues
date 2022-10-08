package drools.issues.executor;

import java.util.Collections;
import java.util.List;

public interface RulesExecutor extends AutoCloseable {

	String getVersion();

	void addGlobalVariable(String name, Object value);

	void clearGlobalVariables();

	ExecutionResult fire(Object... facts);

	@Override
	default void close() {
		clearGlobalVariables();
	}

	void check();

	public interface ExecutionResult {
		int FIRED_RULES_MAX = 10_000;

		int FIRED_RULES_WARNING = 500;

		List<?> getFacts();

		RulesExecutor getSource();

		List<?> getOutput();

		default boolean isOK() {
			return true;
		}

		static ExecutionResult error(RulesExecutor executor, RuntimeException e) {
			return new ErrorExecutionResult(executor, e);
		}

		int getNumberOfFiredRules();

		default boolean isExcessOfFiredRules() {
			return getNumberOfFiredRules() > FIRED_RULES_WARNING;
		}
	}

}

class ErrorExecutionResult implements RulesExecutor.ExecutionResult {
	private final RulesExecutor source;
	private final RuntimeException cause;

	ErrorExecutionResult(RulesExecutor source, RuntimeException cause) {
		this.source = source;
		this.cause = cause;
	}

	@Override
	public List<?> getFacts() {
		return Collections.emptyList();
	}

	@Override
	public RulesExecutor getSource() {
		return source;
	}

	public RuntimeException getCause() {
		return cause;
	}

	@Override
	public List<?> getOutput() {
		return Collections.emptyList();
	}

	@Override
	public boolean isOK() {
		return false;
	}

	@Override
	public int getNumberOfFiredRules() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "ErrorExecutionResult [source=" + source + ", cause=" + cause + "]";
	}

}
