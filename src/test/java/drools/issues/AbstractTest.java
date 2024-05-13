package drools.issues;

import org.drools.io.ClassPathResource;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;

import drools.issues.executor.RulesExecutor;

class AbstractTest {

	protected RulesExecutor executor(TestScenario scenario, String drlName) {
		return executor(scenario, drlName, AbstractTest.class.getClassLoader());
	}

	protected RulesExecutor executor(TestScenario scenario, String drlName, ClassLoader classLoader) {
		return scenario.factory.create(drlName, classLoader, scenario.runtimeType, build(drlName));
	}

	public Resource[] build(String drlName) {
		ClassPathResource bar = new ClassPathResource("drools/issues/" + drlName);
		bar.setResourceType(ResourceType.DRL);
		return new Resource[] { bar };
	}
}
