import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;

import edu.cmu.deiis.types.GeneMention;

/**
 * 
 * This is a CAS consumer that writes extracted gene mentions to the file specified by the output
 * parameter.
 * 
 */

public class Output extends CasConsumer_ImplBase {

  private static final String PARAM_OUTDIR = "outputFileName";

  private File outputFile = null;

  private FileWriter writer = null;

  /**
   * Open file for writing results.
   * 
   * @param u
   */
  public void initialize() {
    String outputFileName = (String) getConfigParameterValue(PARAM_OUTDIR);
    outputFile = new File(outputFileName);
    try {
      writer = new FileWriter(outputFile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Write results to output file.
   */
  @Override
  public void processCas(CAS arg0) throws ResourceProcessException {

    JCas cas;
    try {
      cas = arg0.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    FSIterator it = cas.getAnnotationIndex(GeneMention.type).iterator();

    while (it.hasNext()) {
      GeneMention currMention = (GeneMention) it.next();
      if (currMention.getCasProcessorId().equals(Filterer.FINAL)) {
        try {
          writer.write(currMention.getSentenceId() + "|" + currMention.getBegin() + " "
                  + currMention.getEnd() + "|" + currMention.getMentionText() + "\n");
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Closer writer for writing results.
   */
  public void collectionProcessComplete() {
    try {
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
