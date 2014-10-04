import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

/**
 * 
 * This is an annotator that scores extraction results based on the gold standard.
 * 
 * @author mtydykov
 * 
 */

public class Scorer extends JCasAnnotator_ImplBase {
  private HashMap<Integer, Integer> goldSpans;

  private int totalPresent = 0;

  private int totalRecall = 0;

  private int truePositives = 0;

  public void initialize(UimaContext u) {
    goldSpans = new HashMap<Integer, Integer>();
  }

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator itGeneMention = arg0.getAnnotationIndex(GeneMention.type).iterator();
    while (itGeneMention.hasNext()) {
      GeneMention currentMention = (GeneMention) itGeneMention.next();
      if (currentMention.getCasProcessorId().equals("GOLD")) {
        totalPresent++;
        goldSpans.put(currentMention.getBegin(), currentMention.getEnd());
      } else if (currentMention.getCasProcessorId().equals("FINAL")) {
        totalRecall++;
        if (goldSpans.containsKey(currentMention.getBegin())
                && goldSpans.get(currentMention.getBegin()) == currentMention.getEnd()) {
          truePositives++;
        }
      }
    }
  }

  public void batchProcessComplete() {
    double recall = this.truePositives / (double)(totalPresent);
    double precision = this.truePositives / (double)totalRecall;
    double f1score = 2 * (recall * precision) / (recall + precision);
    System.out.println("Recall :" + recall);
    System.out.println("Precision : " + precision);
    System.out.println("F1-Score : " + f1score);
  }

}
