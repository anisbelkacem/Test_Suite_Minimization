package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

/**
 * A rational number, i.e. fraction.
 * <p>
 * For example, the rational number 2/3 is created as "new Rational(2, 3)" In this example, 2 is the
 * numerator, 3 is the denominator
 */
public class Rational {

    private long num;
    private long denom;

    /**
     * Construct Rational object from numerator and denominator.
     *
     * @param num   the numerator
     * @param denom the denominator
     * @throws ArithmeticException if denominator is 0 (division by zero)
     */
    public Rational(long num, long denom) {
        if (denom == 0) {
            throw new ArithmeticException();
        }
        this.num = num;
        this.denom = denom;
    }

    /**
     * Normalize the Rational number so that the negative sign is stored in the numerator, rather
     * than the denominator.
     */
    private void normalize() {
        long num = this.num;
        long denom = this.denom;

        if (denom < 0) {
            if (num < 0) {
                this.num = -num;
                this.denom = -denom;
            } else {
                this.denom = -denom;
            }
        }
    }

    /**
     * Reduce numerator and denominator by greatest common denominator.
     */
    public void reduce() {
        this.normalize();
        long g = gcd(this.num, this.denom);
        this.num /= g;
        this.denom /= g;
    }

    /**
     * @return the numerator of this Rational object.
     */
    public long num() {
        return this.num;
    }

    /**
     * @return denominator of this Rational object.
     */
    public long denom() {
        return this.denom;
    }

    /**
     * Addition (changes object in place)
     *
     * @param r a Rational number object
     */
    public void add(Rational r) {
        this.num = (this.num * r.denom()) + (r.num() * this.denom);
        this.denom = this.denom * r.denom();
        this.normalize();
    }

    /**
     * Subtraction
     *
     * @param r a Rational number object
     */
    public void sub(Rational r) {
        this.num = (this.num * r.denom()) - (r.num() * this.denom);
        this.denom = this.denom * r.denom();
        this.normalize();
    }

    /**
     * Multiplication
     *
     * @param r a Rational number object
     */
    public void mul(Rational r) {
        this.num = (this.num * r.num());
        this.denom = (this.denom * r.denom());
        this.normalize();
    }

    /**
     * Division
     *
     * @param r a Rational number object
     */
    public void div(Rational r) {
        this.num = (this.num * r.denom());
        this.denom = (this.denom * r.num());
        this.normalize();
    }

    /**
     * Instance method to test for equivalence with another object (overrides Object's method)
     *
     * @param other an object
     * @return true if the two objects represent the same rational number
     */
    public boolean equals(Object other) {
        if (!(other instanceof Rational)) {
            return false;
        }
        Rational a = (Rational) other;
        if ((a.num() * this.denom()) == (this.num() * a.denom())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Greatest Common Denominator (GCD)
     *
     * @param a first long integer
     * @param b second long integer
     * @return greatest common denominator
     */
    public long gcd(long a, long b) {
        long g;
        if (b == 0) {
            return a;
        } else {
            g = gcd(b, (a % b));
			if (g < 0) {
				return -g;
			} else {
				return g;
			}
        }
    }

}