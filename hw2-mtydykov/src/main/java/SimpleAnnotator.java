import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.deiis.SentenceData;

/**
 * 
 * This is an annotator that uses Stanford POS tags to exract gene mentions.
 * 
 * @author mtydykov
 * 
 */

public class SimpleAnnotator extends JCasAnnotator_ImplBase {
  PosTagNamedEntityRecognizer recognizer = null;

  @Override
  public void initialize(UimaContext context){
    try {
      recognizer =  new PosTagNamedEntityRecognizer();
    } catch (ResourceInitializationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {

    FSIterator it = arg0.getAnnotationIndex(SentenceData.type).iterator();

    while (it.hasNext()) {
      SentenceData curr = (SentenceData) it.next();
      Map<Integer, Integer> spanMap = recognizer.getGeneSpans(curr.getSentText());
      for (Entry<Integer, Integer> pair : spanMap.entrySet()) {
        
        GeneMention mention = new GeneMention(arg0);
        mention.setBeginAndEnd(pair.getKey(), pair.getValue(), curr);
        mention.setMentionText(curr.getSentText().substring(pair.getKey(), pair.getValue()));
        mention.setSentenceId(curr.getSentId());
        mention.setCasProcessorId("SIMPLE");
        mention.setConfidence(0.5);
        mention.addToIndexes();
        
      }
    }
  }
}
