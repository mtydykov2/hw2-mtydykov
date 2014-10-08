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

import edu.cmu.deiis.types.SentenceData;
import edu.cmu.deiis.types.Annotation;
import edu.cmu.deiis.types.GeneMention;

/**
 * 
 * This is an annotator that uses LingPipe to extract gene mentions.
 * 
 * @author mtydykov
 * 
 */

public class LingPipeAnnotator extends JCasAnnotator_ImplBase {

  private static final String LINGPIPE_ID = "0";

  private final static String PARAM_MODEL_FILE = "ModelFileName";

  private Chunker chunker = null;

  @Override
  public void process(JCas arg0) throws AnalysisEngineProcessException {

    FSIterator it = arg0.getAnnotationIndex(SentenceData.type).iterator();
    while (it.hasNext()) {
      SentenceData curr = (SentenceData) it.next();
      Chunking chunkIterator = chunker.chunk(curr.getSentText().toCharArray(), 0, curr
              .getSentText().toCharArray().length);
      for (Chunk c : chunkIterator.chunkSet()) {
        GeneMention mention = new GeneMention(arg0);
        mention.setSentenceId(curr.getSentId());
        mention.setBeginAndEnd(c.start(), c.end(), curr.getSentText());
        mention.setMentionText(curr.getSentText().substring(c.start(), c.end()));
        mention.setSentenceId(curr.getSentId());
        mention.setCasProcessorId(LINGPIPE_ID);
        mention.addToIndexes();
      }
    }

  }

  /**
   * Load model file.
   */
  public void initialize(UimaContext aContext) {
    try {
      this.chunker = (Chunker) AbstractExternalizable.readResourceObject((String) aContext
              .getConfigParameterValue("PARAM_MODEL_FILE"));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
