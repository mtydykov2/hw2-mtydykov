import java.util.HashMap;
import java.util.HashSet;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.GeneMention;

/**
 * 
 * This is the final filter on all annotations before output.
 * 
 * @author mtydykov
 * 
 */

public class Filterer extends JCasAnnotator_ImplBase {

  public static final String FINAL = "FINAL";

  /**
   * Use a few simple rules to filter any bad mentions left over by the previous components.
   */
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator annotationIt = arg0.getAnnotationIndex(GeneMention.type).iterator();

    HashSet<GeneMention> finalMentions = new HashSet<GeneMention>();
    while (annotationIt.hasNext()) {
      GeneMention currentMention = (GeneMention) annotationIt.next();
      if (currentMention.getCasProcessorId().equals(Merger.MERGED)) {
        // simple rules to filter mentions that are likely to be wrong
        if ((currentMention.getEnd() - currentMention.getBegin()) > 1
                && !containsBadCharsAtEnd(currentMention.getMentionText())
                && !BadTermChecker.isBadWord(currentMention.getMentionText())) {
          HashMap<Integer, Integer> span = new HashMap<Integer, Integer>();
          span.put(currentMention.getBegin(), currentMention.getEnd());
          finalMentions.add(currentMention);
        }
      }
    }

    for (GeneMention current : finalMentions) {
      GeneMention finalMention = new GeneMention(arg0);
      finalMention.setCasProcessorId(FINAL);
      finalMention.setSentenceId(current.getSentenceId());
      finalMention.setBegin(current.getBegin());
      finalMention.setEnd(current.getEnd());
      finalMention.setMentionText(current.getMentionText());
      finalMention.addToIndexes();
    }
  }

  /**
   * Checks if the given string contains undesirable characters at the end.
   * 
   * @param t
   * @return
   */
  private boolean containsBadCharsAtEnd(String t) {
    if (t.charAt(t.length() - 1) == ')') {
      return true;
    }
    if (t.charAt(t.length() - 1) == '.') {
      return true;
    }
    return false;
  }

}
