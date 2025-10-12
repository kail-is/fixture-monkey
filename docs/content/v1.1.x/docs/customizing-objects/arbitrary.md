---
title: "Creating Random Test Data with Conditions"
weight: 43
menu:
docs:
parent: "customizing-objects"
identifier: "arbitrary"
---

## What you will learn in this document
- How to create test data with random but controlled values
- How to set ranges, patterns, and limits for your test data
- When and why to use random values instead of fixed values

## Introduction to Random Test Data
Sometimes in testing, using **fixed values** isn't enough. You might want:
- A range of valid inputs rather than a single value
- Different test data each time the test runs
- Random but realistic data that follows business rules

For example, when testing:
- Age validation: you might want random ages between 18-65
- Username validation: you need random strings following specific patterns
- Payment processing: you need various amounts within certain ranges

## Understanding Arbitrary
In Fixture Monkey, we use `Arbitrary` to create random values that follow rules. Think of an `Arbitrary` as a **value generator with rules**.

> **In simple terms:** An Arbitrary is like a machine that produces random values, but only values that follow your rules.

## Step-by-Step Guide to Random Values

### 1. Basic Usage: Setting a Simple Range

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Create a member with age between 20 and 30
Member member = fixtureMonkey.giveMeBuilder(Member.class)
    .set("age", Arbitraries.integers().between(20, 30))  // Random age between 20-30
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Create a member with age between 20 and 30
val member = fixtureMonkey.giveMeBuilder<Member>()
    .setExp(Member::age, Arbitraries.integers().between(20, 30))  // Random age between 20-30
    .sample()
{{< /tab >}}
{{< /tabpane>}}

### 2. Working with Text: String Patterns

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Create a user with valid username (lowercase letters, 5-10 characters)
User user = fixtureMonkey.giveMeBuilder(User.class)
    .set("username", Arbitraries.strings()
        .withCharRange('a', 'z')  // Only lowercase letters
        .ofMinLength(5)           // At least 5 characters
        .ofMaxLength(10))         // At most 10 characters
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Create a user with valid username (lowercase letters, 5-10 characters)
val user = fixtureMonkey.giveMeBuilder<User>()
    .setExp(User::username, Arbitraries.strings()
        .withCharRange('a', 'z')  // Only lowercase letters
        .ofMinLength(5)           // At least 5 characters
        .ofMaxLength(10))         // At most 10 characters
    .sample()
{{< /tab >}}
{{< /tabpane>}}

### 3. Selecting from Valid Options

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Create an order with a valid status
Order order = fixtureMonkey.giveMeBuilder(Order.class)
    .set("status", Arbitraries.of(  // Randomly pick one of these values
        OrderStatus.PENDING,
        OrderStatus.PROCESSING,
        OrderStatus.SHIPPED))
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Create an order with a valid status
val order = fixtureMonkey.giveMeBuilder<Order>()
    .setExp(Order::status, Arbitraries.of(  // Randomly pick one of these values
        OrderStatus.PENDING,
        OrderStatus.PROCESSING,
        OrderStatus.SHIPPED))
    .sample()
{{< /tab >}}
{{< /tabpane>}}

### 4. Combining Multiple Constraints

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Create a product with various constraints
Product product = fixtureMonkey.giveMeBuilder(Product.class)
    .set("id", Arbitraries.longs().greaterOrEqual(1000))  // ID at least 1000
    .set("name", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(10))  // Name max 10 chars
    .set("price", Arbitraries.bigDecimals()
        .between(BigDecimal.valueOf(10.0), BigDecimal.valueOf(1000.0)))  // Price between 10-1000
    .set("category", Arbitraries.of("Electronics", "Clothing", "Books"))  // One of these categories
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Create a product with various constraints
val product = fixtureMonkey.giveMeBuilder<Product>()
    .setExp(Product::id, Arbitraries.longs().greaterOrEqual(1000))  // ID at least 1000
    .setExp(Product::name, Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(10))  // Name max 10 chars
    .setExp(Product::price, Arbitraries.bigDecimals()
        .between(BigDecimal.valueOf(10.0), BigDecimal.valueOf(1000.0)))  // Price between 10-1000
    .setExp(Product::category, Arbitraries.of("Electronics", "Clothing", "Books"))  // One of these categories
    .sample()
{{< /tab >}}
{{< /tabpane>}}

## Real-world Example: Testing Age Verification

Let's say you're testing a service that only allows adult members (18+) but has senior discounts (65+):

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
@Test
void adultMembersCanRegister() {
    // Create 50 random adult members for testing
    for (int i = 0; i < 50; i++) {
        Member member = fixtureMonkey.giveMeBuilder(Member.class)
            .set("age", Arbitraries.integers().between(18, 100))  // Adults only
            .sample();
            
        boolean isSenior = member.getAge() >= 65;
        
        // Test registration logic with various ages
        MembershipResponse response = membershipService.register(member);
        
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.hasDiscount()).isEqualTo(isSenior);  // Seniors get discounts
    }
}
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
@Test
fun adultMembersCanRegister() {
    // Create 50 random adult members for testing
    repeat(50) {
        val member = fixtureMonkey.giveMeBuilder<Member>()
            .setExp(Member::age, Arbitraries.integers().between(18, 100))  // Adults only
            .sample()
            
        val isSenior = member.age >= 65
        
        // Test registration logic with various ages
        val response = membershipService.register(member)
        
        assertThat(response.isSuccess).isTrue()
        assertThat(response.hasDiscount).isEqualTo(isSenior)  // Seniors get discounts
    }
}
{{< /tab >}}
{{< /tabpane>}}

## Common Arbitrary Methods

| Method | Purpose | Example |
|--------|---------|---------|
| `between(min, max)` | Values in range | `Arbitraries.integers().between(1, 100)` |
| `greaterOrEqual(min)` | Values ≥ min | `Arbitraries.longs().greaterOrEqual(1000)` |
| `lessOrEqual(max)` | Values ≤ max | `Arbitraries.doubles().lessOrEqual(99.9)` |
| `ofMaxLength(max)` | Strings with max length | `Arbitraries.strings().ofMaxLength(10)` |
| `withCharRange(from, to)` | Strings with character range | `Arbitraries.strings().withCharRange('a', 'z')` |
| `of(values...)` | Choose from options | `Arbitraries.of("Red", "Green", "Blue")` |

## Frequently Asked Questions

### When should I use Arbitrary instead of fixed values?

Use Arbitrary when:
- You want to test with a variety of inputs rather than a single value
- The exact value doesn't matter, but it needs to follow rules
- You want to discover edge cases automatically
- You need to test many different valid inputs

### Won't random values make my tests inconsistent?

While values are random, they still follow your defined rules. This helps you:
- Find bugs that only appear with certain values
- Ensure your code works with the full range of valid inputs
- Discover unexpected edge cases

If a test fails, you can use Fixture Monkey's `@Seed` annotation to make it reproducible:

```java
import com.navercorp.fixturemonkey.junit.jupiter.annotation.Seed;
import com.navercorp.fixturemonkey.junit.jupiter.extension.FixtureMonkeySeedExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FixtureMonkeySeedExtension.class)
class MembershipTest {
    @Test
    @Seed(123L)  // Use a specific seed for predictable random values
    void testAdultMembersOnly() {
        Member member = fixtureMonkey.giveMeBuilder(Member.class)
            .set("age", Arbitraries.integers().between(18, 100))
            .sample();
            
        // Your test logic
        assertThat(membershipService.isEligible(member)).isTrue();
    }
}
```

With the `@Seed` annotation, Fixture Monkey will use the specified seed value to generate the same "random" values every time the test runs. This makes tests with random data completely reproducible.

One of the most useful features of `FixtureMonkeySeedExtension` is that it automatically logs the seed value when a test fails:

```
Test Method [MembershipTest#testAdultMembersOnly] failed with seed: 42
```

You can then add this seed value to your `@Seed` annotation to consistently reproduce the exact test scenario that failed.

### How is this different from setPostCondition()?

- `setPostCondition()` generates any value and then checks if it matches a condition
- `Arbitrary` directly generates values that meet the constraints

Use `Arbitrary` when you need more control over the generated values or when `setPostCondition()` is too slow because it has to discard many invalid values.

## Advanced Arbitrary Types (Experimental)

Since version 1.1.12, Fixture Monkey provides specialized arbitrary types for more control over value generation.

### CombinableArbitrary.integers()

The `CombinableArbitrary.integers()` method returns an `IntegerCombinableArbitrary` that provides specialized methods for integer generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate integers with various constraints
Member member = fixtureMonkey.giveMeBuilder(Member.class)
    .set("age", CombinableArbitrary.integers()
        .withRange(18, 65)     // Age between 18-65
        .positive())           // Only positive numbers
    .set("score", CombinableArbitrary.integers()
        .even()                // Only even numbers
        .withRange(0, 100))    // Between 0-100
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Generate integers with various constraints
val member = fixtureMonkey.giveMeBuilder<Member>()
    .setExp(Member::age, CombinableArbitrary.integers()
        .withRange(18, 65)     // Age between 18-65
        .positive())           // Only positive numbers
    .setExp(Member::score, CombinableArbitrary.integers()
        .even()                // Only even numbers
        .withRange(0, 100))    // Between 0-100
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### IntegerCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate integers between min and max (inclusive) | `integers().withRange(1, 100)` |
| `positive()` | Generate only positive integers (≥ 1) | `integers().positive()` |
| `negative()` | Generate only negative integers (≤ -1) | `integers().negative()` |
| `even()` | Generate only even integers | `integers().even()` |
| `odd()` | Generate only odd integers | `integers().odd()` |

**Important Note:** When multiple constraint methods are chained, the **last method wins**. For example:
```java
// This will generate negative integers, ignoring the positive() call
CombinableArbitrary.integers().positive().negative()

// This will generate integers in range 10-50, ignoring the positive() call
CombinableArbitrary.integers().positive().withRange(10, 50)
```

### CombinableArbitrary.bytes()

The `CombinableArbitrary.bytes()` method returns a `ByteCombinableArbitrary` dedicated to byte value generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate byte-specific attributes with constraints
Packet packet = fixtureMonkey.giveMeBuilder(Packet.class)
    .set("flag", CombinableArbitrary.bytes()
        .ascii()               // ASCII-safe bytes
        .even())               // Only even values
    .set("signal", CombinableArbitrary.bytes()
        .withRange((byte)10, (byte)42)  // Between 10 and 42
        .positive())           // Only positive bytes
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Generate byte-specific attributes with constraints
val packet = fixtureMonkey.giveMeBuilder<Packet>()
    .setExp(Packet::flag, CombinableArbitrary.bytes()
        .ascii()               // ASCII-safe bytes
        .even())               // Only even values
    .setExp(Packet::signal, CombinableArbitrary.bytes()
        .withRange(10.toByte(), 42.toByte())  // Between 10 and 42
        .positive())           // Only positive bytes
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### ByteCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate bytes between min and max (inclusive) | `bytes().withRange((byte)1, (byte)100)` |
| `positive()` | Generate only positive bytes (≥ 1) | `bytes().positive()` |
| `negative()` | Generate only negative bytes (≤ -1) | `bytes().negative()` |
| `even()` | Generate only even bytes | `bytes().even()` |
| `odd()` | Generate only odd bytes | `bytes().odd()` |
| `ascii()` | Generate ASCII bytes (0-127) | `bytes().ascii()` |

**Important Notes:**
- Constraint methods follow the **last method wins** rule:
  ```java
  // positive() is ignored; only negative bytes are produced
  CombinableArbitrary.bytes().positive().negative()
  ```
- Combining `ascii()` with other methods further narrows the result:
  ```java
  // Only even ASCII bytes (0, 2, 4, ..., 126)
  CombinableArbitrary.bytes().ascii().even()
  ```

### CombinableArbitrary.longs()

The `CombinableArbitrary.longs()` method returns a `LongCombinableArbitrary` dedicated to 64-bit integer generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate long values with business constraints
Payment payment = fixtureMonkey.giveMeBuilder(Payment.class)
    .set("amount", CombinableArbitrary.longs()
        .withRange(10_000L, 1_000_000L)  // Between 10,000 and 1,000,000
        .positive())                     // Only positive values
    .set("transactionId", CombinableArbitrary.longs()
        .multipleOf(100)                 // Batch-aligned ids
        .nonZero())                      // Avoid zero ids
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Generate long values with business constraints
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

#### LongCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate longs between min and max (inclusive) | `longs().withRange(0L, 1_000L)` |
| `positive()` | Generate only positive longs (≥ 1) | `longs().positive()` |
| `negative()` | Generate only negative longs (≤ -1) | `longs().negative()` |
| `even()` | Generate only even longs | `longs().even()` |
| `odd()` | Generate only odd longs | `longs().odd()` |
| `nonZero()` | Exclude zero from generation | `longs().nonZero()` |
| `multipleOf(divisor)` | Generate longs that are multiples of the given divisor | `longs().multipleOf(500)` |

**Important Notes:**
- Constraint methods follow the **last method wins** rule.
- `multipleOf(0)` throws an `ArithmeticException`; provide a non-zero divisor.

### CombinableArbitrary.shorts()

The `CombinableArbitrary.shorts()` method returns a `ShortCombinableArbitrary` for 16-bit integer generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate scheduling helpers with short values
Schedule schedule = fixtureMonkey.giveMeBuilder(Schedule.class)
    .set("month", CombinableArbitrary.shorts().month())
    .set("day", CombinableArbitrary.shorts()
        .day()
        .filter(d -> d <= 28))  // Limit to 28 for safety
    .set("score", CombinableArbitrary.shorts().score())
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Generate scheduling helpers with short values
val schedule = fixtureMonkey.giveMeBuilder<Schedule>()
    .setExp(Schedule::month, CombinableArbitrary.shorts().month())
    .setExp(Schedule::day, CombinableArbitrary.shorts()
        .day()
        .filter { it <= 28 })
    .setExp(Schedule::score, CombinableArbitrary.shorts().score())
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### ShortCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate shorts between min and max (inclusive) | `shorts().withRange((short)1, (short)100)` |
| `positive()` | Generate only positive shorts (≥ 1) | `shorts().positive()` |
| `negative()` | Generate only negative shorts (≤ -1) | `shorts().negative()` |
| `even()` | Generate only even shorts | `shorts().even()` |
| `odd()` | Generate only odd shorts | `shorts().odd()` |
| `nonZero()` | Exclude zero from generation | `shorts().nonZero()` |
| `multipleOf(value)` | Generate multiples of the provided value | `shorts().multipleOf((short)5)` |
| `percentage()` | Generate shorts in the 0-100 range | `shorts().percentage()` |
| `score()` | Generate shorts useful for scoring (0-100) | `shorts().score()` |
| `year()` | Generate year-like values (1900-2100) | `shorts().year()` |
| `month()` | Generate months (1-12) | `shorts().month()` |
| `day()` | Generate days (1-31) | `shorts().day()` |
| `hour()` | Generate hours (0-23) | `shorts().hour()` |
| `minute()` | Generate minutes (0-59) | `shorts().minute()` |

**Important Notes:**
- As with other combinable arbitraries, the **last method wins**.
- Combine convenience methods (`year()`, `month()`, etc.) with additional filters for domain specifics (e.g., leap years).

### CombinableArbitrary.bigIntegers()

The `CombinableArbitrary.bigIntegers()` method returns a `BigIntegerCombinableArbitrary` for arbitrary-precision integer generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate loyalty data requiring large integer domains
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
// Generate loyalty data requiring large integer domains
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

#### BigIntegerCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate values between min and max (inclusive) | `bigIntegers().withRange(BigInteger.ZERO, new BigInteger("1000"))` |
| `positive()` | Generate only positive values (≥ 1) | `bigIntegers().positive()` |
| `negative()` | Generate only negative values (≤ -1) | `bigIntegers().negative()` |
| `nonZero()` | Exclude zero from generation | `bigIntegers().nonZero()` |
| `percentage()` | Generate values in the 0-100 range | `bigIntegers().percentage()` |
| `score(min, max)` | Generate score-like values in a custom range | `bigIntegers().score(BigInteger.ZERO, new BigInteger("1000"))` |
| `even()` | Generate only even values | `bigIntegers().even()` |
| `odd()` | Generate only odd values | `bigIntegers().odd()` |
| `multipleOf(divisor)` | Generate values that are multiples of the divisor | `bigIntegers().multipleOf(new BigInteger("1024"))` |
| `prime()` | Generate prime numbers within the current constraints | `bigIntegers().prime()` |

**Important Notes:**
- Prime generation can be expensive for large ranges; intersect with `withRange` to keep search spaces manageable.
- Subsequent constraint calls override previous ones (last method wins).

### CombinableArbitrary.bigDecimals()

The `CombinableArbitrary.bigDecimals()` method returns a `BigDecimalCombinableArbitrary` for arbitrary-precision decimal generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate monetary values with precision and business rules
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
// Generate monetary values with precision and business rules
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

#### BigDecimalCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate values between min and max (inclusive) | `bigDecimals().withRange(new BigDecimal("0.0"), new BigDecimal("100.0"))` |
| `positive()` | Generate only positive values (> 0) | `bigDecimals().positive()` |
| `negative()` | Generate only negative values (< 0) | `bigDecimals().negative()` |
| `nonZero()` | Exclude zero from generation | `bigDecimals().nonZero()` |
| `percentage()` | Generate values between 0.0 and 100.0 | `bigDecimals().percentage()` |
| `score(min, max)` | Generate score-like values in a custom range | `bigDecimals().score(new BigDecimal("1.0"), new BigDecimal("5.0"))` |
| `withPrecision(precision)` | Limit significant digits | `bigDecimals().withPrecision(4)` |
| `withScale(scale)` | Fix digits to the right of the decimal point | `bigDecimals().withScale(2)` |
| `normalized()` | Strip trailing zeros for normalized representations | `bigDecimals().normalized()` |

**Important Notes:**
- Combining `withPrecision` and `withScale` lets you enforce currency-like formats.
- As with other types, the **last method wins** when constraints conflict.

### CombinableArbitrary.doubles()

The `CombinableArbitrary.doubles()` method returns a `DoubleCombinableArbitrary` for double-precision floating-point generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate sensor measurements with floating-point constraints
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
// Generate sensor measurements with floating-point constraints
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

#### DoubleCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate doubles between min and max (inclusive) | `doubles().withRange(-10.0, 10.0)` |
| `positive()` | Generate only positive doubles (> 0) | `doubles().positive()` |
| `negative()` | Generate only negative doubles (< 0) | `doubles().negative()` |
| `nonZero()` | Exclude zero from generation | `doubles().nonZero()` |
| `withPrecision(scale)` | Limit decimal places | `doubles().withPrecision(2)` |
| `finite()` | Exclude NaN and infinity | `doubles().finite()` |
| `infinite()` | Generate ±∞ values | `doubles().infinite()` |
| `normalized()` | Generate values between 0.0 and 1.0 | `doubles().normalized()` |
| `nan()` | Generate NaN values | `doubles().nan()` |
| `percentage()` | Generate values between 0 and 100 | `doubles().percentage()` |
| `score()` | Generate values suited for scoring (0-100) | `doubles().score()` |
| `withSpecialValue(value)` | Inject a custom edge case | `doubles().withSpecialValue(Double.MIN_NORMAL)` |
| `withStandardSpecialValues()` | Inject standard IEEE edge cases | `doubles().withStandardSpecialValues()` |

**Important Notes:**
- Applying methods like `nan()`, `infinite()`, or `withStandardSpecialValues()` overrides earlier range restrictions—chain them last.
- Use `finite()` to explicitly exclude IEEE special values before applying further filters.

### CombinableArbitrary.floats()

The `CombinableArbitrary.floats()` method returns a `FloatCombinableArbitrary` mirroring the double API for single-precision floats:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate float-based sensor readings
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
// Generate float-based sensor readings
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

#### FloatCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate floats between min and max (inclusive) | `floats().withRange(-5f, 5f)` |
| `positive()` | Generate only positive floats (> 0) | `floats().positive()` |
| `negative()` | Generate only negative floats (< 0) | `floats().negative()` |
| `nonZero()` | Exclude zero from generation | `floats().nonZero()` |
| `withPrecision(scale)` | Limit decimal places | `floats().withPrecision(3)` |
| `finite()` | Exclude NaN and infinity | `floats().finite()` |
| `infinite()` | Generate ±∞ values | `floats().infinite()` |
| `normalized()` | Generate values between 0.0 and 1.0 | `floats().normalized()` |
| `nan()` | Generate NaN values | `floats().nan()` |
| `percentage()` | Generate values between 0 and 100 | `floats().percentage()` |
| `score()` | Generate values suited for scoring (0-100) | `floats().score()` |
| `withSpecialValue(value)` | Inject a custom edge case | `floats().withSpecialValue(Float.MIN_VALUE)` |
| `withStandardSpecialValues()` | Inject standard IEEE edge cases | `floats().withStandardSpecialValues()` |

**Important Notes:**
- Prefer `finite()` when float consumers cannot handle NaN or infinity.
- Inject special values last so they survive range or normalization constraints.

### CombinableArbitrary.strings()

The `CombinableArbitrary.strings()` method returns a `StringCombinableArbitrary` that provides specialized methods for string generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate strings with various character sets and constraints
User user = fixtureMonkey.giveMeBuilder(User.class)
    .set("username", CombinableArbitrary.strings()
        .alphabetic()          // Only alphabetic characters
        .withLength(5, 15))    // Length between 5-15
    .set("password", CombinableArbitrary.strings()
        .ascii()               // ASCII characters
        .withMinLength(8))     // At least 8 characters
    .set("phoneNumber", CombinableArbitrary.strings()
        .numeric()             // Only numeric characters
        .withLength(10, 11))   // 10 or 11 digits
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Generate strings with various character sets and constraints
val user = fixtureMonkey.giveMeBuilder<User>()
    .setExp(User::username, CombinableArbitrary.strings()
        .alphabetic()          // Only alphabetic characters
        .withLength(5, 15))    // Length between 5-15
    .setExp(User::password, CombinableArbitrary.strings()
        .ascii()               // ASCII characters
        .withMinLength(8))     // At least 8 characters
    .setExp(User::phoneNumber, CombinableArbitrary.strings()
        .numeric()             // Only numeric characters
        .withLength(10, 11))   // 10 or 11 digits
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### StringCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withLength(min, max)` | Generate strings with length between min and max | `strings().withLength(5, 10)` |
| `withMinLength(min)` | Generate strings with minimum length | `strings().withMinLength(3)` |
| `withMaxLength(max)` | Generate strings with maximum length | `strings().withMaxLength(20)` |
| `alphabetic()` | Generate strings with only alphabetic characters (a-z, A-Z) | `strings().alphabetic()` |
| `ascii()` | Generate strings with only ASCII characters | `strings().ascii()` |
| `numeric()` | Generate strings with only numeric characters (0-9) | `strings().numeric()` |
| `korean()` | Generate strings with only Korean characters (가-힣) | `strings().korean()` |
| `filterCharacter(predicate)` | Filter individual characters in the string | `strings().filterCharacter(c -> c != 'x')` |

**Important Notes:** 
1. **Character set methods conflict with each other.** When multiple character set methods are chained, the **last method wins**:
   ```java
   // This will generate Korean characters only, ignoring alphabetic()
   CombinableArbitrary.strings().alphabetic().korean()
   ```

2. **Character set methods ignore other configuration methods.** When a character set method is called, it creates a new instance that ignores previous configurations:
   ```java
   // The withLength(5, 10) is ignored when alphabetic() is called
   CombinableArbitrary.strings().withLength(5, 10).alphabetic()
   ```

### CombinableArbitrary.chars()

The `CombinableArbitrary.chars()` method returns a `CharacterCombinableArbitrary` for single-character generation:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Generate membership attributes with character constraints
Member member = fixtureMonkey.giveMeBuilder(Member.class)
    .set("grade", CombinableArbitrary.chars()
        .uppercase()           // Uppercase letters only
        .withRange('A', 'D'))  // Between A and D
    .set("locale", CombinableArbitrary.chars()
        .korean()              // Hangul syllables
        .filter(c -> c >= '나')) // Additional filter
    .sample();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Generate membership attributes with character constraints
val member = fixtureMonkey.giveMeBuilder<Member>()
    .setExp(Member::grade, CombinableArbitrary.chars()
        .uppercase()           // Uppercase letters only
        .withRange('A', 'D'))  // Between A and D
    .setExp(Member::locale, CombinableArbitrary.chars()
        .korean()              // Hangul syllables
        .filter { it >= '나' }) // Additional filter
    .sample()
{{< /tab >}}
{{< /tabpane>}}

#### CharacterCombinableArbitrary Methods

| Method | Description | Example |
|--------|-------------|---------|
| `withRange(min, max)` | Generate characters between min and max (inclusive) | `chars().withRange('A', 'Z')` |
| `alphabetic()` | Generate alphabetic characters (a-z, A-Z) | `chars().alphabetic()` |
| `numeric()` | Generate numeric characters (0-9) | `chars().numeric()` |
| `alphaNumeric()` | Generate alphanumeric characters | `chars().alphaNumeric()` |
| `ascii()` | Generate printable ASCII characters (0x20-0x7E) | `chars().ascii()` |
| `uppercase()` | Generate uppercase letters (A-Z) | `chars().uppercase()` |
| `lowercase()` | Generate lowercase letters (a-z) | `chars().lowercase()` |
| `korean()` | Generate Hangul syllables (가-힣) | `chars().korean()` |
| `emoji()` | Generate a curated set of emoji code points | `chars().emoji()` |
| `whitespace()` | Generate whitespace characters (space, tab, etc.) | `chars().whitespace()` |

**Important Notes:**
- Character set methods (`alphabetic`, `numeric`, `uppercase`, etc.) follow the **last method wins** rule:
  ```java
  // alphabetic() is ignored; only digits are produced
  CombinableArbitrary.chars().alphabetic().numeric()
  ```
- Applying `withRange` or `filter` after a character set method further narrows the output:
  ```java
  // Restricts uppercase letters to A-C
  CombinableArbitrary.chars().uppercase().withRange('A', 'C')
  ```

### Advanced Filtering

All numeric CombinableArbitrary types (`integers`, `bytes`, `shorts`, `longs`, `bigIntegers`, `bigDecimals`, `doubles`, `floats`) as well as `strings()` and `chars()` support advanced filtering:

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
// Filter integers with custom conditions
Integer score = CombinableArbitrary.integers()
    .withRange(0, 100)
    .filter(n -> n % 5 == 0)  // Only multiples of 5
    .combined();

// Filter strings with custom character conditions
String code = CombinableArbitrary.strings()
    .withLength(6, 8)
    .filterCharacter(c -> Character.isUpperCase(c) || Character.isDigit(c))  // Only uppercase letters and digits
    .combined();

// Filter bytes with custom conditions
Byte checksum = CombinableArbitrary.bytes()
    .ascii()
    .filter(b -> (b & 0x0F) == 0)  // Low nibble must be 0
    .combined();

// Filter characters with custom conditions
Character emoji = CombinableArbitrary.chars()
    .emoji()
    .filter(c -> Character.getType(c) == Character.OTHER_SYMBOL || c == '❤')
    .combined();
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
// Filter integers with custom conditions
val score = CombinableArbitrary.integers()
    .withRange(0, 100)
    .filter { it % 5 == 0 }  // Only multiples of 5
    .combined()

// Filter strings with custom character conditions
val code = CombinableArbitrary.strings()
    .withLength(6, 8)
    .filterCharacter { it.isUpperCase() || it.isDigit() }  // Only uppercase letters and digits
    .combined()

// Filter bytes with custom conditions
val checksum = CombinableArbitrary.bytes()
    .ascii()
    .filter { (it.toInt() and 0x0F) == 0 }  // Low nibble must be 0
    .combined()

// Filter characters with custom conditions
val emoji = CombinableArbitrary.chars()
    .emoji()
    .filter { Character.getType(it) == Character.OTHER_SYMBOL || it == '❤' }
    .combined()
{{< /tab >}}
{{< /tabpane>}}

### Real-world Example: User Registration Validation

{{< tabpane persist=false >}}
{{< tab header="Java" lang="java">}}
@Test
void validateUserRegistrationWithVariousInputs() {
    for (int i = 0; i < 100; i++) {
        User user = fixtureMonkey.giveMeBuilder(User.class)
            .set("username", CombinableArbitrary.strings()
                .alphabetic()
                .withLength(3, 20))           // Valid username: 3-20 alphabetic chars
            .set("email", CombinableArbitrary.strings()
                .ascii()
                .withLength(5, 50)
                .filter(s -> s.contains("@"))) // Simple email validation
            .set("age", CombinableArbitrary.integers()
                .withRange(13, 120))          // Valid age range
            .set("score", CombinableArbitrary.integers()
                .withRange(0, 100)
                .filter(n -> n % 10 == 0))    // Score in multiples of 10
            .sample();
            
        // Test with various valid inputs
        ValidationResult result = userService.validateRegistration(user);
        assertThat(result.isValid()).isTrue();
    }
}
{{< /tab >}}
{{< tab header="Kotlin" lang="kotlin">}}
@Test
fun validateUserRegistrationWithVariousInputs() {
    repeat(100) {
        val user = fixtureMonkey.giveMeBuilder<User>()
            .setExp(User::username, CombinableArbitrary.strings()
                .alphabetic()
                .withLength(3, 20))           // Valid username: 3-20 alphabetic chars
            .setExp(User::email, CombinableArbitrary.strings()
                .ascii()
                .withLength(5, 50)
                .filter { it.contains("@") }) // Simple email validation
            .setExp(User::age, CombinableArbitrary.integers()
                .withRange(13, 120))          // Valid age range
            .setExp(User::score, CombinableArbitrary.integers()
                .withRange(0, 100)
                .filter { it % 10 == 0 })     // Score in multiples of 10
            .sample()
            
        // Test with various valid inputs
        val result = userService.validateRegistration(user)
        assertThat(result.isValid).isTrue()
    }
}
{{< /tab >}}
{{< /tabpane>}}

## Additional Resources

For more details about all available Arbitrary types and methods, see the [Jqwik User Guide](https://jqwik.net/docs/current/user-guide.html#static-arbitraries-methods)
