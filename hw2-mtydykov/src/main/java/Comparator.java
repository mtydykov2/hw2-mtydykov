import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import com.aliasi.classify.ConditionalClassifier;
import com.aliasi.classify.LogisticRegressionClassifier;
import com.aliasi.matrix.DenseVector;
import com.aliasi.matrix.Vector;
import com.aliasi.stats.LogisticRegression;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.deiis.SentenceData;
import edu.cmu.deiis.types.Token;

/**
 * 
 * This is an annotator that uses LingPipe to extract gene mentions.
 * 
 * @author mtydykov
 * 
 */

public class Comparator extends JCasAnnotator_ImplBase {

  private LogisticRegression classifier = null;

  public void initialize(UimaContext u) {
    try {
      classifier = (LogisticRegression) AbstractExternalizable.readObject(new File(
              "model"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator annotationIt = arg0.getAnnotationIndex(GeneMention.type).iterator();
    FSIterator sentIterator = arg0.getAnnotationIndex(SentenceData.type).iterator();
    SentenceData data = null;
    while (sentIterator.hasNext()) {
      data = (SentenceData) sentIterator.next();
    }
    HashMap<HashMap<Integer, Integer>, ArrayList<GeneMention>> tokensToAnnotations = new HashMap<HashMap<Integer, Integer>, ArrayList<GeneMention>>();
    HashMap<HashMap<Integer, Integer>, HashSet<String>> tokenSpansToAnnotators = new HashMap<HashMap<Integer, Integer>, HashSet<String>>();

    while (annotationIt.hasNext()) {
      GeneMention currentMention = (GeneMention) annotationIt.next();
      String[] parts = currentMention.getMentionText().split("\\s");
      int slotsBefore = 0;
      for (int i = 0; i < parts.length; i++) {
        HashMap<Integer, Integer> beginToEnd = new HashMap<Integer, Integer>();
        int startIndex = currentMention.getBegin() + slotsBefore;
        int endIndex = startIndex + parts[i].length();
        if (i == parts.length - 1) {
          endIndex--;
        }
        beginToEnd.put(startIndex, endIndex);
        if (!tokensToAnnotations.containsKey(beginToEnd)) {
          tokensToAnnotations.put(beginToEnd, new ArrayList<GeneMention>());
          tokenSpansToAnnotators.put(beginToEnd, new HashSet<String>());
        }
        if (!tokenSpansToAnnotators.get(beginToEnd).contains(currentMention.getCasProcessorId()) &&
                !currentMention.getCasProcessorId().equals("GOLD")) {
          tokensToAnnotations.get(beginToEnd).add(currentMention);
          tokenSpansToAnnotators.get(beginToEnd).add(currentMention.getCasProcessorId());
        }

        slotsBefore = parts[i].length();
      }
    }

    TreeMap<Integer, Integer> tokenSpanToClassification = new TreeMap<Integer, Integer>();
    for (HashMap<Integer, Integer> tokenSpan : tokensToAnnotations.keySet()) {
      double[] vecFeatures = new double[1];
      for (int i = 0; i < tokensToAnnotations.get(tokenSpan).size(); i++) {
        vecFeatures[i] = tokensToAnnotations.get(tokenSpan).get(i).getConfidence();
      }
      Vector features = new DenseVector(vecFeatures);
      double[] classification = classifier.classify(features);
      if (classification[1] > classification[0]) {
        tokenSpanToClassification.putAll(tokenSpan);
      }
    }

    Iterator<Map.Entry<Integer, Integer>> spanIterator = tokenSpanToClassification.entrySet()
            .iterator();
    GeneMention finalMention = new GeneMention(arg0);
    while (spanIterator.hasNext()) {
      Map.Entry<Integer, Integer> entry = spanIterator.next();
      int begin = entry.getKey();
      finalMention.setBegin(begin);
      int end = entry.getValue();
      finalMention.setCasProcessorId("FINAL");
      finalMention.setEnd(end);
      Iterator<Map.Entry<Integer, Integer>> nextIterator = tokenSpanToClassification.subMap(begin+1, tokenSpanToClassification.lastKey()+1).entrySet().iterator();
      // advance
      boolean addNew = false;
      while (nextIterator.hasNext() && !addNew) {
        Map.Entry<Integer, Integer> nextEntry = nextIterator.next();
        int nextBegin = nextEntry.getKey();
        int nextEnd = nextEntry.getValue();
        if (end != nextBegin || !nextIterator.hasNext()) {
          addNew = true;
          finalMention.setEnd(nextEnd);
        }
      }
      finalMention.addToIndexes();
      finalMention = new GeneMention(arg0);
    }
  }

  public void setBeginAndEnd(int originalBegin, int originalEnd, SentenceData original,
          GeneMention currMention) {
    String before = original.getSentText().substring(0, originalBegin);
    before = before.replaceAll("\\s+", "");
    String after = original.getSentText().substring(0, originalEnd);
    after = after.replaceAll("\\s+", "");
    currMention.setBegin(before.length());
    currMention.setEnd(after.length());
  }
}
