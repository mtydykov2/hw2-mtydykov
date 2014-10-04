import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.deiis.SentenceData;
import edu.cmu.deiis.types.Annotation;

/**
 * 
 * This is an annotator that uses LingPipe to extract gene mentions.
 * 
 * @author mtydykov
 * 
 */

public class LingPipeAnnotator extends JCasAnnotator_ImplBase {

  private File modelFile = null;

  private final static String PARAM_MODEL_FILE = "ModelFileName";
  private ConfidenceChunker chunker = null;
  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {

    FSIterator it = arg0.getAnnotationIndex(SentenceData.type).iterator();
    if (modelFile != null) {
      while (it.hasNext()) {
        SentenceData curr = (SentenceData) it.next();
        Iterator<Chunk> chunkIterator = chunker.nBestChunks(curr.getSentText().toCharArray(), 0,
                curr.getSentText().toCharArray().length, 1);
        while (chunkIterator.hasNext()) {
          Chunk c = chunkIterator.next();
          GeneMention mention = new GeneMention(arg0);
          mention.setBeginAndEnd(c.start(), c.end(), curr);
          mention.setMentionText(curr.getSentText().substring(c.start(),
                  c.end()));
          mention.setSentenceId(curr.getSentText());
          mention.setConfidence(Math.pow(2, c.score()));
          mention.setCasProcessorId("LING");
          mention.addToIndexes();
        }
      }
    }
  }

  public void initialize(UimaContext aContext) {
    this.modelFile = new File((String) aContext.getConfigParameterValue(PARAM_MODEL_FILE));
    try {
      this.chunker = (ConfidenceChunker) AbstractExternalizable
              .readObject(modelFile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
