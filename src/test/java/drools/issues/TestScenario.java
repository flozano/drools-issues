package drools.issues;

import org.kie.api.io.Resource;

import drools.issues.executor.ReuseStatelessSessionExecutor;
import drools.issues.executor.RulesExecutor;
import drools.issues.executor.RuntimeType;
import drools.issues.executor.SingleStatefulSessionExecutor;

public enum TestScenario {
	MVEL_STATEFUL(SingleStatefulSessionExecutor::new, RuntimeType.CLASSIC_MVEL),
	MVEL_STATELESS(ReuseStatelessSessionExecutor::new, RuntimeType.CLASSIC_MVEL),
	EXECUTABLE_MODEL_STATEFUL(SingleStatefulSessionExecutor::new, RuntimeType.EXECUTABLE_MODEL),
	EXECUTABLE_MODEL_STATELESS(ReuseStatelessSessionExecutor::new, RuntimeType.EXECUTABLE_MODEL);

	public final ExecutorFactory factory;
	public final RuntimeType runtimeType;

	TestScenario(ExecutorFactory factory, RuntimeType runtimeType) {
		this.factory = factory;
		this.runtimeType = runtimeType;
	}
	public interface ExecutorFactory {
		RulesExecutor create(String ruleSetReference, ClassLoader classLoader, RuntimeType runtimeType,
				Resource... resources);
	}
}
