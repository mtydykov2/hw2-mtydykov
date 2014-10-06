import java.util.HashMap;
import java.util.HashSet;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.DoubleArray;

import edu.cmu.deiis.types.GeneMention;
import edu.cmu.deiis.types.SentenceData;

/**
 * 
 * This is class trains a linear regression classifier to decide which gene mentions to keep from
 * various annotators.
 * 
 * @author mtydykov
 * 
 */

public class FeatureExtractor extends JCasAnnotator_ImplBase {

  private final static String[] ANNOTATORS = { "LING", "ABNER" };

  private final static int NUM_FEATURES = ANNOTATORS.length + 2;

  /**
   * This annotator extracts features for each mention for the given sentence.
   */
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {
    FSIterator annotationIt = arg0.getAnnotationIndex(GeneMention.type).iterator();
    FSIterator sentIterator = arg0.getAnnotationIndex(SentenceData.type).iterator();
    SentenceData data = (SentenceData) sentIterator.next();

    HashMap<GeneMention, HashSet<GeneMention>> tokensToOverlaps = new HashMap<GeneMention, HashSet<GeneMention>>();
    HashSet<GeneMention> correctMentions = new HashSet<GeneMention>();
    // for each gene annotation, find # of other annotatons that overlapped
    while (annotationIt.hasNext()) {
      GeneMention currMention = (GeneMention) annotationIt.next();
      if (!currMention.getCasProcessorId().equals(CollectionReader.GOLD)) {
        if (!tokensToOverlaps.containsKey(currMention)) {
          tokensToOverlaps.put(currMention, new HashSet<GeneMention>());
        }
        FSIterator annotationIt2 = arg0.getAnnotationIndex(GeneMention.type).iterator();
        while (annotationIt2.hasNext()) {
          GeneMention currOther = (GeneMention) annotationIt2.next();
          // make sure not to add the same mention to overlaps map
          if (!currOther.equals(currMention)) {
            if (currOther.getCasProcessorId().equals(CollectionReader.GOLD)) {
              if (currMention.getBegin() == currOther.getBegin()
                      && currMention.getEnd() == currOther.getEnd()) {
                correctMentions.add(currMention);
              }
            } else {
              if (currOther.getBegin() >= currMention.getBegin()
                      && currMention.getEnd() > currOther.getBegin()
                      || currOther.getEnd() <= currMention.getEnd()
                      && currMention.getBegin() < currOther.getEnd()) {
                tokensToOverlaps.get(currMention).add(currOther);
              }
            }
          }
        }
      }
    }

    // keep track of which annotators voted for this annotation.
    for (GeneMention mention : tokensToOverlaps.keySet()) {
      mention.setFeaturesArray(new DoubleArray(arg0, NUM_FEATURES));
      // record the fact that the particular annotator voted for this mention
      mention.setFeaturesArray(Integer.parseInt(mention.getCasProcessorId()), 1.0);
      for (GeneMention other : tokensToOverlaps.get(mention)) {
        mention.setFeaturesArray(Integer.parseInt(other.getCasProcessorId()), 1.0);
      }
      int featIndex = ANNOTATORS.length;
      
      // check whether or not the mention contains any undesirable terms
      double containsBadTerms = 0.0;
      if (BadTermChecker.containsBadTerms(mention.getMentionText())) {
        containsBadTerms = 1.0;
      }
      mention.setFeaturesArray(featIndex, containsBadTerms);
      featIndex ++;
      
      // check whether or not there is a perfect overlap 
      // amongst all mentions that overlap
      boolean perfectOverlap = true;
      for (GeneMention other : tokensToOverlaps.get(mention)) {
        if(other.getBegin() != mention.getBegin() || other.getEnd() != mention.getEnd()){
          perfectOverlap = false;
        }
      }
      if(perfectOverlap){
        mention.setFeaturesArray(featIndex, 1.0);
      } else {
        mention.setFeaturesArray(featIndex, 0.0);
      }
    }
  }
}
