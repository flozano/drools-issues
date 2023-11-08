package drools.issues;

import org.drools.io.ClassPathResource;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;

import drools.issues.executor.ReuseStatelessSessionExecutor;
import drools.issues.executor.RulesExecutor;
import drools.issues.executor.RuntimeType;
import drools.issues.executor.SingleStatefulSessionExecutor;

class AbstractTest {

	protected RulesExecutor executor(RuntimeType runtimeType, String drlName) {
		return new ReuseStatelessSessionExecutor(drlName, AbstractTest.class.getClassLoader(), runtimeType,
				build(drlName));
	}

	public Resource[] build(String drlName) {
		ClassPathResource bar = new ClassPathResource("drools/issues/" + drlName);
		bar.setResourceType(ResourceType.DRL);
		return new Resource[] { bar };
	}
}
