package drools.issues.executor;

import org.drools.core.WorkingMemory;
import org.drools.core.rule.consequence.ConsequenceExceptionHandler;
import org.drools.core.rule.consequence.InternalMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesConsequenceExceptionHandler implements ConsequenceExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RulesConsequenceExceptionHandler.class);

	public RulesConsequenceExceptionHandler() {
	}

	@Override
	public void handleException(InternalMatch internalMatch, WorkingMemory workingMemory, Exception exception) {
		String releaseId = workingMemory.getKnowledgeBase().getResolvedReleaseId().toString();
		var ruleName = internalMatch.getRule().getName();
		LOGGER.error("Handling exception for rule '{}' (releaseId={})", ruleName, releaseId, exception);
	}

}
