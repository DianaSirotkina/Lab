package sample;

import java.math.BigInteger;

class RandomGenerator {

    static String generateUsingMidSquareMethod(String initialValue) {
        BigInteger pow = new BigInteger(initialValue).pow(2);
        StringBuilder s = new StringBuilder(String.valueOf(pow));
        while (s.length() < 16) {
            s.insert(0, "0");
        }
        return s.substring(4, 12);
    }

    static String generateUsingCongruentialMethod(String initialValue, String m, String k){
        BigInteger multiply = new BigInteger(initialValue).multiply(new BigInteger(k));
        BigInteger res = multiply.mod(new BigInteger(m));
        StringBuffer s = new StringBuffer(res.toString());
        while (s.length() < 8) {
            s = s.insert(0, "0");
        }
        return s.toString();
    }
}
