package com.wingdao.math.pojo;

public class Fraction {
    //分数整数部分
    private int m;
    //分数分子部分
    private int n;
    //分数分母部分
    private int d;

    public Fraction() {
    }

    public Fraction(int m, int n, int d) {
        this.m = m;
        this.n = n;
        this.d = d;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    //化简为最简分数
    public static Fraction simplifyFra(Fraction fra) {
        Fraction newFra = new Fraction(fra.getM(),fra.getN(),fra.getD());
        int mod = 1;
        int n = newFra.getN();
        int d = newFra.getD();
        int min = n < d ? n : d;
        if (min == 0) {
            return newFra;
        }
        for (int i = 1; i <= min; i++) {
            if (n % i == 0 && d % i == 0) {
                mod = i;
            }
        }
        newFra.setN(n/mod);
        newFra.setD(d/mod);
        return newFra;
    }

    public boolean isZero() {
        return m == 0;
    }

    public Fraction add(Fraction fraction) {
        Fraction leftFra = simplifyFra(this);
        Fraction rightFra = simplifyFra(fraction);
        Fraction result = new Fraction(0, leftFra.getN() * rightFra.getD() + rightFra.getN() * leftFra.getD(), leftFra.getD() * rightFra.getD());
        return simplifyFra(result);
    }

    public Fraction subtract(Fraction fraction) {
        Fraction leftFra = simplifyFra(this);
        Fraction rightFra = simplifyFra(fraction);
        Fraction result = new Fraction(0, leftFra.getN() * rightFra.getD() - rightFra.getN() * leftFra.getD(), leftFra.getD() * rightFra.getD());
        return simplifyFra(result);
    }

    public Fraction multiply(Fraction fraction) {
        Fraction leftFra = simplifyFra(this);
        Fraction rightFra = simplifyFra(fraction);
        Fraction result = new Fraction(0, leftFra.getN() * rightFra.getN(), leftFra.getD() * rightFra.getD());
        return simplifyFra(result);
    }

    public Fraction divide(Fraction fraction) {
        Fraction leftFra = simplifyFra(this);
        Fraction rightFra = simplifyFra(fraction);
        if (rightFra.getN() == 0) {
            return new Fraction(0,0,1);
        } else {
            Fraction result = new Fraction(0, leftFra.getN() * rightFra.getD(), leftFra.getD() * rightFra.getN());
            return simplifyFra(result);
        }
    }

    public boolean isNegative() {
        return n < 0;
    }
}
