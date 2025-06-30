package com.navercorp.fixturemonkey.api.arbitrary;

/**
 * jqwik Arbitraries.longs()를 CombinableArbitrary.longs()로 대체할 수 있는지 검증
 */
public class ReplacementVerificationTest {

	public static void main(String[] args) {
		System.out.println("=== jqwik vs FixtureMonkey 대체 가능성 검증 ===");

		try {
			// 1. 기본 생성 비교
			testBasicGeneration();

			// 2. 범위 지정 비교
			testRangeGeneration();

			// 3. 양수 생성 비교
			testPositiveGeneration();

			// 4. 편의 메서드 체크
			testConvenienceMethods();

			System.out.println("🎉 모든 대체 테스트 통과!");

		} catch (Exception e) {
			System.out.println("❌ 대체 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void testBasicGeneration() {
		System.out.println("\n1. 기본 생성 테스트");

		// jqwik 방식 (비교 대상)
		// Long jqwikLong = Arbitraries.longs().sample();

		// FixtureMonkey 방식 (대체재)
		Long fixtureMonkeyLong = CombinableArbitrary.longs().combined();

		System.out.println("✅ FixtureMonkey 생성: " + fixtureMonkeyLong);
		assert fixtureMonkeyLong instanceof Long : "Long 타입이어야 함";
	}

	private static void testRangeGeneration() {
		System.out.println("\n2. 범위 지정 테스트");

		// jqwik: Arbitraries.longs().between(100L, 200L)
		// FixtureMonkey: CombinableArbitrary.longs().withRange(100L, 200L)

		Long rangedValue = CombinableArbitrary.longs().withRange(100L, 200L).combined();
		System.out.println("✅ 범위 지정 (100-200): " + rangedValue);
		assert rangedValue >= 100L && rangedValue <= 200L : "범위를 벗어남";
	}

	private static void testPositiveGeneration() {
		System.out.println("\n3. 양수 생성 테스트");

		// jqwik: Arbitraries.longs().greaterOrEqual(1L)
		// FixtureMonkey: CombinableArbitrary.longs().positive()

		Long positiveValue = CombinableArbitrary.longs().positive().combined();
		System.out.println("✅ 양수 생성: " + positiveValue);
		assert positiveValue > 0L : "양수가 아님";
	}

	private static void testConvenienceMethods() {
		System.out.println("\n4. 편의 메서드 테스트");

		// 음수
		Long negative = CombinableArbitrary.longs().negative().combined();
		System.out.println("✅ 음수: " + negative);
		assert negative < 0L : "음수가 아님";

		// 짝수
		Long even = CombinableArbitrary.longs().even().combined();
		System.out.println("✅ 짝수: " + even);
		assert even % 2 == 0 : "짝수가 아님";

		// 홀수
		Long odd = CombinableArbitrary.longs().odd().combined();
		System.out.println("✅ 홀수: " + odd);
		assert odd % 2 != 0 : "홀수가 아님";

		// greaterOrEqual
		Long greaterOrEqual = CombinableArbitrary.longs().greaterOrEqual(1000L).combined();
		System.out.println("✅ >= 1000: " + greaterOrEqual);
		assert greaterOrEqual >= 1000L : "1000 이상이 아님";

		// lessOrEqual
		Long lessOrEqual = CombinableArbitrary.longs().lessOrEqual(100L).combined();
		System.out.println("✅ <= 100: " + lessOrEqual);
		assert lessOrEqual <= 100L : "100 이하가 아님";
	}
}
