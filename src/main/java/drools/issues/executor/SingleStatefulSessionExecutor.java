package drools.issues.executor;

import org.drools.core.base.MapGlobalResolver;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionsPool;

public class SingleStatefulSessionExecutor extends AbstractDroolsExecutor implements RulesExecutor {
	private static final int INITIAL_POOL_SIZE = 5;
	private final MapGlobalResolver sessionGlobals = new MapGlobalResolver();
	private final KieSessionsPool pool;

	public SingleStatefulSessionExecutor(String ruleSetReference, ClassLoader classLoader, RuntimeType runtimeType,
			Resource... resources) {
		super(ruleSetReference, classLoader, runtimeType, resources);
		pool = getBase().newKieSessionsPool(INITIAL_POOL_SIZE);
	}

	@Override
	public ExecutionResult fire(Object... facts) {
		KieSession session = null;
		try {
			session = pool.newKieSession(SESSION_CONFIGURATION);
			session.getGlobals().setDelegate(sessionGlobals);
			var batchResult = executeBatch(session, facts);
			return new ExecutionResultImpl(facts, batchResult.results, batchResult.firedRules);
		} finally {
			if (session != null) {
				session.dispose();
			}
		}
	}

	@Override
	public void check() {
		getContainer().newKieSession().destroy();
	}

	@Override
	public void addGlobalVariable(String name, Object value) {
		sessionGlobals.set(name, value);
	}

	@Override
	public void clearGlobalVariables() {
		sessionGlobals.clear();
	}

	@Override
	protected void disposeSession() {
		clearGlobalVariables();
		pool.shutdown();
	}

}