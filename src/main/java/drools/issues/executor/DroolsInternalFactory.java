package drools.issues.executor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.drools.model.codegen.ExecutableModelProject;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.conf.KieBaseOption;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.conf.ConsequenceExceptionHandlerOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DroolsInternalFactory {

	protected static final Logger LOGGER = LoggerFactory.getLogger(DroolsInternalFactory.class);

	private DroolsInternalFactory() {
	}

	static KieContainer instanceKieContainer(KieServices kieServices, ReleaseId releaseId, ClassLoader classLoader,
			Resource[] resources, RuntimeType runtimeType) {

		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		StringBuilder drl = new StringBuilder();
		for (Resource resource : resources) {
			LOGGER.debug("Loading rules (resource={})", resource.getSourcePath());
			logTraceDRL(resource, drl);
			kieFileSystem = kieFileSystem.write(resource);
			LOGGER.debug("Rules loaded (resource={})", resource.getSourcePath());
		}
		LOGGER.debug("Building (releaseId={})", releaseId);
		kieFileSystem.generateAndWritePomXML(releaseId);
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem, classLoader);
		if (runtimeType == RuntimeType.EXECUTABLE_MODEL) {
			kieBuilder.buildAll(ExecutableModelProject.class);
		} else {
			kieBuilder.buildAll();
		}
		LOGGER.debug("Built (releaseId={})", releaseId);
		Results results = kieBuilder.getResults();
		if (results.getMessages().size() > 0) {
			LOGGER.warn("KieBuilder has errors (releaseId={}): {}", releaseId, results);
			throw new InvalidRulesException(releaseId, results, drl);
		}
		LOGGER.debug("Creating container (releaseId={})", releaseId);
		KieContainer result = kieServices.newKieContainer(releaseId, classLoader);
		LOGGER.debug("Container created (releaseId={})", releaseId);
		return result;
	}

	static KieBase instanceKieBase(List<KieBaseOption> kieBaseOptions, KieServices kieServices,
			KieContainer kieContainer) {
		KieBaseConfiguration config = kieServices.newKieBaseConfiguration();
		kieBaseOptions.forEach(config::setOption);
		config.setProperty(ConsequenceExceptionHandlerOption.PROPERTY_NAME,
				RulesConsequenceExceptionHandler.class.getName());
		return kieContainer.newKieBase(config);
	}

	static void logTraceDRL(Resource resource, StringBuilder allDrl) {
		if (LOGGER.isTraceEnabled()) {
			if (ResourceType.DTABLE.equals(resource.getResourceType())) {
				DecisionTableConfiguration configuration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
				configuration.setInputType(DecisionTableInputType.XLS);

				DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();
				String drl = decisionTableProvider.loadFromResource(resource, configuration);

				LOGGER.trace("Generated DRL for DecisionTable resource {} is: {}", resource.getSourcePath(), drl);

				allDrl.append("\n// DRL from decision table ").append(resource.getSourcePath()).append("\n");
				allDrl.append(drl);
				allDrl.append("\n//\n");
			} else if (ResourceType.DRL.equals(resource.getResourceType())) {
				try (InputStream is = resource.getInputStream()) {
					allDrl.append("\n// DRL directly from ").append(resource.getSourcePath()).append("\n");
					allDrl.append(IOUtils.toString(is, StandardCharsets.UTF_8));
					allDrl.append("\n//\n");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	static KieServices instanceKieServices() {
		return KieServices.Factory.get();
	}
}
