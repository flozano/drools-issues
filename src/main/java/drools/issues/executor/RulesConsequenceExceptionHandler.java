package drools.issues.executor;

import org.drools.core.WorkingMemory;
import org.drools.core.spi.Activation;
import org.drools.core.spi.ConsequenceExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesConsequenceExceptionHandler implements ConsequenceExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RulesConsequenceExceptionHandler.class);

	public RulesConsequenceExceptionHandler() {
	}

	@Override
	public void handleException(Activation activation, WorkingMemory workingMemory, Exception exception) {
		String releaseId = workingMemory.getKnowledgeBase().getResolvedReleaseId().toString();
		LOGGER.error("Handling exception for rule '{}' (releaseId={})", activation.getRule().getName(), releaseId,
				exception);
	}

}
