package de.uni_passau.fim.se2.sbse.suite_minimisation.examples.dependencies;

/**
 * Represents byte[], as a slice (offset + length) into an existing byte[].
 *
 * <p>{@code BytesRef} implements {@link Comparable}. The underlying byte arrays
 * are sorted lexicographically, numerically treating elements as unsigned. This is identical to
 * Unicode codepoint order.
 */
public interface BytesRef extends Comparable<BytesRef>, Cloneable {

  /**
   * Returns a shallow clone of this instance (the underlying bytes are
   * <b>not</b> copied and will be shared by both the returned object and this
   * object.
   */
  BytesRef clone();

  /**
   * Calculates the hash code as required by TermsHash during indexing
   */
  @Override
  int hashCode();

  @Override
  boolean equals(Object other);

  /**
   * Returns hex encoded bytes, eg [0x6c 0x75 0x63 0x65 0x6e 0x65]
   */
  @Override
  String toString();

  /**
   * Unsigned byte order comparison
   */
  @Override
  int compareTo(BytesRef other);
}