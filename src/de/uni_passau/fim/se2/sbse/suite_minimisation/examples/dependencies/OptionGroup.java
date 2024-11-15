package de.uni_passau.fim.se2.sbse.suite_minimisation.examples.dependencies;
import java.util.Collection;

/** A group of mutually exclusive options. */
public interface OptionGroup {

  /** @return the options in this group as a <code>Collection</code> */
  Collection<Option> getOptions();

  /**
   * Returns whether this option group is required.
   *
   * @return whether this option group is required
   */
  boolean isRequired();
}