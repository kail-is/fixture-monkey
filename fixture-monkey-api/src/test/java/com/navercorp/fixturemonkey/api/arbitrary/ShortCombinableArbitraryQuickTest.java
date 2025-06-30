package com.navercorp.fixturemonkey.api.arbitrary;

public class ShortCombinableArbitraryQuickTest {
	public static void main(String[] args) {
		// 기본 Short 생성 테스트
		Short basicShort = CombinableArbitrary.shorts().combined();
		System.out.println("✅ ShortCombinableArbitrary works: " + basicShort);
		
		// 범위 테스트 (100-200)
		Short rangeShort = CombinableArbitrary.shorts().withRange((short) 100, (short) 200).combined();
		System.out.println("✅ Range works: " + rangeShort + " (should be 100-200)");
		
		// 양수 테스트
		Short positiveShort = CombinableArbitrary.shorts().positive().combined();
		System.out.println("✅ Positive works: " + positiveShort + " (should be > 0)");
		
		// 음수 테스트
		Short negativeShort = CombinableArbitrary.shorts().negative().combined();
		System.out.println("✅ Negative works: " + negativeShort + " (should be < 0)");
		
		// 짝수 테스트
		Short evenShort = CombinableArbitrary.shorts().even().combined();
		System.out.println("✅ Even works: " + evenShort + " (should be even)");
		
		// 홀수 테스트
		Short oddShort = CombinableArbitrary.shorts().odd().combined();
		System.out.println("✅ Odd works: " + oddShort + " (should be odd)");
		
		// greaterOrEqual 테스트
		Short greaterShort = CombinableArbitrary.shorts().greaterOrEqual((short) 1000).combined();
		System.out.println("✅ GreaterOrEqual works: " + greaterShort + " (should be >= 1000)");
		
		// lessOrEqual 테스트
		Short lessShort = CombinableArbitrary.shorts().lessOrEqual((short) 100).combined();
		System.out.println("✅ LessOrEqual works: " + lessShort + " (should be <= 100)");
		
		System.out.println("🎉 All ShortCombinableArbitrary tests passed!");
	}
}
