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
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.navercorp.fixturemonkey.api.exception.FixedValueFilterMissException;

class LongCombinableArbitraryTest {
	@Test
	void combined() {
		// when
		Long actual = CombinableArbitrary.longs().combined();

		// then
		then(actual).isInstanceOf(Long.class);
	}

	@Test
	void withRange() {
		// given
		long min = 100L;
		long max = 200L;

		// when
		Long actual = CombinableArbitrary.longs().withRange(min, max).combined();

		// then
		then(actual).isBetween(min, max);
	}

	@Test
	void positive() {
		// when
		Long actual = CombinableArbitrary.longs().positive().combined();

		// then
		then(actual).isPositive();
	}

	@Test
	void negative() {
		// when
		Long actual = CombinableArbitrary.longs().negative().combined();

		// then
		then(actual).isNegative();
	}

	@Test
	void even() {
		// when
		boolean allEven = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.longs().even().combined())
			.allMatch(value -> value % 2 == 0);

		// then
		then(allEven).isTrue();
	}

	@Test
	void odd() {
		// when
		boolean allOdd = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.longs().odd().combined())
			.allMatch(value -> value % 2 != 0);

		// then
		then(allOdd).isTrue();
	}

	@Test
	void greaterOrEqual() {
		// given
		long min = 1000L;

		// when
		Long actual = CombinableArbitrary.longs().greaterOrEqual(min).combined();

		// then
		then(actual).isGreaterThanOrEqualTo(min);
	}

	@Test
	void lessOrEqual() {
		// given
		long max = 100L;

		// when
		Long actual = CombinableArbitrary.longs().lessOrEqual(max).combined();

		// then
		then(actual).isLessThanOrEqualTo(max);
	}

	@Test
	void lastMethodWinsWithPositiveAndRange() {
		// given
		long min = 100L;
		long max = 200L;

		// when - positive()를 무시하고 withRange()가 우선되어야 함
		Long actual = CombinableArbitrary.longs().positive().withRange(min, max).combined();

		// then
		then(actual).isBetween(min, max);
	}

	@Test
	void lastMethodWinsWithGreaterOrEqualAndLessOrEqual() {
		// given
		long max = 50L;

		// when - greaterOrEqual()을 무시하고 lessOrEqual()이 우선되어야 함
		Long actual = CombinableArbitrary.longs().greaterOrEqual(1000L).lessOrEqual(max).combined();

		// then
		then(actual).isLessThanOrEqualTo(max);
	}

	// ===== ArbitraryTest 스타일: 공통 기능 검증 =====

	@Test
	void longUnique() {
		// when & then - 고정값에 unique() 적용하면 예외 발생해야 함
		thenThrownBy(() -> CombinableArbitrary.longs()
			.filter(x -> x == 123L)  // 항상 같은 값만 생성
			.unique()
			.combined())
			.isExactlyInstanceOf(FixedValueFilterMissException.class);
	}

	@Test
	void longMapping() {
		// when
		String actual = CombinableArbitrary.longs()
			.withRange(100L, 200L)
			.map(x -> "prefix" + x)
			.combined();

		// then
		then(actual).startsWith("prefix");
		then(actual).contains("1"); // 100-200 범위이므로 1이 포함됨
	}

	@Test
	void longFiltering() {
		// when
		Long actual = CombinableArbitrary.longs()
			.withRange(0L, 1000L)
			.filter(x -> x > 500L)
			.combined();

		// then
		then(actual).isGreaterThan(500L);
		then(actual).isLessThanOrEqualTo(1000L);
	}

	@Test
	void longFilteringWithMultipleConditions() {
		// when
		Long actual = CombinableArbitrary.longs()
			.withRange(0L, 100L)
			.filter(x -> x % 10 == 0)  // 10의 배수만
			.combined();

		// then
		then(actual % 10).isEqualTo(0L);
		then(actual).isBetween(0L, 100L);
	}

	@Test
	void longInjectNull() {
		// when
		Long actual = CombinableArbitrary.longs()
			.withRange(1L, 100L)
			.injectNull(1.0)  // 100% null
			.combined();

		// then
		then(actual).isNull();
	}

	@Test
	void longInjectNullWithZeroProbability() {
		// when
		Long actual = CombinableArbitrary.longs()
			.withRange(1L, 100L)
			.injectNull(0.0)  // 0% null
			.combined();

		// then
		then(actual).isNotNull();
		then(actual).isBetween(1L, 100L);
	}

	@Test
	void longCombinationWithMultipleOperations() {
		// when - 여러 연산을 조합
		String actual = CombinableArbitrary.longs()
			.withRange(1L, 10L)
			.filter(x -> x > 5L)      // 6-10 범위
			.map(x -> "value:" + x)   // 문자열 변환
			.combined();

		// then
		then(actual).startsWith("value:");
		String numberPart = actual.substring(6);
		long number = Long.parseLong(numberPart);
		then(number).isBetween(6L, 10L);
	}

	@Test
	void longUniqueWithDifferentValues() {
		// when - 서로 다른 값들에는 unique()가 정상 작동
		Long actual = CombinableArbitrary.longs()
			.withRange(1L, 1000L)  // 충분히 넓은 범위
			.unique()
			.combined();

		// then
		then(actual).isBetween(1L, 1000L);
	}

	@Test
	void fixed() {
		// when
		boolean actual = CombinableArbitrary.longs().fixed();

		// then
		then(actual).isFalse();
	}
}
