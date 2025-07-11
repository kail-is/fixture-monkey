package com.navercorp.fixturemonkey.tests.java;

import static com.navercorp.fixturemonkey.tests.TestEnvironment.TEST_COUNT;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.RepeatedTest;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.tests.java.specs.MutableSpecs.ContainerObject;
import com.navercorp.fixturemonkey.tests.java.specs.MutableSpecs.DateTimeObject;
import com.navercorp.fixturemonkey.tests.java.specs.MutableSpecs.JavaTypeObject;
import com.navercorp.fixturemonkey.tests.java.specs.NoSetterSpecs;

class BeanArbitraryIntrospectorTest {
	private static final FixtureMonkey SUT = FixtureMonkey.builder()
		.objectIntrospector(BeanArbitraryIntrospector.INSTANCE)
		.defaultNotNull(true)
		.build();

	@RepeatedTest(TEST_COUNT)
	void sampleJavaType() {
		JavaTypeObject actual = SUT.giveMeOne(JavaTypeObject.class);

		then(actual).isNotNull();
	}

	@RepeatedTest(TEST_COUNT)
	void sampleDateTime() {
		DateTimeObject actual = SUT.giveMeOne(DateTimeObject.class);

		then(actual).isNotNull();
	}

	@RepeatedTest(TEST_COUNT)
	void sampleContainer() {
		ContainerObject actual = SUT.giveMeOne(ContainerObject.class);

		then(actual).isNotNull();
	}

	@RepeatedTest(TEST_COUNT)
	void setPropertyPostCondition() {
		String actual = SUT.giveMeBuilder(JavaTypeObject.class)
			.setPostCondition("string", String.class, str -> str.length() > 5)
			.sample()
			.getString();

		then(actual).hasSizeGreaterThan(5);
	}

	@RepeatedTest(TEST_COUNT)
	void objectGenerationShouldWorkWithoutSetter() {
		NoSetterSpecs.StringObject actual = SUT.giveMeOne(NoSetterSpecs.StringObject.class);

		then(actual).isNotNull();
	}
}
