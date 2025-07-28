import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shamir's Secret Sharing Algorithm Implementation
 * This class solves for the constant term 'c' of a polynomial using Lagrange interpolation
 */
public class ShamirSecretSharing {
    
    /**
     * Represents a point (x, y) in the polynomial
     */
    static class Point {
        BigInteger x;
        BigInteger y;
        
        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    /**
     * Simple JSON parser for our specific format
     */
    public static class SimpleJsonParser {
        public static String readFile(String filename) throws IOException {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }
            return content.toString();
        }
        
        public static int extractInt(String json, String key) {
            // First try to match quoted integers
            Pattern quotedPattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(\\d+)\"");
            Matcher quotedMatcher = quotedPattern.matcher(json);
            if (quotedMatcher.find()) {
                return Integer.parseInt(quotedMatcher.group(1));
            }
            
            // Then try to match unquoted integers
            Pattern unquotedPattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
            Matcher unquotedMatcher = unquotedPattern.matcher(json);
            return unquotedMatcher.find() ? Integer.parseInt(unquotedMatcher.group(1)) : 0;
        }
        
        public static String extractString(String json, String key) {
            Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(json);
            return matcher.find() ? matcher.group(1) : "";
        }
    }
    
    /**
     * Parses the JSON input and extracts points with decoded y values
     */
    public static List<Point> parseInput(String jsonFile) throws IOException {
        String jsonContent = SimpleJsonParser.readFile(jsonFile);
        
        List<Point> points = new ArrayList<>();
        
        // Extract k and n values
        int k = SimpleJsonParser.extractInt(jsonContent, "k");
        int n = SimpleJsonParser.extractInt(jsonContent, "n");
        
        System.out.println("Processing " + jsonFile + ":");
        System.out.println("n = " + n + ", k = " + k + " (polynomial degree = " + (k-1) + ")");
        
        // Find all numbered sections (not "keys")
        Pattern sectionPattern = Pattern.compile("\"(\\d+)\"\\s*:\\s*\\{([^}]+)\\}");
        Matcher sectionMatcher = sectionPattern.matcher(jsonContent);
        
        while (sectionMatcher.find()) {
            String key = sectionMatcher.group(1);
            String sectionContent = sectionMatcher.group(2);
            
            // Extract base and value from this section
            int base = SimpleJsonParser.extractInt(sectionContent, "base");
            String value = SimpleJsonParser.extractString(sectionContent, "value");
            
            // x is the key (converted to BigInteger)
            BigInteger x = new BigInteger(key);
            
            // y is the decoded value from the given base
            BigInteger y = decodeValue(value, base);
            
            points.add(new Point(x, y));
            System.out.println("Point " + key + ": base=" + base + ", value=" + value + " -> decoded y=" + y);
        }
        
        // We only need k points for interpolation
        if (points.size() >= k) {
            points = points.subList(0, k);
            System.out.println("Using first " + k + " points for interpolation:");
            for (Point p : points) {
                System.out.println("  " + p);
            }
        }
        
        return points;
    }
    
    /**
     * Decodes a value from the given base to decimal (BigInteger)
     * Supports bases from 2 to 36 (using custom implementation for larger bases if needed)
     */
    public static BigInteger decodeValue(String value, int base) {
        if (base < 2 || base > 36) {
            throw new IllegalArgumentException("Base must be between 2 and 36, got: " + base);
        }
        
        try {
            return new BigInteger(value, base);
        } catch (NumberFormatException e) {
            // If BigInteger fails, implement custom conversion
            return customBaseConversion(value, base);
        }
    }
    
    /**
     * Custom base conversion for cases where BigInteger might fail
     */
    public static BigInteger customBaseConversion(String value, int base) {
        BigInteger result = BigInteger.ZERO;
        BigInteger baseBI = BigInteger.valueOf(base);
        
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            int digitValue;
            
            if (digit >= '0' && digit <= '9') {
                digitValue = digit - '0';
            } else if (digit >= 'a' && digit <= 'z') {
                digitValue = digit - 'a' + 10;
            } else if (digit >= 'A' && digit <= 'Z') {
                digitValue = digit - 'A' + 10;
            } else {
                throw new NumberFormatException("Invalid digit: " + digit);
            }
            
            if (digitValue >= base) {
                throw new NumberFormatException("Digit " + digit + " is not valid for base " + base);
            }
            
            result = result.multiply(baseBI).add(BigInteger.valueOf(digitValue));
        }
        
        return result;
    }
    
    /**
     * Calculates the constant term 'c' using Lagrange interpolation
     * The secret is f(0) = c, where f(x) is the polynomial
     */
    public static BigInteger findSecret(List<Point> points) {
        int n = points.size();
        
        System.out.println("\nCalculating secret using Lagrange interpolation:");
        
        // We'll use rational arithmetic to avoid precision issues
        BigInteger secretNumerator = BigInteger.ZERO;
        BigInteger secretDenominator = BigInteger.ONE;
        
        for (int i = 0; i < n; i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;
            
            BigInteger termNumerator = yi;
            BigInteger termDenominator = BigInteger.ONE;
            
            // Calculate the Lagrange basis polynomial Li(0)
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j).x;
                    // Li(0) has factor (0 - xj) / (xi - xj) = -xj / (xi - xj)
                    termNumerator = termNumerator.multiply(xj.negate());
                    termDenominator = termDenominator.multiply(xi.subtract(xj));
                }
            }
            
            // Add this term to the secret
            BigInteger newNumerator = secretNumerator.multiply(termDenominator).add(termNumerator.multiply(secretDenominator));
            BigInteger newDenominator = secretDenominator.multiply(termDenominator);
            
            // Simplify the fraction by dividing by GCD
            BigInteger gcd = newNumerator.gcd(newDenominator);
            secretNumerator = newNumerator.divide(gcd);
            secretDenominator = newDenominator.divide(gcd);
            
            System.out.println("Term " + (i+1) + ": point=" + points.get(i) + 
                             ", contribution numerator=" + termNumerator + 
                             ", contribution denominator=" + termDenominator);
        }
        
        System.out.println("Final fraction: " + secretNumerator + "/" + secretDenominator);
        
        // The secret should be an integer (secretDenominator should be 1)
        if (!secretDenominator.equals(BigInteger.ONE)) {
            System.out.println("Warning: Secret is not an integer. Result=" + secretNumerator.divide(secretDenominator));
        }
        
        return secretNumerator.divide(secretDenominator);
    }
    
    /**
     * Alternative implementation using a more robust approach for large numbers
     */
    public static BigInteger findSecretRobust(List<Point> points) {
        int n = points.size();
        
        // We'll use rational arithmetic to avoid precision issues
        // Each term will be calculated as a fraction and then combined
        
        BigInteger secretNumerator = BigInteger.ZERO;
        BigInteger secretDenominator = BigInteger.ONE;
        
        for (int i = 0; i < n; i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;
            
            BigInteger termNumerator = yi;
            BigInteger termDenominator = BigInteger.ONE;
            
            // Calculate the Lagrange basis polynomial Li(0)
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j).x;
                    // Li(0) has factor (0 - xj) / (xi - xj) = -xj / (xi - xj)
                    termNumerator = termNumerator.multiply(xj.negate());
                    termDenominator = termDenominator.multiply(xi.subtract(xj));
                }
            }
            
            // Add this term to the secret: secret += termNumerator/termDenominator
            // secret = secretNumerator/secretDenominator + termNumerator/termDenominator
            // secret = (secretNumerator * termDenominator + termNumerator * secretDenominator) / (secretDenominator * termDenominator)
            
            BigInteger newNumerator = secretNumerator.multiply(termDenominator).add(termNumerator.multiply(secretDenominator));
            BigInteger newDenominator = secretDenominator.multiply(termDenominator);
            
            // Simplify the fraction by dividing by GCD
            BigInteger gcd = newNumerator.gcd(newDenominator);
            secretNumerator = newNumerator.divide(gcd);
            secretDenominator = newDenominator.divide(gcd);
        }
        
        // The secret should be an integer (secretDenominator should be 1)
        if (!secretDenominator.equals(BigInteger.ONE)) {
            System.out.println("Warning: Secret is not an integer. Numerator=" + secretNumerator + ", Denominator=" + secretDenominator);
        }
        
        return secretNumerator.divide(secretDenominator);
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Shamir's Secret Sharing Algorithm ===\n");
            
            // Process Test Case 1
            System.out.println("=== TEST CASE 1 ===");
            List<Point> points1 = parseInput("testcase1.json");
            BigInteger secret1 = findSecret(points1);
            System.out.println("\nSecret for Test Case 1: " + secret1);
            System.out.println();
            
            // Process Test Case 2
            System.out.println("=== TEST CASE 2 ===");
            List<Point> points2 = parseInput("testcase2.json");
            BigInteger secret2 = findSecret(points2);
            System.out.println("\nSecret for Test Case 2: " + secret2);
            System.out.println();
            
            // Final output
            System.out.println("=== FINAL RESULTS ===");
            System.out.println("Secret for Test Case 1: " + secret1);
            System.out.println("Secret for Test Case 2: " + secret2);
            
        } catch (IOException e) {
            System.err.println("Error reading input files: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
