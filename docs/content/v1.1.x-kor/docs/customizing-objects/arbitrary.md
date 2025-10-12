---
title: "조건을 만족하는 랜덤 테스트 데이터 만들기"
weight: 43
menu:
docs:
parent: "customizing-objects"
identifier: "arbitrary"
---

## 이 문서에서 배우는 내용
- 랜덤하지만 규칙을 따르는 테스트 데이터 만들기
- 숫자 범위, 문자열 패턴, 값 목록 등의 제약조건 설정 방법
- 고정 값 대신 랜덤 값을 사용해야 하는 상황과 이유

## 랜덤 테스트 데이터 소개
테스트에서 항상 **고정된 값**만 사용하는 것은 충분하지 않을 수 있습니다. 다음과 같은 상황에서는 랜덤 값이 필요합니다:
- 단일 값이 아닌 유효한 입력값 범위로 테스트
- 테스트가 실행될 때마다 다른 테스트 데이터 사용
- 비즈니스 규칙을 따르는 현실적이지만 다양한 데이터

예를 들어, 다음과 같은 테스트 상황에서 유용합니다:
- 나이 검증: 18-65세 사이의 랜덤 나이 생성
- 사용자명 검증: 특정 패턴을 따르는 랜덤 문자열 생성
- 결제 처리: 특정 범위 내의 다양한 금액 생성

## Arbitrary 이해하기
Fixture Monkey에서는 규칙을 따르는 랜덤 값을 만들기 위해 `Arbitrary`를 사용합니다. `Arbitrary`는 **규칙이 있는 값 생성기**라고 생각하면 됩니다.

> **쉽게 말하면:** Arbitrary는 랜덤 값을 생성하는 기계와 같지만, 여러분이 정한 규칙을 따르는 값만 생성합니다.

## 단계별 랜덤 값 생성 가이드

### 1. 기본 사용법: 간단한 범위 설정

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 20-30세 사이의 회원 생성
Member member = fixtureMonkey.giveMeBuilder(Member.class)
    .set("age", Arbitraries.integers().between(20, 30))  // 20-30 사이 랜덤 나이
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 20-30세 사이의 회원 생성
val member = fixtureMonkey.giveMeBuilder<Member>()
    .setExp(Member::age, Arbitraries.integers().between(20, 30))  // 20-30 사이 랜덤 나이
    .sample()
{{< /tab >}}
{{< /tabpane>}}

### 2. 텍스트 다루기: 문자열 패턴

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 유효한 사용자명을 가진 사용자 생성 (소문자, 5-10자)
User user = fixtureMonkey.giveMeBuilder(User.class)
    .set("username", Arbitraries.strings()
        .withCharRange('a', 'z')  // 소문자만 사용
        .ofMinLength(5)           // 최소 5자
        .ofMaxLength(10))         // 최대 10자
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 유효한 사용자명을 가진 사용자 생성 (소문자, 5-10자)
val user = fixtureMonkey.giveMeBuilder<User>()
    .setExp(User::username, Arbitraries.strings()
        .withCharRange('a', 'z')  // 소문자만 사용
        .ofMinLength(5)           // 최소 5자
        .ofMaxLength(10))         // 최대 10자
    .sample()
{{< /tab >}}
{{< /tabpane>}}

### 3. 유효한 옵션에서 선택하기

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 유효한 상태를 가진 주문 생성
Order order = fixtureMonkey.giveMeBuilder(Order.class)
    .set("status", Arbitraries.of(  // 이 값들 중 하나를 랜덤하게 선택
        OrderStatus.PENDING,
        OrderStatus.PROCESSING,
        OrderStatus.SHIPPED))
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 유효한 상태를 가진 주문 생성
val order = fixtureMonkey.giveMeBuilder<Order>()
    .setExp(Order::status, Arbitraries.of(  // 이 값들 중 하나를 랜덤하게 선택
        OrderStatus.PENDING,
        OrderStatus.PROCESSING,
        OrderStatus.SHIPPED))
    .sample()
{{< /tab >}}
{{< /tabpane>}}

### 4. 여러 제약조건 결합하기

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 다양한 제약조건을 가진 상품 생성
Product product = fixtureMonkey.giveMeBuilder(Product.class)
    .set("id", Arbitraries.longs().greaterOrEqual(1000))  // ID는 최소 1000 이상
    .set("name", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(10))  // 이름은 최대 10자
    .set("price", Arbitraries.bigDecimals()
        .between(BigDecimal.valueOf(10.0), BigDecimal.valueOf(1000.0)))  // 가격은 10-1000 사이
    .set("category", Arbitraries.of("전자제품", "의류", "도서"))  // 이 카테고리 중 하나
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 다양한 제약조건을 가진 상품 생성
val product = fixtureMonkey.giveMeBuilder<Product>()
    .setExp(Product::id, Arbitraries.longs().greaterOrEqual(1000))  // ID는 최소 1000 이상
    .setExp(Product::name, Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(10))  // 이름은 최대 10자
    .setExp(Product::price, Arbitraries.bigDecimals()
        .between(BigDecimal.valueOf(10.0), BigDecimal.valueOf(1000.0)))  // 가격은 10-1000 사이
    .setExp(Product::category, Arbitraries.of("전자제품", "의류", "도서"))  // 이 카테고리 중 하나
    .sample()
{{< /tab >}}
{{< /tabpane>}}

## 실제 사례: 나이 검증 테스트

성인 회원(18세 이상)만 가입할 수 있고 노인(65세 이상)은 할인을 받는 서비스를 테스트한다고 가정해 봅시다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
@Test
void 성인_회원_가입_테스트() {
    // 50명의 랜덤 성인 회원으로 테스트
    for (int i = 0; i < 50; i++) {
        Member member = fixtureMonkey.giveMeBuilder(Member.class)
            .set("age", Arbitraries.integers().between(18, 100))  // 성인만
            .sample();
            
        boolean isSenior = member.getAge() >= 65;
        
        // 다양한 나이로 가입 로직 테스트
        MembershipResponse response = membershipService.register(member);
        
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.hasDiscount()).isEqualTo(isSenior);  // 노인은 할인 받음
    }
}
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
@Test
fun 성인_회원_가입_테스트() {
    // 50명의 랜덤 성인 회원으로 테스트
    repeat(50) {
        val member = fixtureMonkey.giveMeBuilder<Member>()
            .setExp(Member::age, Arbitraries.integers().between(18, 100))  // 성인만
            .sample()
            
        val isSenior = member.age >= 65
        
        // 다양한 나이로 가입 로직 테스트
        val response = membershipService.register(member)
        
        assertThat(response.isSuccess).isTrue()
        assertThat(response.hasDiscount).isEqualTo(isSenior)  // 노인은 할인 받음
    }
}
{{< /tab >}}
{{< /tabpane>}}

## 자주 사용하는 Arbitrary 메서드

| 메서드 | 용도 | 예시 |
|--------|------|------|
| `between(min, max)` | 범위 내 값 | `Arbitraries.integers().between(1, 100)` |
| `greaterOrEqual(min)` | 최소값 이상 | `Arbitraries.longs().greaterOrEqual(1000)` |
| `lessOrEqual(max)` | 최대값 이하 | `Arbitraries.doubles().lessOrEqual(99.9)` |
| `ofMaxLength(max)` | 최대 길이 문자열 | `Arbitraries.strings().ofMaxLength(10)` |
| `withCharRange(from, to)` | 문자 범위 설정 | `Arbitraries.strings().withCharRange('a', 'z')` |
| `of(values...)` | 옵션 중 선택 | `Arbitraries.of("빨강", "초록", "파랑")` |

## 자주 묻는 질문

### 고정 값 대신 Arbitrary를 사용해야 하는 경우는 언제인가요?

다음과 같은 경우에 Arbitrary를 사용하세요:
- 단일 값이 아닌 다양한 입력으로 테스트하고 싶을 때
- 정확한 값보다는 규칙을 따르는 값이 필요할 때
- 자동으로 엣지 케이스를 발견하고 싶을 때
- 다양한 유효한 입력으로 테스트해야 할 때

### 랜덤 값을 사용하면 테스트가 불안정하지 않을까요?

값은 랜덤이지만 여러분이 정의한 규칙을 따르기 때문에 다음과 같은 이점이 있습니다:
- 특정 값에서만 나타나는 버그 발견 가능
- 유효한 입력 전체 범위에서 코드가 작동하는지 확인
- 예상치 못한 엣지 케이스 발견

테스트가 실패한 경우 Fixture Monkey의 `@Seed` 어노테이션을 사용하여 재현 가능하게 만들 수 있습니다:

```java
import com.navercorp.fixturemonkey.junit.jupiter.annotation.Seed;
import com.navercorp.fixturemonkey.junit.jupiter.extension.FixtureMonkeySeedExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FixtureMonkeySeedExtension.class)
class MembershipTest {
    @Test
    @Seed(123L)  // 예측 가능한 랜덤 값을 위한 특정 시드 사용
    void 성인_회원만_가능한_테스트() {
        Member member = fixtureMonkey.giveMeBuilder(Member.class)
            .set("age", Arbitraries.integers().between(18, 100))
            .sample();
            
        // 테스트 로직
        assertThat(membershipService.isEligible(member)).isTrue();
    }
}
```

`@Seed` 어노테이션을 사용하면 Fixture Monkey는 지정된 시드 값을 사용하여 테스트가 실행될 때마다 동일한 "랜덤" 값을 생성합니다. 이렇게 하면 랜덤 데이터를 사용하는 테스트를 완전히 재현 가능하게 만들 수 있습니다.

`FixtureMonkeySeedExtension`의 가장 유용한 기능 중 하나는 테스트가 실패할 때 자동으로 시드 값을 로그에 출력한다는 것입니다:

```
Test Method [MembershipTest#성인_회원만_가능한_테스트] failed with seed: 42
```

이렇게 출력된 시드 값을 `@Seed` 어노테이션에 추가하면 실패한 테스트 상황을 일관되게 재현할 수 있습니다.

### setPostCondition()과 어떻게 다른가요?

- `setPostCondition()`은 임의의 값을 생성한 후 조건에 맞는지 확인합니다
- `Arbitrary`는 조건을 만족하는 값을 직접 생성합니다

생성된 값에 대한 더 많은 제어가 필요하거나, `setPostCondition()`이 많은 유효하지 않은 값을 폐기해야 해서 너무 느릴 때는 `Arbitrary`를 사용하세요.

## 고급 Arbitrary 타입 (실험적 기능)

버전 1.1.12부터 Fixture Monkey는 값 생성을 더 세밀하게 제어할 수 있는 전용 arbitrary 타입을 제공합니다.

### CombinableArbitrary.integers()

`CombinableArbitrary.integers()` 메서드는 정수 생성을 위한 전용 메서드들을 제공하는 `IntegerCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 다양한 제약조건을 가진 정수 생성
Member member = fixtureMonkey.giveMeBuilder(Member.class)
    .set("age", CombinableArbitrary.integers()
        .withRange(18, 65)     // 18-65세 사이
        .positive())           // 양수만
    .set("score", CombinableArbitrary.integers()
        .even()                // 짝수만
        .withRange(0, 100))    // 0-100 사이
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 다양한 제약조건을 가진 정수 생성
val member = fixtureMonkey.giveMeBuilder<Member>()
    .setExp(Member::age, CombinableArbitrary.integers()
        .withRange(18, 65)     // 18-65세 사이
        .positive())           // 양수만
    .setExp(Member::score, CombinableArbitrary.integers()
        .even()                // 짝수만
        .withRange(0, 100))    // 0-100 사이
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### IntegerCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 정수 생성 (양 끝값 포함) | `integers().withRange(1, 100)` |
| `positive()` | 양수만 생성 (≥ 1) | `integers().positive()` |
| `negative()` | 음수만 생성 (≤ -1) | `integers().negative()` |
| `even()` | 짝수만 생성 | `integers().even()` |
| `odd()` | 홀수만 생성 | `integers().odd()` |

**중요 참고사항:** 여러 제약조건 메서드를 연결할 때 **마지막 메서드가 우선**됩니다. 예를 들어:
```java
// positive() 호출을 무시하고 음수를 생성합니다
CombinableArbitrary.integers().positive().negative()

// positive() 호출을 무시하고 10-50 범위의 정수를 생성합니다
CombinableArbitrary.integers().positive().withRange(10, 50)
```

### CombinableArbitrary.bytes()

`CombinableArbitrary.bytes()` 메서드는 바이트 값을 생성하기 위한 전용 메서드를 제공하는 `ByteCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 바이트 제약조건이 있는 패킷 생성
Packet packet = fixtureMonkey.giveMeBuilder(Packet.class)
    .set("flag", CombinableArbitrary.bytes()
        .ascii()               // ASCII 범위만
        .even())               // 짝수만
    .set("signal", CombinableArbitrary.bytes()
        .withRange((byte)10, (byte)42)  // 10-42 사이
        .positive())           // 양수만
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 바이트 제약조건이 있는 패킷 생성
val packet = fixtureMonkey.giveMeBuilder<Packet>()
    .setExp(Packet::flag, CombinableArbitrary.bytes()
        .ascii()               // ASCII 범위만
        .even())               // 짝수만
    .setExp(Packet::signal, CombinableArbitrary.bytes()
        .withRange(10.toByte(), 42.toByte())  // 10-42 사이
        .positive())           // 양수만
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### ByteCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 바이트 생성 (양 끝값 포함) | `bytes().withRange((byte)1, (byte)100)` |
| `positive()` | 양수 바이트만 생성 (≥ 1) | `bytes().positive()` |
| `negative()` | 음수 바이트만 생성 (≤ -1) | `bytes().negative()` |
| `even()` | 짝수 바이트만 생성 | `bytes().even()` |
| `odd()` | 홀수 바이트만 생성 | `bytes().odd()` |
| `ascii()` | ASCII 범위 바이트 생성 (0-127) | `bytes().ascii()` |

**중요 참고사항:**
- 제약 메서드는 **마지막에 호출된 메서드가 우선**합니다.
  ```java
  // positive()는 무시되고 음수 바이트가 생성됩니다
  CombinableArbitrary.bytes().positive().negative()
  ```
- `ascii()`와 다른 메서드를 조합하면 결과가 더 좁아집니다.
  ```java
  // 짝수 ASCII 바이트만 생성합니다 (0, 2, 4, ..., 126)
  CombinableArbitrary.bytes().ascii().even()
  ```

### CombinableArbitrary.longs()

`CombinableArbitrary.longs()` 메서드는 64비트 정수를 생성하는 `LongCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 비즈니스 규칙을 반영한 long 값 생성
Payment payment = fixtureMonkey.giveMeBuilder(Payment.class)
    .set("amount", CombinableArbitrary.longs()
        .withRange(10_000L, 1_000_000L)  // 10,000 ~ 1,000,000
        .positive())                     // 양수만
    .set("transactionId", CombinableArbitrary.longs()
        .multipleOf(100)                 // 100 단위 배수
        .nonZero())                      // 0 제외
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 비즈니스 규칙을 반영한 long 값 생성
val payment = fixtureMonkey.giveMeBuilder<Payment>()
    .setExp(Payment::amount, CombinableArbitrary.longs()
        .withRange(10_000L, 1_000_000L)
        .positive())
    .setExp(Payment::transactionId, CombinableArbitrary.longs()
        .multipleOf(100)
        .nonZero())
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### LongCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 long 생성 (양 끝값 포함) | `longs().withRange(0L, 1_000L)` |
| `positive()` | 양수 long만 생성 (≥ 1) | `longs().positive()` |
| `negative()` | 음수 long만 생성 (≤ -1) | `longs().negative()` |
| `even()` | 짝수 long만 생성 | `longs().even()` |
| `odd()` | 홀수 long만 생성 | `longs().odd()` |
| `nonZero()` | 0을 제외하고 생성 | `longs().nonZero()` |
| `multipleOf(divisor)` | 특정 배수만 생성 | `longs().multipleOf(500)` |

**중요 참고사항:**
- 제약 메서드는 **마지막에 호출된 메서드**가 우선합니다.
- `multipleOf(0)`은 `ArithmeticException`을 발생시키므로 0이 아닌 값을 사용하세요.

### CombinableArbitrary.shorts()

`CombinableArbitrary.shorts()` 메서드는 16비트 정수를 생성하는 `ShortCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// short 값으로 구성된 일정 정보 생성
Schedule schedule = fixtureMonkey.giveMeBuilder(Schedule.class)
    .set("month", CombinableArbitrary.shorts().month())
    .set("day", CombinableArbitrary.shorts()
        .day()
        .filter(d -> d <= 28))  // 28일 이하로 제한
    .set("score", CombinableArbitrary.shorts().score())
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// short 값으로 구성된 일정 정보 생성
val schedule = fixtureMonkey.giveMeBuilder<Schedule>()
    .setExp(Schedule::month, CombinableArbitrary.shorts().month())
    .setExp(Schedule::day, CombinableArbitrary.shorts()
        .day()
        .filter { it <= 28 })
    .setExp(Schedule::score, CombinableArbitrary.shorts().score())
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### ShortCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 short 생성 (양 끝값 포함) | `shorts().withRange((short)1, (short)100)` |
| `positive()` | 양수 short만 생성 (≥ 1) | `shorts().positive()` |
| `negative()` | 음수 short만 생성 (≤ -1) | `shorts().negative()` |
| `even()` | 짝수 short만 생성 | `shorts().even()` |
| `odd()` | 홀수 short만 생성 | `shorts().odd()` |
| `nonZero()` | 0을 제외하고 생성 | `shorts().nonZero()` |
| `multipleOf(value)` | 특정 값의 배수만 생성 | `shorts().multipleOf((short)5)` |
| `percentage()` | 0-100 범위 값 생성 | `shorts().percentage()` |
| `score()` | 점수(0-100) 용도로 생성 | `shorts().score()` |
| `year()` | 연도(1900-2100) 생성 | `shorts().year()` |
| `month()` | 월(1-12) 생성 | `shorts().month()` |
| `day()` | 일(1-31) 생성 | `shorts().day()` |
| `hour()` | 시(0-23) 생성 | `shorts().hour()` |
| `minute()` | 분(0-59) 생성 | `shorts().minute()` |

**중요 참고사항:**
- 역시 **마지막으로 호출한 메서드가 우선**합니다.
- `year()`, `month()` 등을 사용한 뒤 추가 필터로 도메인 규칙(예: 윤년)을 세밀하게 제어할 수 있습니다.

### CombinableArbitrary.bigIntegers()

`CombinableArbitrary.bigIntegers()` 메서드는 임의정밀도 정수를 생성하는 `BigIntegerCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 큰 정수 범위를 다루는 멤버십 데이터 생성
LoyaltyAccount account = fixtureMonkey.giveMeBuilder(LoyaltyAccount.class)
    .set("balance", CombinableArbitrary.bigIntegers()
        .withRange(new BigInteger("0"), new BigInteger("100000000000"))
        .even())
    .set("primeId", CombinableArbitrary.bigIntegers()
        .withRange(new BigInteger("1000"), new BigInteger("10000"))
        .prime())
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 큰 정수 범위를 다루는 멤버십 데이터 생성
val account = fixtureMonkey.giveMeBuilder<LoyaltyAccount>()
    .setExp(LoyaltyAccount::balance, CombinableArbitrary.bigIntegers()
        .withRange(BigInteger("0"), BigInteger("100000000000"))
        .even())
    .setExp(LoyaltyAccount::primeId, CombinableArbitrary.bigIntegers()
        .withRange(BigInteger("1000"), BigInteger("10000"))
        .prime())
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### BigIntegerCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 임의정밀 정수 생성 | `bigIntegers().withRange(BigInteger.ZERO, BigInteger("1000"))` |
| `positive()` | 양수만 생성 (≥ 1) | `bigIntegers().positive()` |
| `negative()` | 음수만 생성 (≤ -1) | `bigIntegers().negative()` |
| `nonZero()` | 0 제외 | `bigIntegers().nonZero()` |
| `percentage()` | 0-100 범위 값 생성 | `bigIntegers().percentage()` |
| `score(min, max)` | 사용자 지정 점수 범위 생성 | `bigIntegers().score(BigInteger.ZERO, BigInteger("1000"))` |
| `even()` | 짝수만 생성 | `bigIntegers().even()` |
| `odd()` | 홀수만 생성 | `bigIntegers().odd()` |
| `multipleOf(divisor)` | 특정 배수 생성 | `bigIntegers().multipleOf(BigInteger("1024"))` |
| `prime()` | 소수만 생성 | `bigIntegers().prime()` |

**중요 참고사항:**
- 소수 생성(`prime()`)은 큰 범위에서 계산 비용이 크므로 적절한 범위를 함께 지정하세요.
- 제약 메서드는 여전히 **마지막 호출 우선** 규칙을 따릅니다.

### CombinableArbitrary.bigDecimals()

`CombinableArbitrary.bigDecimals()` 메서드는 임의정밀도 소수를 생성하는 `BigDecimalCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 금액/비율과 같은 정밀도가 필요한 값 생성
Invoice invoice = fixtureMonkey.giveMeBuilder(Invoice.class)
    .set("subtotal", CombinableArbitrary.bigDecimals()
        .withRange(new BigDecimal("10.00"), new BigDecimal("9999.99"))
        .withScale(2)
        .normalized())
    .set("discountRate", CombinableArbitrary.bigDecimals()
        .percentage()
        .withPrecision(3))
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 금액/비율과 같은 정밀도가 필요한 값 생성
val invoice = fixtureMonkey.giveMeBuilder<Invoice>()
    .setExp(Invoice::subtotal, CombinableArbitrary.bigDecimals()
        .withRange(BigDecimal("10.00"), BigDecimal("9999.99"))
        .withScale(2)
        .normalized())
    .setExp(Invoice::discountRate, CombinableArbitrary.bigDecimals()
        .percentage()
        .withPrecision(3))
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### BigDecimalCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 임의정밀 소수 생성 | `bigDecimals().withRange(BigDecimal("0.0"), BigDecimal("100.0"))` |
| `positive()` | 양수만 생성 (> 0) | `bigDecimals().positive()` |
| `negative()` | 음수만 생성 (< 0) | `bigDecimals().negative()` |
| `nonZero()` | 0 제외 | `bigDecimals().nonZero()` |
| `percentage()` | 0.0-100.0 범위 생성 | `bigDecimals().percentage()` |
| `score(min, max)` | 임의의 점수 범위 생성 | `bigDecimals().score(BigDecimal("1.0"), BigDecimal("5.0"))` |
| `withPrecision(precision)` | 유효 숫자 자릿수 제한 | `bigDecimals().withPrecision(4)` |
| `withScale(scale)` | 소수점 이하 자리수 고정 | `bigDecimals().withScale(2)` |
| `normalized()` | 불필요한 0을 제거한 정규화 값 생성 | `bigDecimals().normalized()` |

**중요 참고사항:**
- `withPrecision`과 `withScale`을 조합하면 통화 포맷과 같은 형식을 손쉽게 강제할 수 있습니다.
- 제약이 충돌하면 항상 **마지막 호출이 우선**합니다.

### CombinableArbitrary.doubles()

`CombinableArbitrary.doubles()` 메서드는 배정도 부동소수를 생성하는 `DoubleCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 부동소수 기반 센서 측정 값 생성
Measurement measurement = fixtureMonkey.giveMeBuilder(Measurement.class)
    .set("value", CombinableArbitrary.doubles()
        .withRange(-100.0, 100.0)
        .finite())
    .set("confidence", CombinableArbitrary.doubles()
        .normalized()
        .withPrecision(3))
    .set("special", CombinableArbitrary.doubles()
        .withRange(-1.0, 1.0)
        .withStandardSpecialValues())
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 부동소수 기반 센서 측정 값 생성
val measurement = fixtureMonkey.giveMeBuilder<Measurement>()
    .setExp(Measurement::value, CombinableArbitrary.doubles()
        .withRange(-100.0, 100.0)
        .finite())
    .setExp(Measurement::confidence, CombinableArbitrary.doubles()
        .normalized()
        .withPrecision(3))
    .setExp(Measurement::special, CombinableArbitrary.doubles()
        .withRange(-1.0, 1.0)
        .withStandardSpecialValues())
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### DoubleCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 double 생성 | `doubles().withRange(-10.0, 10.0)` |
| `positive()` | 양수만 생성 (> 0) | `doubles().positive()` |
| `negative()` | 음수만 생성 (< 0) | `doubles().negative()` |
| `nonZero()` | 0 제외 | `doubles().nonZero()` |
| `withPrecision(scale)` | 소수점 자리 제한 | `doubles().withPrecision(2)` |
| `finite()` | NaN/무한대 제외 | `doubles().finite()` |
| `infinite()` | ±무한대 생성 | `doubles().infinite()` |
| `normalized()` | 0.0-1.0 범위 생성 | `doubles().normalized()` |
| `nan()` | NaN 생성 | `doubles().nan()` |
| `percentage()` | 0-100 범위 생성 | `doubles().percentage()` |
| `score()` | 점수(0-100) 생성 | `doubles().score()` |
| `withSpecialValue(value)` | 사용자 정의 특수값 삽입 | `doubles().withSpecialValue(Double.MIN_NORMAL)` |
| `withStandardSpecialValues()` | NaN/±∞ 등 표준 특수값 삽입 | `doubles().withStandardSpecialValues()` |

**중요 참고사항:**
- `nan()`, `infinite()`, `withStandardSpecialValues()`는 이전 범위 설정을 무시할 수 있으므로 마지막에 호출하는 것이 안전합니다.
- 특수값을 배제하려면 먼저 `finite()`를 호출하세요.

### CombinableArbitrary.floats()

`CombinableArbitrary.floats()` 메서드는 단정도 부동소수를 생성하는 `FloatCombinableArbitrary`를 제공합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// float 기반 센서 데이터를 생성
SensorReading reading = fixtureMonkey.giveMeBuilder(SensorReading.class)
    .set("temperature", CombinableArbitrary.floats()
        .withRange(-40f, 50f)
        .finite())
    .set("probability", CombinableArbitrary.floats()
        .normalized()
        .withPrecision(2))
    .set("fallback", CombinableArbitrary.floats()
        .withRange(-1f, 1f)
        .withStandardSpecialValues())
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// float 기반 센서 데이터를 생성
val reading = fixtureMonkey.giveMeBuilder<SensorReading>()
    .setExp(SensorReading::temperature, CombinableArbitrary.floats()
        .withRange(-40f, 50f)
        .finite())
    .setExp(SensorReading::probability, CombinableArbitrary.floats()
        .normalized()
        .withPrecision(2))
    .setExp(SensorReading::fallback, CombinableArbitrary.floats()
        .withRange(-1f, 1f)
        .withStandardSpecialValues())
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### FloatCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 float 생성 | `floats().withRange(-5f, 5f)` |
| `positive()` | 양수만 생성 (> 0) | `floats().positive()` |
| `negative()` | 음수만 생성 (< 0) | `floats().negative()` |
| `nonZero()` | 0 제외 | `floats().nonZero()` |
| `withPrecision(scale)` | 소수점 자리 제한 | `floats().withPrecision(3)` |
| `finite()` | NaN/무한대 제외 | `floats().finite()` |
| `infinite()` | ±무한대 생성 | `floats().infinite()` |
| `normalized()` | 0.0-1.0 범위 생성 | `floats().normalized()` |
| `nan()` | NaN 생성 | `floats().nan()` |
| `percentage()` | 0-100 범위 생성 | `floats().percentage()` |
| `score()` | 점수(0-100) 생성 | `floats().score()` |
| `withSpecialValue(value)` | 사용자 정의 특수값 삽입 | `floats().withSpecialValue(Float.MIN_VALUE)` |
| `withStandardSpecialValues()` | 표준 특수값 삽입 | `floats().withStandardSpecialValues()` |

**중요 참고사항:**
- NaN/무한대를 허용하지 않아야 한다면 반드시 `finite()`를 적용하세요.
- 특수값 삽입 메서드는 마지막에 호출해야 다른 제약에 의해 제거되지 않습니다.

### CombinableArbitrary.strings()

`CombinableArbitrary.strings()` 메서드는 문자열 생성을 위한 전용 메서드들을 제공하는 `StringCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 다양한 문자 집합과 제약조건을 가진 문자열 생성
User user = fixtureMonkey.giveMeBuilder(User.class)
    .set("username", CombinableArbitrary.strings()
        .alphabetic()          // 알파벳 문자만
        .withLength(5, 15))    // 길이 5-15
    .set("password", CombinableArbitrary.strings()
        .ascii()               // ASCII 문자
        .withMinLength(8))     // 최소 8자
    .set("phoneNumber", CombinableArbitrary.strings()
        .numeric()             // 숫자 문자만
        .withLength(10, 11))   // 10자리 또는 11자리
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 다양한 문자 집합과 제약조건을 가진 문자열 생성
val user = fixtureMonkey.giveMeBuilder<User>()
    .setExp(User::username, CombinableArbitrary.strings()
        .alphabetic()          // 알파벳 문자만
        .withLength(5, 15))    // 길이 5-15
    .setExp(User::password, CombinableArbitrary.strings()
        .ascii()               // ASCII 문자
        .withMinLength(8))     // 최소 8자
    .setExp(User::phoneNumber, CombinableArbitrary.strings()
        .numeric()             // 숫자 문자만
        .withLength(10, 11))   // 10자리 또는 11자리
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### StringCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withLength(min, max)` | min과 max 사이 길이의 문자열 생성 | `strings().withLength(5, 10)` |
| `withMinLength(min)` | 최소 길이를 가진 문자열 생성 | `strings().withMinLength(3)` |
| `withMaxLength(max)` | 최대 길이를 가진 문자열 생성 | `strings().withMaxLength(20)` |
| `alphabetic()` | 알파벳 문자만 포함하는 문자열 생성 (a-z, A-Z) | `strings().alphabetic()` |
| `ascii()` | ASCII 문자만 포함하는 문자열 생성 | `strings().ascii()` |
| `numeric()` | 숫자 문자만 포함하는 문자열 생성 (0-9) | `strings().numeric()` |
| `korean()` | 한글 문자만 포함하는 문자열 생성 (가-힣) | `strings().korean()` |
| `filterCharacter(predicate)` | 문자열의 개별 문자를 필터링 | `strings().filterCharacter(c -> c != 'x')` |

**중요 참고사항:** 
1. **문자 집합 메서드들은 서로 충돌합니다.** 여러 문자 집합 메서드를 연결할 때 **마지막 메서드가 우선**됩니다:
   ```java
   // alphabetic()을 무시하고 한글 문자만 생성합니다
   CombinableArbitrary.strings().alphabetic().korean()
   ```

2. **문자 집합 메서드는 다른 설정 메서드를 무시합니다.** 문자 집합 메서드가 호출되면 이전 설정을 무시하는 새 인스턴스가 생성됩니다:
   ```java
   // alphabetic()이 호출되면 withLength(5, 10)이 무시됩니다
   CombinableArbitrary.strings().withLength(5, 10).alphabetic()
   ```

### CombinableArbitrary.chars()

`CombinableArbitrary.chars()` 메서드는 단일 문자를 생성하기 위한 전용 메서드를 제공하는 `CharacterCombinableArbitrary`를 반환합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 다양한 문자 제약조건을 가진 회원 등급 생성
Member member = fixtureMonkey.giveMeBuilder(Member.class)
    .set("grade", CombinableArbitrary.chars()
        .uppercase()           // 대문자만
        .withRange('A', 'D'))  // A-D 사이
    .set("locale", CombinableArbitrary.chars()
        .korean()              // 한글만
        .filter(c -> c >= '나')) // 추가 필터
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 다양한 문자 제약조건을 가진 회원 등급 생성
val member = fixtureMonkey.giveMeBuilder<Member>()
    .setExp(Member::grade, CombinableArbitrary.chars()
        .uppercase()           // 대문자만
        .withRange('A', 'D'))  // A-D 사이
    .setExp(Member::locale, CombinableArbitrary.chars()
        .korean()              // 한글만
        .filter { it >= '나' }) // 추가 필터
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### CharacterCombinableArbitrary 메서드

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `withRange(min, max)` | min과 max 사이의 문자 생성 (양 끝값 포함) | `chars().withRange('A', 'Z')` |
| `alphabetic()` | 알파벳 문자만 생성 (a-z, A-Z) | `chars().alphabetic()` |
| `numeric()` | 숫자 문자만 생성 (0-9) | `chars().numeric()` |
| `alphaNumeric()` | 알파벳 + 숫자 조합 생성 | `chars().alphaNumeric()` |
| `ascii()` | ASCII 인쇄 가능 문자만 생성 (0x20-0x7E) | `chars().ascii()` |
| `uppercase()` | 대문자만 생성 (A-Z) | `chars().uppercase()` |
| `lowercase()` | 소문자만 생성 (a-z) | `chars().lowercase()` |
| `korean()` | 한글 완성형 문자 생성 (가-힣) | `chars().korean()` |
| `emoji()` | 대표적인 이모지 코드 포인트 생성 | `chars().emoji()` |
| `whitespace()` | 공백 문자만 생성 (스페이스, 탭 등) | `chars().whitespace()` |

**중요 참고사항:**
- 문자 집합 관련 메서드(`alphabetic`, `numeric`, `uppercase` 등)를 여러 번 연결하면 **마지막에 호출된 메서드**가 우선합니다.
  ```java
  // alphabetic()을 무시하고 숫자 문자만 생성합니다
  CombinableArbitrary.chars().alphabetic().numeric()
  ```
- 문자 집합 메서드 이후에 범위나 필터를 추가하면 해당 조건이 위에 덮어씌워집니다.
  ```java
  // uppercase() 결과에서 다시 범위를 재정의합니다
  CombinableArbitrary.chars().uppercase().withRange('A', 'C')
  ```

### 고급 필터링

`integers()`, `bytes()`, `shorts()`, `longs()`, `bigIntegers()`, `bigDecimals()`, `doubles()`, `floats()` 등 숫자용 CombinableArbitrary와 `strings()`, `chars()` 모두 고급 필터링을 지원합니다:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// 사용자 정의 조건으로 정수 필터링
Integer score = CombinableArbitrary.integers()
    .withRange(0, 100)
    .filter(n -> n % 5 == 0)  // 5의 배수만
    .combined();

// 사용자 정의 문자 조건으로 문자열 필터링
String code = CombinableArbitrary.strings()
    .withLength(6, 8)
    .filterCharacter(c -> Character.isUpperCase(c) || Character.isDigit(c))  // 대문자와 숫자만
    .combined();

// 사용자 정의 조건으로 바이트 필터링
Byte checksum = CombinableArbitrary.bytes()
    .ascii()
    .filter(b -> (b & 0x0F) == 0)  // 하위 4비트가 0인 값만
    .combined();

// 사용자 정의 조건으로 문자 필터링
Character emoji = CombinableArbitrary.chars()
    .emoji()
    .filter(c -> Character.getType(c) == Character.OTHER_SYMBOL || c == '❤')
    .combined();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// 사용자 정의 조건으로 정수 필터링
val score = CombinableArbitrary.integers()
    .withRange(0, 100)
    .filter { it % 5 == 0 }  // 5의 배수만
    .combined()

// 사용자 정의 문자 조건으로 문자열 필터링
val code = CombinableArbitrary.strings()
    .withLength(6, 8)
    .filterCharacter { it.isUpperCase() || it.isDigit() }  // 대문자와 숫자만
    .combined()

// 사용자 정의 조건으로 바이트 필터링
val checksum = CombinableArbitrary.bytes()
    .ascii()
    .filter { (it.toInt() and 0x0F) == 0 }  // 하위 4비트가 0인 값만
    .combined()

// 사용자 정의 조건으로 문자 필터링
val emoji = CombinableArbitrary.chars()
    .emoji()
    .filter { it == '❤' || Character.getType(it) == Character.OTHER_SYMBOL }
    .combined()
{{< /tab >}}
{{< /tabpane>}}

### 실제 사례: 사용자 등록 검증

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
@Test
void 다양한_입력으로_사용자_등록_검증() {
    for (int i = 0; i < 100; i++) {
        User user = fixtureMonkey.giveMeBuilder(User.class)
            .set("username", CombinableArbitrary.strings()
                .alphabetic()
                .withLength(3, 20))           // 유효한 사용자명: 3-20자 알파벳
            .set("email", CombinableArbitrary.strings()
                .ascii()
                .withLength(5, 50)
                .filter(s -> s.contains("@"))) // 간단한 이메일 검증
            .set("age", CombinableArbitrary.integers()
                .withRange(13, 120))          // 유효한 나이 범위
            .set("score", CombinableArbitrary.integers()
                .withRange(0, 100)
                .filter(n -> n % 10 == 0))    // 10의 배수인 점수
            .sample();
            
        // 다양한 유효한 입력으로 테스트
        ValidationResult result = userService.validateRegistration(user);
        assertThat(result.isValid()).isTrue();
    }
}
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
@Test
fun 다양한_입력으로_사용자_등록_검증() {
    repeat(100) {
        val user = fixtureMonkey.giveMeBuilder<User>()
            .setExp(User::username, CombinableArbitrary.strings()
                .alphabetic()
                .withLength(3, 20))           // 유효한 사용자명: 3-20자 알파벳
            .setExp(User::email, CombinableArbitrary.strings()
                .ascii()
                .withLength(5, 50)
                .filter { it.contains("@") }) // 간단한 이메일 검증
            .setExp(User::age, CombinableArbitrary.integers()
                .withRange(13, 120))          // 유효한 나이 범위
            .setExp(User::score, CombinableArbitrary.integers()
                .withRange(0, 100)
                .filter { it % 10 == 0 })     // 10의 배수인 점수
            .sample()
            
        // 다양한 유효한 입력으로 테스트
        val result = userService.validateRegistration(user)
        assertThat(result.isValid).isTrue()
    }
}
{{< /tab >}}
{{< /tabpane>}}

## 추가 자료

모든 Arbitrary 유형과 메서드에 대한 자세한 내용은 [Jqwik 사용자 가이드](https://jqwik.net/docs/current/user-guide.html#static-arbitraries-methods)를 참조하세요.
