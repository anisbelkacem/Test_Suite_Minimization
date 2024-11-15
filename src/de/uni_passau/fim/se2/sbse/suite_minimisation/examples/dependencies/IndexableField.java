package de.uni_passau.fim.se2.sbse.suite_minimisation.examples.dependencies;
/**
 * Represents a single field for indexing.  IndexWriter consumes Iterable&lt;IndexableField&gt; as a
 * document.
 */
public interface IndexableField {

  /**
   * Field name
   */
  String name();

  /**
   * Non-null if this field has a binary value
   */
  BytesRef binaryValue();

  /**
   * Non-null if this field has a string value
   */
  String stringValue();
}