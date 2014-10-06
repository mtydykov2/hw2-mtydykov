public class BadTermChecker {
  /**
   * This is a utility class to check strings against known terms that
   * are not gene mentions.
   */
  private static final String[] nonPeptideHormones = { "melatonin", "triiodothyronine",
      "thyroxine", "prostaglandins", "leukotrienes", "prostacyclin", "thromboxane" };
  private static final String[] miscBadTerms = {"mutations"};
  private static final String[] nonPeptideHormoneAbbreviations = { "MT", "T3", "T4", "PG", "LT",
      "PGI2", "TXA2" };

  /**
   * Check if any of the terms in the input string
   * match any of the bad terms.
   * @param t
   * @return
   */
  public static boolean containsBadTerms(String t){
    for(String token: t.split("\\s")){
      if(isBadWord(token)){
        return true;
      }
    }
    return false;
  }
  
  /**
   * Check if the input string is the same as any of the bad terms.
   * @param t
   * @return
   */
  public static boolean isBadWord(String t) {
    if (isBadWord(t, nonPeptideHormones)) {
      return true;
    }
    if (isBadWord(t, nonPeptideHormoneAbbreviations)) {
      return true;
    }
    if(isBadWord (t, miscBadTerms)){
      return true;
    }
    return false;
  }

  /**
   * Given an array of bad terms and an input string,
   * check if any of the bad terms is the same
   * as the input string.
   * @param t
   * @param terms
   * @return
   */
  public static boolean isBadWord(String t, String[] terms) {
    for (String term : terms) {
      if (t.toLowerCase().equals(term.toLowerCase())) {
        return true;
      }
    }
    return false;
  }
}
