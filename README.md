# Shamir's Secret Sharing Algorithm - Java Implementation

This project implements Shamir's Secret Sharing algorithm to find the constant term of a polynomial using Lagrange interpolation.

## Problem Description

The program solves for the constant term 'c' of an unknown polynomial using given roots in a specific JSON format. The roots' y-values are encoded in different bases and need to be decoded before processing.

## Files

- `ShamirSecretSharingSimple.java` - Main implementation (no external dependencies)
- `ShamirSecretSharing.java` - Alternative implementation (also no external dependencies)
- `VerifyResults.java` - Mathematical verification program
- `testcase1.json` - First test case
- `testcase2.json` - Second test case

## Algorithm

1. **Parse JSON Input**: Read test cases from JSON files
2. **Decode Y Values**: Convert y-values from their respective bases to decimal
3. **Lagrange Interpolation**: Use the decoded points to find the polynomial's constant term

The algorithm uses Lagrange interpolation to find f(0), which is the constant term 'c' of the polynomial.

## Usage

### Compile and Run (Both versions work without dependencies)

```bash
# Option 1: Simple version
javac ShamirSecretSharingSimple.java
java ShamirSecretSharingSimple

# Option 2: Alternative version
javac ShamirSecretSharing.java
java ShamirSecretSharing

# Verification
javac VerifyResults.java
java VerifyResults
```

### Compile and Run (With Gson)

~~If you want to use the Gson version:~~

~~1. Download Gson JAR file~~
~~2. Compile: `javac -cp gson.jar ShamirSecretSharing.java`~~
~~3. Run: `java -cp .:gson.jar ShamirSecretSharing`~~

**Note**: Both versions now work without external dependencies!

## Expected Output

The program will process both test cases and output the secret (constant term) for each:

**Final Results:**

- **Test Case 1**: Secret = **3**
- **Test Case 2**: Secret = **79836264049851**

Both results have been mathematically verified using polynomial reconstruction and manual Lagrange interpolation calculations.

## Implementation Details

- Uses `BigInteger` for handling large numbers (up to 256-bit)
- Implements robust rational arithmetic to avoid precision issues
- Uses Lagrange interpolation formula: f(0) = Σ(yi \* Li(0)) where Li(0) is the Lagrange basis polynomial evaluated at x=0

## Test Cases

- **Test Case 1**: 4 points, requires 3 points (polynomial degree 2)
- **Test Case 2**: 10 points, requires 7 points (polynomial degree 6)
