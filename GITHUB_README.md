# 🔐 Shamir's Secret Sharing Algorithm - Java Implementation

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Complete-success.svg?style=for-the-badge)](https://github.com/yourusername/shamir-secret-sharing)

> A robust Java implementation of Shamir's Secret Sharing algorithm using Lagrange interpolation to find polynomial constant terms.

## 📖 Problem Description

This project solves the **Catalog Placements Assignment** - implementing a simplified version of Shamir's Secret Sharing algorithm. Given an unknown polynomial of degree `m`, we use `k = m + 1` roots to solve for the coefficients and find the constant term `c`.

**Polynomial Format:**

```
f(x) = a_m * x^m + a_{m-1} * x^{m-1} + ... + a_1 * x + c
```

**Objective:** Find the constant term `c` using Lagrange interpolation from encoded JSON data points.

## 🎯 Key Features

- ✅ **Pure Java Implementation** - No external dependencies required
- ✅ **BigInteger Support** - Handles large numbers up to 256-bit
- ✅ **Multi-Base Decoding** - Supports bases 2-36 for input values
- ✅ **Rational Arithmetic** - Prevents precision loss in calculations
- ✅ **Mathematical Verification** - Includes verification program
- ✅ **Robust Error Handling** - Comprehensive error checking

## 📁 Project Structure

```
shamir-secret-sharing/
├── 📄 ShamirSecretSharingSimple.java  # Main implementation
├── 📄 ShamirSecretSharing.java        # Alternative implementation
├── 📄 VerifyResults.java              # Mathematical verification
├── 📊 testcase1.json                  # Test Case 1 (degree 2 polynomial)
├── 📊 testcase2.json                  # Test Case 2 (degree 6 polynomial)
├── 📚 MATHEMATICAL_EXPLANATION.md     # Detailed math breakdown
└── 📖 README.md                       # This file
```

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- No external libraries required!

### Compilation & Execution

```bash
# Clone the repository
git clone https://github.com/yourusername/shamir-secret-sharing.git
cd shamir-secret-sharing

# Option 1: Run Simple Version (Recommended)
javac ShamirSecretSharingSimple.java
java ShamirSecretSharingSimple

# Option 2: Run Alternative Version
javac ShamirSecretSharing.java
java ShamirSecretSharing

# Run Mathematical Verification
javac VerifyResults.java
java VerifyResults
```

## 📊 Sample Output

```
=== Shamir's Secret Sharing Algorithm ===

=== TEST CASE 1 ===
Processing testcase1.json:
n = 4, k = 3 (polynomial degree = 2)
Point 1: base=10, value=4 -> decoded y=4
Point 2: base=2, value=111 -> decoded y=7
Point 3: base=10, value=12 -> decoded y=12
Using first 3 points for interpolation:
  (1, 4)
  (2, 7)
  (3, 12)

Calculating secret using Lagrange interpolation:
...

=== FINAL RESULTS ===
Secret for Test Case 1: 3
Secret for Test Case 2: 79836264049851
```

## 🧮 Algorithm Explanation

### 1. **Input Parsing**

- Reads JSON test cases with points in format `{"base": "X", "value": "Y"}`
- Decodes Y values from various bases (2-36) to decimal

### 2. **Lagrange Interpolation**

Uses the Lagrange interpolation formula to find `f(0)`:

```
f(0) = Σ(i=1 to k) yi * Li(0)
```

Where `Li(0)` is the Lagrange basis polynomial:

```
Li(0) = ∏(j=1,j≠i to k) (0 - xj) / (xi - xj) = ∏(j=1,j≠i to k) (-xj) / (xi - xj)
```

### 3. **Rational Arithmetic**

- Maintains precision using numerator/denominator pairs
- Simplifies fractions using GCD to prevent overflow
- Ensures final result is an integer

## 📋 Test Cases

| Test Case | Points Available | Points Used | Polynomial Degree | Secret Result      |
| --------- | ---------------- | ----------- | ----------------- | ------------------ |
| Case 1    | 4                | 3           | 2                 | **3**              |
| Case 2    | 10               | 7           | 6                 | **79836264049851** |

### Test Case 1 Verification

**Derived Polynomial:** `f(x) = x² + 3`

| x   | Expected y | Calculated f(x) | ✓   |
| --- | ---------- | --------------- | --- |
| 1   | 4          | 1² + 3 = 4      | ✅  |
| 2   | 7          | 2² + 3 = 7      | ✅  |
| 3   | 12         | 3² + 3 = 12     | ✅  |
| 6   | 39         | 6² + 3 = 39     | ✅  |

**Secret:** `f(0) = 0² + 3 = 3` ✅

## 🔧 Implementation Details

### Base Conversion

```java
// Supports bases 2-36 with custom fallback
public static BigInteger decodeValue(String value, int base) {
    try {
        return new BigInteger(value, base);
    } catch (NumberFormatException e) {
        return customBaseConversion(value, base);
    }
}
```

### Lagrange Interpolation

```java
// Rational arithmetic to prevent precision loss
BigInteger termNumerator = yi;
BigInteger termDenominator = BigInteger.ONE;

for (int j = 0; j < n; j++) {
    if (i != j) {
        termNumerator = termNumerator.multiply(xj.negate());
        termDenominator = termDenominator.multiply(xi.subtract(xj));
    }
}
```

## 🧪 Testing & Verification

Run the verification program to confirm mathematical accuracy:

```bash
java VerifyResults
```

**Output:**

```
✓ All points match! Secret = 3 is correct.
✓ Manual calculation confirms secret = 3
✓ Both test cases have been mathematically verified
```

## 📚 Educational Resources

- **[MATHEMATICAL_EXPLANATION.md](MATHEMATICAL_EXPLANATION.md)** - Detailed mathematical breakdown
- **Lagrange Interpolation** - [Wikipedia](https://en.wikipedia.org/wiki/Lagrange_polynomial)
- **Shamir's Secret Sharing** - [Original Paper](https://web.mit.edu/6.857/OldStuff/Fall03/ref/Shamir-HowToShareASecret.pdf)

## 🛠️ Development

### VS Code Tasks

This project includes VS Code tasks for easy development:

- `Ctrl+Shift+P` → "Tasks: Run Task"
- Choose from available build/run tasks

### Project Requirements Met

- ✅ Pure Java implementation (no Python)
- ✅ Reads JSON input from separate files
- ✅ Correctly decodes Y values from different bases
- ✅ Calculates secret using polynomial interpolation
- ✅ Handles 256-bit numbers
- ✅ Provides correct output for both test cases

## 📈 Performance Characteristics

- **Time Complexity:** O(k²) where k is the number of points used
- **Space Complexity:** O(k) for storing points
- **Precision:** Arbitrary precision using BigInteger
- **Supported Bases:** 2 to 36

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🏆 Assignment Results

**Final Results:**

- **Test Case 1 Secret:** `3`
- **Test Case 2 Secret:** `79836264049851`

**Status:** ✅ Assignment Complete - All requirements met and mathematically verified!

---

<div align="center">

**[⭐ Star this repo](https://github.com/yourusername/shamir-secret-sharing)** if you found it helpful!

Made with ❤️ for Catalog Placements Assignment

</div>
