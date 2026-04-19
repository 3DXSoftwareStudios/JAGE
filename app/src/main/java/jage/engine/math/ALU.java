/**
 * Copyright 2026 Anton Jantos
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.math;

import java.math.BigInteger;

public class ALU {
    
    private static final BigInteger MASK64 = new BigInteger("FFFFFFFFFFFFFFFF", 16);
    private boolean Overflow;
    
    public byte signedAdd8(byte a, byte b) {
        int result = a + b;
        Overflow = result > Byte.MAX_VALUE || result < Byte.MIN_VALUE;
        return (byte) result;
    }

    public byte signedSub8(byte a, byte b) {
        int result = a - b;
        Overflow = result > Byte.MAX_VALUE || result < Byte.MIN_VALUE;
        return (byte) result;
    }

    public byte signedMul8(byte a, byte b) {
        int result = a * b;
        Overflow = result > Byte.MAX_VALUE || result < Byte.MIN_VALUE;
        return (byte) result;
    }

    public byte signedDiv8(byte a, byte b) {
        if (a == Byte.MIN_VALUE && b == -1) {
            Overflow = true;
            return 0;
        }
        Overflow = false;
        return (byte) (a / b);
    }
    
    public byte unsignedAdd8(byte a, byte b) {
        int x = a & 0xFF;
        int y = b & 0xFF;
        int result = x + y;
        Overflow = result > 0xFF;
        return (byte) result;
    }

    public byte unsignedSub8(byte a, byte b) {
        int x = a & 0xFF;
        int y = b & 0xFF;
        int result = x - y;
        Overflow = result < 0;
        return (byte) result;
    }

    public byte unsignedMul8(byte a, byte b) {
        int x = a & 0xFF;
        int y = b & 0xFF;
        int result = x * y;
        Overflow = result > 0xFF;
        return (byte) result;
    }

    public byte unsignedDiv8(byte a, byte b) {
        int x = a & 0xFF;
        int y = b & 0xFF;
        Overflow = false;
        return (byte) (x / y);
    }
    
    public short signedAdd16(short a, short b) {
        int result = a + b;
        Overflow = result > Short.MAX_VALUE || result < Short.MIN_VALUE;
        return (short) result;
    }

    public short signedSub16(short a, short b) {
        int result = a - b;
        Overflow = result > Short.MAX_VALUE || result < Short.MIN_VALUE;
        return (short) result;
    }

    public short signedMul16(short a, short b) {
        int result = a * b;
        Overflow = result > Short.MAX_VALUE || result < Short.MIN_VALUE;
        return (short) result;
    }

    public short signedDiv16(short a, short b) {
        if (a == Short.MIN_VALUE && b == -1) {
            Overflow = true;
            return 0;
        }
        Overflow = false;
        return (short) (a / b);
    }
    
    public short unsignedAdd16(short a, short b) {
        int x = a & 0xFFFF;
        int y = b & 0xFFFF;
        int result = x + y;
        Overflow = result > 0xFFFF;
        return (short) result;
    }

    public short unsignedSub16(short a, short b) {
        int x = a & 0xFFFF;
        int y = b & 0xFFFF;
        int result = x - y;
        Overflow = result < 0;
        return (short) result;
    }

    public short unsignedMul16(short a, short b) {
        int x = a & 0xFFFF;
        int y = b & 0xFFFF;
        int result = x * y;
        Overflow = result > 0xFFFF;
        return (short) result;
    }

    public short unsignedDiv16(short a, short b) {
        int x = a & 0xFFFF;
        int y = b & 0xFFFF;
        Overflow = false;
        return (short) (x / y);
    }
    
    public int signedAdd32(int i1, int i2) {
        long result = (long) i1 + i2;
        Overflow = result > Integer.MAX_VALUE || result < Integer.MIN_VALUE;
        return (int) result;
    }

    public int signedSub32(int i1, int i2) {
        long result = (long) i1 - i2;
        Overflow = result > Integer.MAX_VALUE || result < Integer.MIN_VALUE;
        return (int) result;
    }

    public int signedMul32(int i1, int i2) {
        long result = (long) i1 * i2;
        Overflow = result > Integer.MAX_VALUE || result < Integer.MIN_VALUE;
        return (int) result;
    }

    public int signedDiv32(int i1, int i2) {
        if (i1 == Integer.MIN_VALUE && i2 == -1) {
            Overflow = true;
            return 0;
        }
        Overflow = false;
        return i1 / i2;
    }
    
    public int unsignedAdd32(int a, int b) {
        long x = a & 0xFFFFFFFFL;
        long y = b & 0xFFFFFFFFL;
        long result = x + y;
        Overflow = result > 0xFFFFFFFFL;
        return (int) result;
    }

    public int unsignedSub32(int a, int b) {
        long x = a & 0xFFFFFFFFL;
        long y = b & 0xFFFFFFFFL;
        long result = x - y;
        Overflow = result < 0;
        return (int) result;
    }

    public int unsignedMul32(int a, int b) {
        long x = a & 0xFFFFFFFFL;
        long y = b & 0xFFFFFFFFL;
        long result = x * y;
        Overflow = result > 0xFFFFFFFFL;
        return (int) result;
    }

    public int unsignedDiv32(int a, int b) {
        long x = a & 0xFFFFFFFFL;
        long y = b & 0xFFFFFFFFL;
        Overflow = false;
        return (int) (x / y);
    }
    
    public long signedAdd64(long a, long b) {
        BigInteger result = BigInteger.valueOf(a).add(BigInteger.valueOf(b));
        Overflow = result.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || result.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0;
        return result.longValue();
    }

    public long signedSub64(long a, long b) {
        BigInteger result = BigInteger.valueOf(a).subtract(BigInteger.valueOf(b));
        Overflow = result.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || result.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0;
        return result.longValue();
    }

    public long signedMul64(long a, long b) {
        BigInteger result = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
        Overflow = result.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || result.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0;
        return result.longValue();
    }

    public long signedDiv64(long a, long b) {
        if (a == Long.MIN_VALUE && b == -1) {
            Overflow = true;
            return 0;
        }
        Overflow = false;
        return a / b;
    }

    public long unsignedAdd64(long a, long b) {
        BigInteger x = BigInteger.valueOf(a).and(MASK64);
        BigInteger y = BigInteger.valueOf(b).and(MASK64);
        BigInteger result = x.add(y);
        Overflow = result.compareTo(MASK64) > 0;
        return result.longValue();
    }

    public long unsignedSub64(long a, long b) {
        BigInteger x = BigInteger.valueOf(a).and(MASK64);
        BigInteger y = BigInteger.valueOf(b).and(MASK64);
        BigInteger result = x.subtract(y);
        Overflow = result.signum() < 0;
        return result.longValue();
    }

    public long unsignedMul64(long a, long b) {
        BigInteger x = BigInteger.valueOf(a).and(MASK64);
        BigInteger y = BigInteger.valueOf(b).and(MASK64);
        BigInteger result = x.multiply(y);
        Overflow = result.compareTo(MASK64) > 0;
        return result.longValue();
    }

    public long unsignedDiv64(long a, long b) {
        BigInteger x = BigInteger.valueOf(a).and(MASK64);
        BigInteger y = BigInteger.valueOf(b).and(MASK64);
        Overflow = false;
        return x.divide(y).longValue();
    }
    
    public boolean getOverflow(){
        return Overflow;
    }
    
    public void clearOverflow(){
        Overflow = false;
    }
    
}