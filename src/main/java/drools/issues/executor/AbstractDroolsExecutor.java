package drools.issues.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.drools.commands.runtime.rule.FireAllRulesCommand;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.conf.KieBaseMutabilityOption;
import org.kie.api.conf.KieBaseOption;
import org.kie.api.io.Resource;
import org.kie.api.management.GAV;
import org.kie.api.runtime.CommandExecutor;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSessionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractDroolsExecutor implements AutoCloseable, RulesExecutor {

	private static final String FIRED = "fired";
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDroolsExecutor.class);
	private static final String RESULT = "result";

	private final KieContainer kieContainer;
	private final KieBase kieBase;
	private final ReleaseId releaseId;
	private final String ruleSetReference;
	private final KieServices kieServices;
	private final KieCommands commands;

	private final KieSessionConfiguration sessionConfiguration;

	AbstractDroolsExecutor(String ruleSetReference, ClassLoader classLoader, RuntimeType runtimeType,
			Resource[] resources) {
		this(ruleSetReference, classLoader, runtimeType, List.of(KieBaseMutabilityOption.DISABLED), resources);
	}

	AbstractDroolsExecutor(String ruleSetReference, ClassLoader classLoader, RuntimeType runtimeType,
			List<KieBaseOption> kieBaseOptions, Resource... resources) {
		this.ruleSetReference = Objects.requireNonNull(ruleSetReference);
		this.releaseId = new GAV("drools.issues.rules", ruleSetReference, "1.0.0");
		this.kieServices = DroolsInternalFactory.instanceKieServices();
		this.sessionConfiguration = kieServices.newKieSessionConfiguration();
		this.commands = kieServices.getCommands();
		LOGGER.info("Creating new container (ruleSet={}, type={})", ruleSetReference, getClass().getSimpleName());
		kieContainer = DroolsInternalFactory.instanceKieContainer(kieServices, releaseId, classLoader, resources,
				runtimeType);
		kieBase = DroolsInternalFactory.instanceKieBase(kieBaseOptions, kieServices, kieContainer);
		LOGGER.info("New container created (ruleSet={}, type={})", ruleSetReference, getClass().getSimpleName());
	}

	protected KieSessionConfiguration getSessionConfiguration() {
		return sessionConfiguration;
	}

	@Override
	public String toString() {
		return String.format("AbstractDroolsExecutor [releaseId=%s, ruleSetReference=%s, class=%s]", releaseId,
				ruleSetReference, getClass().getSimpleName());
	}

	@Override
	public String getVersion() {
		return ruleSetReference;
	}

	protected KieServices getServices() {
		return kieServices;
	}

	protected KieContainer getContainer() {
		return kieContainer;
	}

	protected KieBase getBase() {
		return kieBase;
	}

	protected KieCommands getCommands() {
		return commands;
	}

	protected Object preProcess(Object fact) {
		return fact;
	}

	protected InternalExecutionResult executeBatch(CommandExecutor executor, Object... facts) {
		return new InternalExecutionResult(executor.execute(commands.newBatchExecution(prepareBatchCommands(facts))));
	}

	protected static class InternalExecutionResult {
		protected final int firedRules;
		protected final List<?> results;

		InternalExecutionResult(List<?> results, int firedRules) {
			this.firedRules = firedRules;
			this.results = results;
		}

		InternalExecutionResult(ExecutionResults r) {
			firedRules = (int) r.getValue(FIRED);
			results = new LinkedList<>((Collection<?>) r.getValue(RESULT));
		}
	}

	private List<Command<?>> prepareBatchCommands(Object[] facts) {
		List<Command<?>> cmds = new ArrayList<Command<?>>();
		for (Object fact : facts) {
			cmds.add(commands.newInsert(preProcess(fact)));
		}
		var fireAllRules = new FireAllRulesCommand(FIRED);
		fireAllRules.setMax(RulesExecutor.ExecutionResult.FIRED_RULES_MAX);
		cmds.add(fireAllRules);
		cmds.add(commands.newGetObjects(x -> {
			LOGGER.trace("Accepting {}", x);
			return true;
		}, RESULT));
		return cmds;
	}

	@Override
	public final void close() {
		try {
			disposeSession();
		} catch (Exception e) {
			LOGGER.error("Error while disposing session", e);
		}
		try {
			LOGGER.info("Closing container (ruleSet={})", ruleSetReference);
			kieContainer.dispose();
		} catch (Exception e) {
			LOGGER.error("Error while closing container", e);
		}
	}

	protected abstract void disposeSession() throws Exception;

	protected class ExecutionResultImpl implements ExecutionResult {
		private final List<?> output;
		private final List<?> facts;
		private final int numberOfFiredRules;

		public ExecutionResultImpl(Object[] facts, List<?> output, int numberOfFiredRules) {
			this(Arrays.asList(facts), output, numberOfFiredRules);
		}

		public ExecutionResultImpl(List<?> facts, List<?> output, int numberOfFiredRules) {
			this.output = output;
			this.facts = facts;
			this.numberOfFiredRules = numberOfFiredRules;
		}

		@Override
		public RulesExecutor getSource() {
			return AbstractDroolsExecutor.this;
		}

		@Override
		public List<?> getOutput() {
			return output;
		}

		@Override
		public List<?> getFacts() {
			return facts;
		}

		@Override
		public String toString() {
			return String.format("ExecutionResultImpl [output=%s]", output);
		}

		@Override
		public int getNumberOfFiredRules() {
			return numberOfFiredRules;
		}

	}

}
