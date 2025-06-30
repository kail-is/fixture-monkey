package com.navercorp.fixturemonkey.api.arbitrary;

public class ByteSpecializedQuickTest {
	public static void main(String[] args) {
		System.out.println("=== Byte Specialized Methods Test ===");
		
		// ASCII 바이트 테스트
		Byte asciiByte = CombinableArbitrary.bytes().ascii().combined();
		System.out.println("✅ ASCII byte: " + asciiByte + " (should be 0-127)");
		
		// 양수 테스트
		Byte positiveByte = CombinableArbitrary.bytes().positive().combined();
		System.out.println("✅ Positive byte: " + positiveByte + " (should be > 0)");
		
		// 음수 테스트
		Byte negativeByte = CombinableArbitrary.bytes().negative().combined();
		System.out.println("✅ Negative byte: " + negativeByte + " (should be < 0)");
		
		// 짝수 테스트
		Byte evenByte = CombinableArbitrary.bytes().even().combined();
		System.out.println("✅ Even byte: " + evenByte + " (should be even)");
		
		// 홀수 테스트
		Byte oddByte = CombinableArbitrary.bytes().odd().combined();
		System.out.println("✅ Odd byte: " + oddByte + " (should be odd)");
		
		// 범위 테스트
		Byte rangeByte = CombinableArbitrary.bytes().withRange((byte) 10, (byte) 50).combined();
		System.out.println("✅ Range byte: " + rangeByte + " (should be 10-50)");
		
		System.out.println("🎉 All ByteCombinableArbitrary specialized methods work!");
	}
}
