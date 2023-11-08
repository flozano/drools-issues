package drools.issues.executor;

import org.drools.core.base.MapGlobalResolver;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.StatelessKieSession;

public class ReuseStatelessSessionExecutor extends AbstractDroolsExecutor {
	private final MapGlobalResolver sessionGlobals = new MapGlobalResolver();
	private static final int SESSION_POOL_INITIAL_SIZE = 5;

	private final StatelessKieSession session;
	private final KieSessionsPool pool;

	public ReuseStatelessSessionExecutor(String ruleSetReference, ClassLoader classLoader,
			RuntimeType runtimeType, Resource... resources) {
		super(ruleSetReference, classLoader, runtimeType, resources);

		pool = getBase().newKieSessionsPool(SESSION_POOL_INITIAL_SIZE);
		session = pool.newStatelessKieSession(getSessionConfiguration());
		session.getGlobals().setDelegate(sessionGlobals);
	}

	@Override
	public ExecutionResult fire(Object... facts) {
		var batchResult = executeBatch(session, facts);
		return new ExecutionResultImpl(facts, batchResult.results, batchResult.firedRules);
	}

	@Override
	public void check() {
		try {
			LOGGER.debug("Checking (ruleSet={})...", getVersion());
			getContainer().newStatelessKieSession();
			LOGGER.debug("Check finished (ruleSet={})", getVersion());
		} catch (final RuntimeException e) {
			LOGGER.error("Error while checking", e);
			throw e;
		}
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
	protected void disposeSession() throws Exception {
		pool.shutdown();
	}
}