package fitnesse.wikitext.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// These are the property and variable names used to configure the translation of wiki markup to HTML
public class Names {
  // property for the collapsible widget
  public static final String STATE = "State";
  // values for the state
  public static final String CLOSED = " closed";
  public static final String INVISIBLE = " invisible";

  // variables for the !contents widget
  public static final String FILTER_TOC = "FILTER_TOC";
  public static final String HELP_TOC = "HELP_TOC";
  public static final String HELP_INSTEAD_OF_TITLE_TOC = "HELP_INSTEAD_OF_TITLE_TOC";
  public static final String MORE_SUFFIX_DEFAULT = " ...";
  public static final String MORE_SUFFIX_TOC = "MORE_SUFFIX_TOC";
  public static final String PROPERTY_CHARACTERS = "PROPERTY_CHARACTERS";
  public static final String PROPERTY_TOC = "PROPERTY_TOC";
  public static final String REGRACE_TOC = "REGRACE_TOC";

  // variable for the expression widget
  public static final String FORMAT_LOCALE = "FORMAT_LOCALE";

  // properties for the header widgets
  public static final String LEVEL = "level";
  public static final String ID = "id";

  // properties for the !headings widget
  public static final String STYLE = "STYLE";
  //values for the style
  public static final String DEFAULT_STYLE = "decimal";
  public static final List<String> VALID_STYLES = new ArrayList<>(Arrays.asList(
    "decimal", "decimal-leading-zero", "lower-roman", "upper-roman", "lower-alpha", "upper-alpha", "none"));

  // properties for the !img widget
  public static final String IMAGE_BORDER= "-b";
  public static final String IMAGE_MARGIN = "-m";
  public static final String IMAGE_WIDTH = "-w";
  public static final String IMAGE_CLASS = "image";
  // values for the image class
  public static final String LEFT = "left";
  public static final String RIGHT = "right";

  // variables for the !include widget
  public static final String COLLAPSE_SETUP = "COLLAPSE_SETUP";
  public static final String COLLAPSE_TEARDOWN = "COLLAPSE_TEARDOWN";
  // properties for the !include widget
  public static final String COLLAPSE = "-c";
  public static final String SEAMLESS = "-seamless";
  public static final String SETUP = "-setup";
  public static final String TEARDOWN = "-teardown";
  // values for the include div class
  public static final String TEARDOWN_CLASS = "teardown";
}
