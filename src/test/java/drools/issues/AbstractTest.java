package drools.issues;

import org.drools.core.io.impl.ClassPathResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;

import drools.issues.executor.RulesExecutor;
import drools.issues.executor.RuntimeType;
import drools.issues.executor.SingleStatefulSessionExecutor;

class AbstractTest {
	private TestInfo testInfo;

	@BeforeEach
	public void init(TestInfo testInfo) {
		this.testInfo = testInfo;
	}

	protected RulesExecutor executor(RuntimeType runtimeType, String drlName) {
		return new SingleStatefulSessionExecutor(testInfo.getTestMethod().get().getName(),
				AbstractTest.class.getClassLoader(), runtimeType, build(drlName));
	}

	public Resource[] build(String drlName) {
		ClassPathResource bar = new ClassPathResource("drools/issues/" + drlName);
		bar.setResourceType(ResourceType.DRL);
		return new Resource[] { bar };
	}
}
