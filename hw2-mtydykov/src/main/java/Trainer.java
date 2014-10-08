import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import com.aliasi.matrix.DenseVector;
import com.aliasi.matrix.Vector;
import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.LogisticRegression;
import com.aliasi.stats.RegressionPrior;

import edu.cmu.deiis.types.GeneMention;

/**
 * 
 * This is class trains a linear regression classifier to decide which gene mentions to keep from
 * various annotators.
 * 
 * @author mtydykov
 * 
 */

public class Trainer extends JCasAnnotator_ImplBase {


  private static final int INT_INCORRECT = 0;

  private static final int INT_CORRECT = 1;

  private HashMap<double[], Integer> featureStrings = new HashMap<double[], Integer>();

  private String modelFileName = "/root/git/hw2-mtydykov/hw2-mtydykov/src/main/resources/models/myModel";
  private File modelFile = null;

  // initialize model file to write to
  public void initialize(UimaContext u){
    modelFile = new File(modelFileName);
  }
  
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator annotationIt = arg0.getAnnotationIndex(GeneMention.type).iterator();
    // 1. for each gene annotation, find # of annotators that overlapped
    // 2. for each gene annotation, for each overlapping annotation look @ confidence of annotator
    while (annotationIt.hasNext()) {
      GeneMention currMention = (GeneMention) annotationIt.next();
      if (!currMention.getCasProcessorId().equals(CollectionReader.GOLD)) {
        FSIterator annotationIt2 = arg0.getAnnotationIndex(GeneMention.type).iterator();
        boolean isCorrect = false;
        while (annotationIt2.hasNext()) {
          GeneMention currOther = (GeneMention) annotationIt2.next();
          // make sure not to add the same mention to overlaps map
          if (!currOther.equals(currMention)) {
            if (currOther.getCasProcessorId().equals(CollectionReader.GOLD)) {
              if (currMention.getBegin() == currOther.getBegin()
                      && currMention.getEnd() == currOther.getEnd()) {
                isCorrect = true;
              }
            }
          }
        }
        if (isCorrect) {
          featureStrings.put(currMention.getFeaturesArray().toArray(), INT_CORRECT);
        } else {
          featureStrings.put(currMention.getFeaturesArray().toArray(), INT_INCORRECT);
        }
      }
    }
  }

  /**
   * Perform the final model training once all sentences have been processed.
   */
  public void collectionProcessComplete() {
    Vector[] inputs = new Vector[this.featureStrings.size()];
    int[] outputsArray = new int[this.featureStrings.size()];
    int i = 0;
    for (Entry<double[], Integer> entry : this.featureStrings.entrySet()) {
      double[] current = entry.getKey();
      inputs[i] = new DenseVector(current);
      outputsArray[i] = entry.getValue();
      i++;
    }

    LogisticRegression regression = LogisticRegression.estimate(inputs, outputsArray,
            RegressionPrior.noninformative(), AnnealingSchedule.inverse(.05, 100), null, // null
                                                                                         // reporter
            0.000000001, // min improve
            1, // min epochs
            10000);
    try {
      regression.compileTo(new ObjectOutputStream(new FileOutputStream(modelFile)));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
