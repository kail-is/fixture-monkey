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

			// Short 테스트 추가!
			Short shortResult = CombinableArbitrary.shorts().combined();
			System.out.println("✅ ShortCombinableArbitrary works: " + shortResult);

			Short shortRanged = CombinableArbitrary.shorts().withRange((short) 10, (short) 50).combined();
			System.out.println("✅ Short Range works: " + shortRanged + " (should be 10-50)");

			Short shortPositive = CombinableArbitrary.shorts().positive().combined();
			System.out.println("✅ Short Positive works: " + shortPositive + " (should be > 0)");

			// Byte 테스트 추가!
			Byte byteResult = CombinableArbitrary.bytes().combined();
			System.out.println("✅ ByteCombinableArbitrary works: " + byteResult);

			Byte byteRanged = CombinableArbitrary.bytes().withRange((byte) 1, (byte) 10).combined();
			System.out.println("✅ Byte Range works: " + byteRanged + " (should be 1-10)");

			Byte bytePositive = CombinableArbitrary.bytes().positive().combined();
			System.out.println("✅ Byte Positive works: " + bytePositive + " (should be > 0)");

			Byte byteAscii = CombinableArbitrary.bytes().ascii().combined();
			System.out.println("✅ Byte ASCII works: " + byteAscii + " (should be 0-127)");

			// Character 테스트 추가!
			Character charResult = CombinableArbitrary.chars().combined();
			System.out.println("✅ CharacterCombinableArbitrary works: " + charResult);

			Character charAlpha = CombinableArbitrary.chars().alpha().combined();
			System.out.println("✅ Character Alpha works: " + charAlpha + " (should be letter)");

			Character charUpper = CombinableArbitrary.chars().uppercase().combined();
			System.out.println("✅ Character Uppercase works: " + charUpper + " (should be A-Z)");

			Character charKorean = CombinableArbitrary.chars().korean().combined();
			System.out.println("✅ Character Korean works: " + charKorean + " (should be 가-힣)");

			Character charEmoji = CombinableArbitrary.chars().emoji().combined();
			System.out.println("✅ Character Emoji works: " + charEmoji + " (should be emoji)");

			Character charWhitespace = CombinableArbitrary.chars().whitespace().combined();
			System.out.println("✅ Character Whitespace works: '" + charWhitespace + "' (should be whitespace)");

			System.out.println("🎉 All tests passed!");
		} catch (Exception e) {
			System.out.println("❌ Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
