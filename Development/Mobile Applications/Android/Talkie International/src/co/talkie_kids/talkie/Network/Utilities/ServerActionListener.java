package co.talkie_kids.talkie.Network.Utilities;


public interface ServerActionListener {

	/** 
     * Post action. 
     *  
     * @param isSuccessful 
     *            the is http request successful 
     * @param json 
     *            the json 
     */
    void postAction(boolean isSuccessful, Object json);    
      
    /** 
     * Pre execute action. 
     */
    void preExecuteAction();   
} 