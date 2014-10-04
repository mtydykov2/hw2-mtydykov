

/* First created by JCasGen Tue Sep 30 23:34:17 EDT 2014 */
package edu.cmu.deiis;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Oct 04 14:18:29 EDT 2014
 * XML source: /root/git/hw2-mtydykov/hw2-mtydykov/src/main/resources/descriptors/deiis_types.xml
 * @generated */
public class SentenceData extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SentenceData.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SentenceData() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public SentenceData(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SentenceData(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SentenceData(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: sentId

  /** getter for sentId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSentId() {
    if (SentenceData_Type.featOkTst && ((SentenceData_Type)jcasType).casFeat_sentId == null)
      jcasType.jcas.throwFeatMissing("sentId", "edu.cmu.deiis.SentenceData");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SentenceData_Type)jcasType).casFeatCode_sentId);}
    
  /** setter for sentId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentId(String v) {
    if (SentenceData_Type.featOkTst && ((SentenceData_Type)jcasType).casFeat_sentId == null)
      jcasType.jcas.throwFeatMissing("sentId", "edu.cmu.deiis.SentenceData");
    jcasType.ll_cas.ll_setStringValue(addr, ((SentenceData_Type)jcasType).casFeatCode_sentId, v);}    
   
    
  //*--------------*
  //* Feature: sentText

  /** getter for sentText - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSentText() {
    if (SentenceData_Type.featOkTst && ((SentenceData_Type)jcasType).casFeat_sentText == null)
      jcasType.jcas.throwFeatMissing("sentText", "edu.cmu.deiis.SentenceData");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SentenceData_Type)jcasType).casFeatCode_sentText);}
    
  /** setter for sentText - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentText(String v) {
    if (SentenceData_Type.featOkTst && ((SentenceData_Type)jcasType).casFeat_sentText == null)
      jcasType.jcas.throwFeatMissing("sentText", "edu.cmu.deiis.SentenceData");
    jcasType.ll_cas.ll_setStringValue(addr, ((SentenceData_Type)jcasType).casFeatCode_sentText, v);}    
   
    
  //*--------------*
  //* Feature: geneMentions

  /** getter for geneMentions - gets 
   * @generated
   * @return value of the feature 
   */
  public FSList getGeneMentions() {
    if (SentenceData_Type.featOkTst && ((SentenceData_Type)jcasType).casFeat_geneMentions == null)
      jcasType.jcas.throwFeatMissing("geneMentions", "edu.cmu.deiis.SentenceData");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((SentenceData_Type)jcasType).casFeatCode_geneMentions)));}
    
  /** setter for geneMentions - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGeneMentions(FSList v) {
    if (SentenceData_Type.featOkTst && ((SentenceData_Type)jcasType).casFeat_geneMentions == null)
      jcasType.jcas.throwFeatMissing("geneMentions", "edu.cmu.deiis.SentenceData");
    jcasType.ll_cas.ll_setRefValue(addr, ((SentenceData_Type)jcasType).casFeatCode_geneMentions, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    