/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.fixturemonkey.api.arbitrary;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class LongCombinableArbitraryIntegrationTest {
	@Test
	void factoryMethodWorksCorrectly() {
		// when
		Long actual = CombinableArbitrary.longs().combined();

		// then
		then(actual).isInstanceOf(Long.class);
	}

	@Test
	void factoryMethodWithRangeWorksCorrectly() {
		// given
		long min = 1000L;
		long max = 2000L;

		// when
		Long actual = CombinableArbitrary.longs().withRange(min, max).combined();

		// then
		then(actual).isBetween(min, max);
	}

	@Test
	void factoryMethodWithPositiveWorksCorrectly() {
		// when
		Long actual = CombinableArbitrary.longs().positive().combined();

		// then
		then(actual).isPositive();
	}
}
