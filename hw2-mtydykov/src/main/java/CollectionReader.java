import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;

import edu.cmu.deiis.types.GeneMention;
import edu.cmu.deiis.types.SentenceData;

/**
 * 
 * This is a collection reader that takes an input file to be processed as a parameter. If a gold
 * standard file is provided, this reader also loads gold standard annotations for each sentence.
 * 
 * @author mtydykov
 * 
 */

public class CollectionReader extends CollectionReader_ImplBase {

  public static final String GOLD = "GOLD";

  private File myFile;

  private File goldStandardFile;

  private int myCurrentIndex;

  private String[] sentences;

  private String text;

  private HashMap<String, ArrayList<String>> goldStandardSentsToAnnotations = new HashMap<String, ArrayList<String>>();

  public static final String PARAM_INPUTDIR = "InputDirectory";

  public static final String PARAM_GOLDDIR = "GoldDirectory";

  /**
   * Loads an input file to be tagged and an optional gold standard file.
   */
  public void initialize() {
    myFile = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)).trim());
    String goldStandardFileName = ((String) getConfigParameterValue(PARAM_GOLDDIR));
    if(goldStandardFileName != null){
      goldStandardFile = new File(goldStandardFileName.trim());
      String goldStandardText = null;
      try {
        goldStandardText = FileUtils.file2String(goldStandardFile);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      String[] goldStandardSents = goldStandardText.split("\n");

      for (String sent : goldStandardSents) {
        String[] parts = sent.split("\\|");
        String sentId = parts[0];
        if (!goldStandardSentsToAnnotations.containsKey(sentId)) {
          goldStandardSentsToAnnotations.put(sentId, new ArrayList<String>());
        }
        goldStandardSentsToAnnotations.get(sentId).add(sent);
      }
    }

    myCurrentIndex = 0;
    try {
      text = FileUtils.file2String(myFile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    sentences = text.split("\n");
  }

  /**
   * Loads sentence data for each sentence in the input file and also attaches gold standard
   * annotations for each sentence, if these are provided.
   */
  @Override
  public void getNext(CAS arg0) throws IOException, CollectionException {
    JCas myCas = null;
    try {
      myCas = arg0.getJCas();
    } catch (CASException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String sent = sentences[myCurrentIndex];
    myCas.setDocumentText(sentences[myCurrentIndex]);
    SentenceData data = new SentenceData(myCas);
    String id = sent.substring(0, sent.indexOf(" "));
    String sentText = sent.substring(sent.indexOf(" "));
    // add relevant gold standard mention annotation to this sentence's CAS
    if (goldStandardSentsToAnnotations.containsKey(id)) {
      for (String mention : goldStandardSentsToAnnotations.get(id)) {
        String[] parts = mention.split("\\|");
        String sentId = parts[0];
        String[] indeces = parts[1].split(" ");
        int begin = Integer.parseInt(indeces[0]);
        int end = Integer.parseInt(indeces[1]);
        String mentionText = parts[2];
        GeneMention goldMention = new GeneMention(myCas);
        goldMention.setSentenceId(sentId);
        goldMention.setBegin(begin);
        goldMention.setEnd(end);
        goldMention.setMentionText(mentionText);
        goldMention.setCasProcessorId(GOLD);
        goldMention.addToIndexes();
      }
    }
    data.setSentText(sentText.trim());
    data.setSentId(id);
    data.addToIndexes();
    myCurrentIndex++;
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return myCurrentIndex < sentences.length;
  }

}
