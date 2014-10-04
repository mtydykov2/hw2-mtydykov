import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

import edu.cmu.deiis.SentenceData;
import edu.cmu.deiis.types.Token;
import edu.stanford.nlp.util.ArrayUtils;

/**
 * 
 * This is an annotator that uses LingPipe to extract gene mentions.
 * 
 * @author mtydykov
 * 
 */

public class Trainer extends JCasAnnotator_ImplBase {
  
  private HashMap<Double[], Integer> featureStrings = new HashMap<Double[], Integer>();
  
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator annotationIt = arg0.getAnnotationIndex(GeneMention.type).iterator();
    FSIterator sentIterator = arg0.getAnnotationIndex(SentenceData.type).iterator();
    SentenceData data = null;
    while (sentIterator.hasNext()) {
      data = (SentenceData) sentIterator.next();
    }
    HashMap<HashMap<Integer, Integer>, ArrayList<GeneMention>> tokensToAnnotations = new HashMap<HashMap<Integer, Integer>, ArrayList<GeneMention>>();
    HashMap<Integer, Integer> correctTokenSpans = new HashMap<Integer, Integer>();
    HashMap<HashMap<Integer, Integer>, HashSet<String>> tokenSpansToAnnotators = 
            new HashMap<HashMap<Integer, Integer>, HashSet<String>>();
    // organize all annotations into maps by annotation span
    // TODO: need to somehow get actual token spans, not the entire annotation span
    while (annotationIt.hasNext()) {
      GeneMention currentMention = (GeneMention) annotationIt.next();
      String[] parts = currentMention.getMentionText().split("\\s");
      int slotsBefore = 0;
      for (int i = 0; i < parts.length; i++) {
        HashMap<Integer, Integer> beginToEnd = new HashMap<Integer, Integer>();
        int startIndex = currentMention.getBegin() + slotsBefore;
        int endIndex = startIndex + parts[i].length();
        if(i == parts.length - 1){
          endIndex--;
        }
        beginToEnd.put(startIndex, endIndex);
        if(currentMention.getCasProcessorId().equals("GOLD")){
          correctTokenSpans.putAll(beginToEnd);
        } else {
          if (!tokensToAnnotations.containsKey(beginToEnd)) {
            tokensToAnnotations.put(beginToEnd, new ArrayList<GeneMention>());
            tokenSpansToAnnotators.put(beginToEnd, new HashSet<String>());
          }
          if(!tokenSpansToAnnotators.get(beginToEnd).contains(currentMention.getCasProcessorId())){
            tokensToAnnotations.get(beginToEnd).add(currentMention);
            tokenSpansToAnnotators.get(beginToEnd).add(currentMention.getCasProcessorId()); 
          }
        }
        slotsBefore = parts[i].length();
      }
    }

    for (HashMap<Integer, Integer> tokenSpan : tokensToAnnotations.keySet()) {
      Double[] vals = new Double[]{0.0};
      for (int i = 0; i < tokensToAnnotations.get(tokenSpan).size(); i++) {
        vals[i] = tokensToAnnotations.get(tokenSpan).get(i).getConfidence();
      }
      int key = tokenSpan.keySet().iterator().next();
      int val = tokenSpan.values().iterator().next();
      if(correctTokenSpans.containsKey(key) && correctTokenSpans.get(key) == val){
        featureStrings.put(vals, 1);
      } else {
        featureStrings.put(vals, 0);
      }
    }
  }
  
  public void collectionProcessComplete(){
    Vector[] inputs = new Vector[this.featureStrings.size()];
    int[] outputsArray = new int[this.featureStrings.size()];
    int i = 0;
    for(Entry<Double[], Integer> entry: this.featureStrings.entrySet()){
      Double[] current = entry.getKey();
      double[] inputsArray = new double[entry.getKey().length];
      for(int j = 0; j < current.length; j++){
        inputsArray[j] = current[j];
      }
      inputs[i] = new DenseVector(inputsArray);
      outputsArray[i] = entry.getValue();
      i++;
    }
    
    LogisticRegression regression = LogisticRegression.estimate(inputs,
            outputsArray,
            RegressionPrior.noninformative(),
            AnnealingSchedule.inverse(.05,100),
            null, // null reporter        
            0.000000001, // min improve
            1, // min epochs
      10000);
    try {
      regression.compileTo(new ObjectOutputStream(new FileOutputStream(new File("model"))));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
}
