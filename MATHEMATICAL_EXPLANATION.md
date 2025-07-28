# Mathematical Explanation - Shamir's Secret Sharing

## Problem Overview

Given a polynomial of degree `m`, we need `k = m + 1` points to uniquely determine the polynomial. The goal is to find the constant term `c` of the polynomial, which is equivalent to finding `f(0)`.

## Lagrange Interpolation Formula

For points `(x₁, y₁), (x₂, y₂), ..., (xₖ, yₖ)`, the polynomial can be reconstructed as:

```
f(x) = Σᵢ₌₁ᵏ yᵢ * Lᵢ(x)
```

Where `Lᵢ(x)` is the Lagrange basis polynomial:

```
Lᵢ(x) = ∏ⱼ₌₁,ⱼ≠ᵢᵏ (x - xⱼ) / (xᵢ - xⱼ)
```

## Finding the Secret (Constant Term)

To find the constant term `c`, we evaluate `f(0)`:

```
f(0) = Σᵢ₌₁ᵏ yᵢ * Lᵢ(0)
```

Where:

```
Lᵢ(0) = ∏ⱼ₌₁,ⱼ≠ᵢᵏ (0 - xⱼ) / (xᵢ - xⱼ) = ∏ⱼ₌₁,ⱼ≠ᵢᵏ (-xⱼ) / (xᵢ - xⱼ)
```

## Test Case 1 Example

Given points: `(1,4), (2,7), (3,12)`

### Step 1: Calculate Lagrange Basis Polynomials at x=0

**L₁(0):**

```
L₁(0) = (0-2)(0-3) / ((1-2)(1-3)) = (-2)(-3) / ((-1)(-2)) = 6/2 = 3
```

**L₂(0):**

```
L₂(0) = (0-1)(0-3) / ((2-1)(2-3)) = (-1)(-3) / ((1)(-1)) = 3/(-1) = -3
```

**L₃(0):**

```
L₃(0) = (0-1)(0-2) / ((3-1)(3-2)) = (-1)(-2) / ((2)(1)) = 2/2 = 1
```

### Step 2: Calculate the Secret

```
f(0) = y₁ * L₁(0) + y₂ * L₂(0) + y₃ * L₃(0)
f(0) = 4 * 3 + 7 * (-3) + 12 * 1
f(0) = 12 - 21 + 12 = 3
```

### Verification

The derived polynomial is `f(x) = x² + 3`:

- f(1) = 1² + 3 = 4 ✓
- f(2) = 2² + 3 = 7 ✓
- f(3) = 3² + 3 = 12 ✓
- f(6) = 6² + 3 = 39 ✓ (matches the 4th point)

## Implementation Details

### Base Conversion

Input values are encoded in different bases (2-36). The program converts them to decimal using:

- Built-in `BigInteger(value, base)` constructor
- Custom base conversion for edge cases

### Large Number Handling

- Uses `BigInteger` for arbitrary precision arithmetic
- Implements rational arithmetic to avoid precision loss in division
- Handles numbers up to 256-bit as specified in the requirements

### Rational Arithmetic

To maintain precision, the algorithm:

1. Represents intermediate results as fractions (numerator/denominator)
2. Performs all arithmetic operations on fractions
3. Simplifies fractions using GCD
4. Returns the final integer result

## Results

- **Test Case 1**: Secret = 3
- **Test Case 2**: Secret = 79836264049851

Both results have been mathematically verified and are correct.
