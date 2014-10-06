import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import com.aliasi.matrix.DenseVector;
import com.aliasi.stats.LogisticRegression;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.deiis.types.GeneMention;
import edu.cmu.deiis.types.SentenceData;

/**
 * 
 * This is the merging component for intermediate annotators. It takes the output of all annotators
 * for a sentence and decides which annotations to keep based on the output of a logistic regression
 * classifier.
 * 
 * @author mtydykov
 * 
 */

public class Merger extends JCasAnnotator_ImplBase {

  public static final String MERGED = "MERGED";
  private LogisticRegression classifier = null;

  public void initialize(UimaContext u) {
    try {
      classifier = (LogisticRegression) AbstractExternalizable.readObject(new File("model"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator annotationIt = arg0.getAnnotationIndex(GeneMention.type).iterator();

    HashMap<HashMap<Integer, Integer>, GeneMention> tokensToAnnotations = new HashMap<HashMap<Integer, Integer>, GeneMention>();

    // Classify each mention.
    while (annotationIt.hasNext()) {
      GeneMention currentMention = (GeneMention) annotationIt.next();
      if (!currentMention.getCasProcessorId().equals(CollectionReader.GOLD)) {
        double[] classification = classifier.classify(new DenseVector(currentMention
                .getFeaturesArray().toArray()));
        if (classification[1] >= classification[0]) {
          HashMap<Integer, Integer> span = new HashMap<Integer, Integer>();
          span.put(currentMention.getBegin(), currentMention.getEnd());
          tokensToAnnotations.put(span, currentMention);
        }
      }
    }

    // For each mention that was kept, create a "merged" mention.
    for (HashMap<Integer, Integer> finalSpans : tokensToAnnotations.keySet()) {
      GeneMention mergedMention = new GeneMention(arg0);
      mergedMention.setCasProcessorId(MERGED);
      mergedMention.setSentenceId(tokensToAnnotations.get(finalSpans).getSentenceId());
      mergedMention.setBegin(tokensToAnnotations.get(finalSpans).getBegin());
      mergedMention.setEnd(tokensToAnnotations.get(finalSpans).getEnd());
      mergedMention.setMentionText(tokensToAnnotations.get(finalSpans).getMentionText());
      mergedMention.addToIndexes();
    }
  }
}
