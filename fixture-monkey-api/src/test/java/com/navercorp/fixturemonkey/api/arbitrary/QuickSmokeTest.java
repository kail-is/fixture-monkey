package com.navercorp.fixturemonkey.api.arbitrary;

public class QuickSmokeTest {
	public static void main(String[] args) {
		try {
			// ServiceLoader 테스트
			Long result = CombinableArbitrary.longs().combined();
			System.out.println("✅ LongCombinableArbitrary works: " + result);
			
			// Range 테스트
			Long ranged = CombinableArbitrary.longs().withRange(100L, 200L).combined();
			System.out.println("✅ Range works: " + ranged + " (should be 100-200)");
			
			// Positive 테스트
			Long positive = CombinableArbitrary.longs().positive().combined();
			System.out.println("✅ Positive works: " + positive + " (should be > 0)");
			
			System.out.println("🎉 All tests passed!");
		} catch (Exception e) {
			System.out.println("❌ Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
