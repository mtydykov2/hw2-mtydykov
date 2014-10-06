package edu.cmu.deiis.types;


/* First created by JCasGen Tue Sep 30 23:45:12 EDT 2014 */

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.DoubleArray;


/** 
 * Updated by JCasGen Sun Oct 05 15:08:02 EDT 2014
 * XML source: /root/git/hw2-mtydykov/hw2-mtydykov/src/main/resources/descriptors/deiis_types.xml
 * @generated */
public class GeneMention extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GeneMention.class);
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
  protected GeneMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public GeneMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public GeneMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public GeneMention(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /**
   * Given the original begin and end indexes for an annotation,
   * convert these to non-whitespace indexes.
   * @param originalBegin
   * @param originalEnd
   * @param sentText
   */
  public void setBeginAndEnd(int originalBegin, int originalEnd, String sentText) {
    String before = sentText.substring(0, originalBegin);
    before = before.replaceAll("\\s+", "");
    String after = sentText.substring(0, originalEnd);
    after = after.replaceAll("\\s+", "");
    this.setBegin(before.length());
    this.setEnd(after.length()-1);
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
  //* Feature: sentenceId

  /** getter for sentenceId - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSentenceId() {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_sentenceId == null)
      jcasType.jcas.throwFeatMissing("sentenceId", "GeneMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GeneMention_Type)jcasType).casFeatCode_sentenceId);}
    
  /** setter for sentenceId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentenceId(String v) {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_sentenceId == null)
      jcasType.jcas.throwFeatMissing("sentenceId", "GeneMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((GeneMention_Type)jcasType).casFeatCode_sentenceId, v);}    
   
    
  //*--------------*
  //* Feature: mentionText

  /** getter for mentionText - gets 
   * @generated
   * @return value of the feature 
   */
  public String getMentionText() {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_mentionText == null)
      jcasType.jcas.throwFeatMissing("mentionText", "GeneMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GeneMention_Type)jcasType).casFeatCode_mentionText);}
    
  /** setter for mentionText - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMentionText(String v) {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_mentionText == null)
      jcasType.jcas.throwFeatMissing("mentionText", "GeneMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((GeneMention_Type)jcasType).casFeatCode_mentionText, v);}    
   
    
  //*--------------*
  //* Feature: tokens

  /** getter for tokens - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getTokens() {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "GeneMention");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_tokens)));}
    
  /** setter for tokens - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTokens(FSArray v) {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "GeneMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_tokens, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for tokens - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public TOP getTokens(int i) {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "GeneMention");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_tokens), i);
    return (TOP)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_tokens), i)));}

  /** indexed setter for tokens - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setTokens(int i, TOP v) { 
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_tokens == null)
      jcasType.jcas.throwFeatMissing("tokens", "GeneMention");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_tokens), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_tokens), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: featuresArray

  /** getter for featuresArray - gets 
   * @generated
   * @return value of the feature 
   */
  public DoubleArray getFeaturesArray() {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_featuresArray == null)
      jcasType.jcas.throwFeatMissing("featuresArray", "GeneMention");
    return (DoubleArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_featuresArray)));}
    
  /** setter for featuresArray - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFeaturesArray(DoubleArray v) {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_featuresArray == null)
      jcasType.jcas.throwFeatMissing("featuresArray", "GeneMention");
    jcasType.ll_cas.ll_setRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_featuresArray, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for featuresArray - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public double getFeaturesArray(int i) {
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_featuresArray == null)
      jcasType.jcas.throwFeatMissing("featuresArray", "GeneMention");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_featuresArray), i);
    return jcasType.ll_cas.ll_getDoubleArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_featuresArray), i);}

  /** indexed setter for featuresArray - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setFeaturesArray(int i, double v) { 
    if (GeneMention_Type.featOkTst && ((GeneMention_Type)jcasType).casFeat_featuresArray == null)
      jcasType.jcas.throwFeatMissing("featuresArray", "GeneMention");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_featuresArray), i);
    jcasType.ll_cas.ll_setDoubleArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((GeneMention_Type)jcasType).casFeatCode_featuresArray), i, v);}
  }



    