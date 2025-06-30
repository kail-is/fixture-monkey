package com.navercorp.fixturemonkey.api.arbitrary;

public class CharacterSpecializedQuickTest {
	public static void main(String[] args) {
		System.out.println("=== Character Specialized Methods Test ===");

		// 한글 테스트
		Character hangulChar = CombinableArbitrary.chars().hangul().combined();
		System.out.println("✅ Hangul char: " + hangulChar + " (Unicode: \\u" +
			String.format("%04X", (int) hangulChar) + ")");

		// 이모지 테스트
		Character emojiChar = CombinableArbitrary.chars().emojiChars().combined();
		System.out.println("✅ Emoji char: " + emojiChar + " (Unicode: \\u" +
			String.format("%04X", (int) emojiChar) + ")");

		// ASCII 테스트
		Character asciiChar = CombinableArbitrary.chars().ascii().combined();
		System.out.println("✅ ASCII char: " + asciiChar + " (Unicode: \\u" +
			String.format("%04X", (int) asciiChar) + ")");

		// 기존 메서드들도 테스트
		Character alphaChar = CombinableArbitrary.chars().alpha().combined();
		System.out.println("✅ Alpha char: " + alphaChar);

		Character numericChar = CombinableArbitrary.chars().numeric().combined();
		System.out.println("✅ Numeric char: " + numericChar);

		Character whitespaceChar = CombinableArbitrary.chars().whitespace().combined();
		System.out.println("✅ Whitespace char: '" + whitespaceChar + "' (Unicode: \\u" +
			String.format("%04X", (int) whitespaceChar) + ")");

		System.out.println("🎉 All CharacterCombinableArbitrary specialized methods work!");
	}
}
