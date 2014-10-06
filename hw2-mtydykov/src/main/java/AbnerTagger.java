import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import abner.Tagger;
import edu.cmu.deiis.types.GeneMention;
import edu.cmu.deiis.types.SentenceData;

/**
 * 
 * This is an annotator that uses the ABNER software to extract gene mentions.
 * 
 * @author mtydykov
 * 
 */

public class AbnerTagger extends JCasAnnotator_ImplBase {

  private static final String NE_TYPE_TO_EXTRACT = "DNA";

  private static final String ABNER_ID = "1";

  Tagger a = null;

  private final static String PARAM_MODEL_FILE = "ModelFileName";

  /**
   * Tag all gene mentions for the given sentence.
   */
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {

    FSIterator it = arg0.getAnnotationIndex(SentenceData.type).iterator();
    SentenceData curr = (SentenceData) it.next();
    String sentTextNoSpaces = curr.getSentText().replaceAll("\\s", "");
    String[] entities = a.getEntities(curr.getSentText(), NE_TYPE_TO_EXTRACT);
    for (String entity : entities) {
      String entityNoSpaces = entity.replaceAll("\\s", "");
      GeneMention newMention = new GeneMention(arg0);
      int startIndex = sentTextNoSpaces.indexOf(entityNoSpaces);
      int endIndex = startIndex + entityNoSpaces.length();
      newMention.setSentenceId(curr.getSentId());
      newMention.setBeginAndEnd(startIndex, endIndex, sentTextNoSpaces);
      newMention.setCasProcessorId(ABNER_ID);
      newMention.setMentionText(entity);
      newMention.addToIndexes();
    }
  }

  /**
   * Initialize model file and the tagger.
   */
  public void initialize(UimaContext aContext) {
    this.a = new Tagger();
  }
}
