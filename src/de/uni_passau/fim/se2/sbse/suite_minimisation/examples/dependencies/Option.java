package de.uni_passau.fim.se2.sbse.suite_minimisation.examples.dependencies;
/**
 * Describes a single command-line option. It maintains information regarding the short-name of the
 * option, the long-name, if any exists, a flag indicating if an argument is required for this
 * option, and a self-documenting description of the option.
 *
 * <p>An Option is not created independently, but is created through an instance of {@link Options}.
 * An Option is required to have at least a short or a long-name.
 *
 * <p><b>Note:</b> once an {@link Option} has been added to an instance of {@link Options}, it's
 * required flag may not be changed anymore.
 */
public interface Option {

  /**
   * Returns the 'unique' Option identifier.
   *
   * @return the 'unique' Option identifier
   */
  String getKey();

  /**
   * Retrieve the long name of this Option.
   *
   * @return Long name of this option, or null, if there is no long name
   */
  String getLongOpt();

  /**
   * Query to see if this Option has a long name
   *
   * @return boolean flag indicating existence of a long name
   */
  boolean hasLongOpt();

  /**
   * Query to see if this Option is mandatory
   *
   * @return boolean flag indicating whether this Option is mandatory
   */
  boolean isRequired();

  /**
   * Sets whether this Option is mandatory.
   *
   * @param required specifies whether this Option is mandatory
   */
  void setRequired(final boolean required);
}