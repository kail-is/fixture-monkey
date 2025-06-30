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

class ShortCombinableArbitraryTest {
	@Test
	void combined() {
		// when
		Short actual = CombinableArbitrary.shorts().combined();

		// then
		then(actual).isInstanceOf(Short.class);
	}

	@Test
	void withRange() {
		// given
		short min = 100;
		short max = 200;

		// when
		Short actual = CombinableArbitrary.shorts().withRange(min, max).combined();

		// then
		then(actual).isBetween(min, max);
	}

	@Test
	void positive() {
		// when
		boolean allPositive = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.shorts().positive().combined())
			.allMatch(s -> s > 0);

		// then
		then(allPositive).isTrue();
	}

	@Test
	void negative() {
		// when
		boolean allNegative = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.shorts().negative().combined())
			.allMatch(s -> s < 0);

		// then
		then(allNegative).isTrue();
	}

	@Test
	void even() {
		// when
		boolean allEven = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.shorts().even().combined())
			.allMatch(s -> s % 2 == 0);

		// then
		then(allEven).isTrue();
	}

	@Test
	void odd() {
		// when
		boolean allOdd = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.shorts().odd().combined())
			.allMatch(s -> s % 2 != 0);

		// then
		then(allOdd).isTrue();
	}

	@Test
	void greaterOrEqual() {
		// given
		short min = 1000;

		// when
		boolean allGreaterOrEqual = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.shorts().greaterOrEqual(min).combined())
			.allMatch(s -> s >= min);

		// then
		then(allGreaterOrEqual).isTrue();
	}

	@Test
	void lessOrEqual() {
		// given
		short max = 100;

		// when
		boolean allLessOrEqual = IntStream.range(0, 100)
			.mapToObj(i -> CombinableArbitrary.shorts().lessOrEqual(max).combined())
			.allMatch(s -> s <= max);

		// then
		then(allLessOrEqual).isTrue();
	}

	@Test
	void lastMethodWinsWithPositiveAndRange() {
		// given
		short min = -100;
		short max = -50;

		// when - positive()를 무시하고 withRange()가 우선되어야 함
		Short actual = CombinableArbitrary.shorts().positive().withRange(min, max).combined();

		// then
		then(actual).isBetween(min, max);
	}

	@Test
	void shortUnique() {
		// when & then - 고정값에 unique() 적용하면 예외 발생해야 함
		thenThrownBy(() -> CombinableArbitrary.shorts()
			.filter(x -> x == (short) 42)  // 항상 같은 값만 생성
			.unique()
			.combined())
			.isExactlyInstanceOf(FixedValueFilterMissException.class);
	}

	@Test
	void shortMapping() {
		// when
		String actual = CombinableArbitrary.shorts()
			.positive()
			.map(x -> "short:" + x)
			.combined();

		// then
		then(actual).startsWith("short:");
		String numberPart = actual.substring(6);
		short value = Short.parseShort(numberPart);
		then(value).isGreaterThan((short) 0);
	}

	@Test
	void shortFiltering() {
		// when
		Short actual = CombinableArbitrary.shorts()
			.withRange((short) 0, (short) 1000)
			.filter(s -> s > 500)
			.combined();

		// then
		then(actual).isGreaterThan((short) 500);
		then(actual).isLessThanOrEqualTo((short) 1000);
	}

	@Test
	void shortFilteringWithMultipleConditions() {
		// when
		Short actual = CombinableArbitrary.shorts()
			.positive()
			.filter(s -> s % 100 == 0)  // 100의 배수만
			.combined();

		// then
		then(actual).isGreaterThan((short) 0);
		then(actual % 100).isEqualTo(0);
	}

	@Test
	void shortInjectNull() {
		// when
		Short actual = CombinableArbitrary.shorts()
			.positive()
			.injectNull(1.0)  // 100% null
			.combined();

		// then
		then(actual).isNull();
	}

	@Test
	void shortInjectNullWithZeroProbability() {
		// when
		Short actual = CombinableArbitrary.shorts()
			.positive()
			.injectNull(0.0)  // 0% null
			.combined();

		// then
		then(actual).isNotNull();
		then(actual).isGreaterThan((short) 0);
	}

	@Test
	void shortCombinationWithMultipleOperations() {
		// when - 여러 연산을 조합
		String actual = CombinableArbitrary.shorts()
			.withRange((short) 1, (short) 100)
			.filter(s -> s % 10 == 0)  // 10의 배수
			.map(s -> "value:" + s)    // 문자열 변환
			.combined();

		// then
		then(actual).startsWith("value:");
		String numberPart = actual.substring(6);
		short value = Short.parseShort(numberPart);
		then(value % 10).isEqualTo(0);
		then(value).isBetween((short) 1, (short) 100);
	}

	@Test
	void shortUniqueWithDifferentValues() {
		// when - 서로 다른 값들에는 unique()가 정상 작동
		Short actual = CombinableArbitrary.shorts()
			.withRange((short) 1, (short) 1000)  // 충분히 넓은 범위
			.unique()
			.combined();

		// then
		then(actual).isBetween((short) 1, (short) 1000);
	}

	@Test
	void fixed() {
		// when
		boolean actual = CombinableArbitrary.shorts().fixed();

		// then
		then(actual).isFalse();
	}
}
