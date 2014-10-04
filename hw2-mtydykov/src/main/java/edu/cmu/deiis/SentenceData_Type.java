
/* First created by JCasGen Tue Sep 30 23:34:17 EDT 2014 */
package edu.cmu.deiis;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sat Oct 04 14:18:29 EDT 2014
 * @generated */
public class SentenceData_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SentenceData_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SentenceData_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SentenceData(addr, SentenceData_Type.this);
  			   SentenceData_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SentenceData(addr, SentenceData_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SentenceData.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.deiis.SentenceData");
 
  /** @generated */
  final Feature casFeat_sentId;
  /** @generated */
  final int     casFeatCode_sentId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSentId(int addr) {
        if (featOkTst && casFeat_sentId == null)
      jcas.throwFeatMissing("sentId", "edu.cmu.deiis.SentenceData");
    return ll_cas.ll_getStringValue(addr, casFeatCode_sentId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSentId(int addr, String v) {
        if (featOkTst && casFeat_sentId == null)
      jcas.throwFeatMissing("sentId", "edu.cmu.deiis.SentenceData");
    ll_cas.ll_setStringValue(addr, casFeatCode_sentId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_sentText;
  /** @generated */
  final int     casFeatCode_sentText;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSentText(int addr) {
        if (featOkTst && casFeat_sentText == null)
      jcas.throwFeatMissing("sentText", "edu.cmu.deiis.SentenceData");
    return ll_cas.ll_getStringValue(addr, casFeatCode_sentText);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSentText(int addr, String v) {
        if (featOkTst && casFeat_sentText == null)
      jcas.throwFeatMissing("sentText", "edu.cmu.deiis.SentenceData");
    ll_cas.ll_setStringValue(addr, casFeatCode_sentText, v);}
    
  
 
  /** @generated */
  final Feature casFeat_geneMentions;
  /** @generated */
  final int     casFeatCode_geneMentions;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getGeneMentions(int addr) {
        if (featOkTst && casFeat_geneMentions == null)
      jcas.throwFeatMissing("geneMentions", "edu.cmu.deiis.SentenceData");
    return ll_cas.ll_getRefValue(addr, casFeatCode_geneMentions);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGeneMentions(int addr, int v) {
        if (featOkTst && casFeat_geneMentions == null)
      jcas.throwFeatMissing("geneMentions", "edu.cmu.deiis.SentenceData");
    ll_cas.ll_setRefValue(addr, casFeatCode_geneMentions, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public SentenceData_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_sentId = jcas.getRequiredFeatureDE(casType, "sentId", "uima.cas.String", featOkTst);
    casFeatCode_sentId  = (null == casFeat_sentId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentId).getCode();

 
    casFeat_sentText = jcas.getRequiredFeatureDE(casType, "sentText", "uima.cas.String", featOkTst);
    casFeatCode_sentText  = (null == casFeat_sentText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentText).getCode();

 
    casFeat_geneMentions = jcas.getRequiredFeatureDE(casType, "geneMentions", "uima.cas.FSList", featOkTst);
    casFeatCode_geneMentions  = (null == casFeat_geneMentions) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_geneMentions).getCode();

  }
}



    