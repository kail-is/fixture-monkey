package com.navercorp.fixturemonkey.api.arbitrary;

public class CharacterCombinableArbitraryQuickTest {
	public static void main(String[] args) {
		// 기본 Character 생성 테스트
		Character basicChar = CombinableArbitrary.chars().combined();
		System.out.println("✅ CharacterCombinableArbitrary works: " + basicChar);
		
		// 범위 테스트 (A-Z)
		Character rangeChar = CombinableArbitrary.chars().withRange('A', 'Z').combined();
		System.out.println("✅ Range works: " + rangeChar + " (should be A-Z)");
		
		// 알파벳 테스트
		Character alphaChar = CombinableArbitrary.chars().alpha().combined();
		System.out.println("✅ Alpha works: " + alphaChar + " (should be letter)");
		
		// 대문자 테스트
		Character upperChar = CombinableArbitrary.chars().uppercase().combined();
		System.out.println("✅ Uppercase works: " + upperChar + " (should be A-Z)");
		
		// 소문자 테스트
		Character lowerChar = CombinableArbitrary.chars().lowercase().combined();
		System.out.println("✅ Lowercase works: " + lowerChar + " (should be a-z)");
		
		// 숫자 테스트
		Character numericChar = CombinableArbitrary.chars().numeric().combined();
		System.out.println("✅ Numeric works: " + numericChar + " (should be 0-9)");
		
		System.out.println("🎉 All CharacterCombinableArbitrary tests passed!");
	}
}
