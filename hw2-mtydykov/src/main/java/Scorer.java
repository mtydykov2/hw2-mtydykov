import java.util.HashMap;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.GeneMention;

/**
 * 
 * This is a scoring module that accumulates system scores per sentence based on the gold standard.
 * 
 * @author mtydykov
 * 
 */

public class Scorer extends JCasAnnotator_ImplBase {

  private int totalPresent = 0;

  private int totalRecall = 0;

  private int truePositives = 0;

  /**
   * Accumulate true positives, total recall and total present for the given sentence.
   */
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    HashMap<Integer, Integer> goldSpans = new HashMap<Integer, Integer>();
    FSIterator itGeneMention = arg0.getAnnotationIndex(GeneMention.type).iterator();
    while (itGeneMention.hasNext()) {
      GeneMention currentMention = (GeneMention) itGeneMention.next();
      if (currentMention.getCasProcessorId().equals(CollectionReader.GOLD)) {
        totalPresent++;
        goldSpans.put(currentMention.getBegin(), currentMention.getEnd());
      } else if (currentMention.getCasProcessorId().equals(Filterer.FINAL)) {
        totalRecall++;
        if (goldSpans.containsKey(currentMention.getBegin())
                && goldSpans.get(currentMention.getBegin()) == currentMention.getEnd()) {
          truePositives++;
        }
      }
    }
  }

  /**
   * Once all sentences have been processed, calculate final precision, recall, and f1-score and
   * print.
   */
  @Override
  public void collectionProcessComplete() {
    double recall = this.truePositives / (double) (totalPresent);
    double precision = this.truePositives / (double) totalRecall;
    double f1score = 2 * (recall * precision) / (recall + precision);
    System.out.println("Recall :" + recall);
    System.out.println("Precision : " + precision);
    System.out.println("F1-Score : " + f1score);
  }

}
