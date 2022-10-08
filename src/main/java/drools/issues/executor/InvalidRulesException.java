package drools.issues.executor;

import java.util.stream.Collectors;

import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;

public final class InvalidRulesException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidRulesException(ReleaseId releaseId, Results results, StringBuilder drl) {
		super(msg(releaseId, results, drl));
	}

	private static String msg(ReleaseId releaseId, Results results, StringBuilder drl) {
		return "Invalid rules " + releaseId + ": "
				+ results.getMessages().stream().map(Object::toString).collect(Collectors.joining("\n,", "\n", ""))
				+ "\nDRL: " + drl.toString();
	}

}