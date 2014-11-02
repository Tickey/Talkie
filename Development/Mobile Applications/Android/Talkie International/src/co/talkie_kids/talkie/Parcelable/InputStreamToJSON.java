package co.talkie_kids.talkie.parcelable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class InputStreamToJSON { 
	  
    /** The input stream. */
    private String mJsonString = "";
  
    /** 
     * Constructor initializes InputStream. 
     *  
     * @param inputStream 
     *            the input stream 
     */
    public InputStreamToJSON(InputStream inputStream) {
        inputStreamToJSONString(inputStream);
    }

	private void inputStreamToJSONString(InputStream inputStream) {
		try {
            /** 
             * Buffer reader from InputStreamReader with standard browser 
             * charset and buffer size 8 for low memory devices 
             */
            BufferedReader reader = new BufferedReader(new InputStreamReader( 
                    inputStream, "iso-8859-1"), 8); 
            StringBuilder sb = new StringBuilder(); 
            String line = null; 
            /** 
             * read new line from reader until it returns null 
             */
            while ((line = reader.readLine()) != null) { 
                /** if not null add newline charecter */
                sb.append(line + "\n"); 
            } 
            inputStream.close(); 
            /** convert StringBuilder to string */
            mJsonString = sb.toString(); 
        } catch (Exception e) {
        	e.printStackTrace();
            Log.e("Buffer Error", "Error converting result " + e.toString()); 
        }
	}
	
	public String getStringResult() {
		return mJsonString;
	}
	
	public Boolean isJSONObject() {
		Boolean result = null;
		if( this.mJsonString != null )
			if( this.mJsonString != "") {
				if( mJsonString.charAt(0) == '{')
					result = true;
				else if(mJsonString.charAt(0) == '[')
					result = false;
			}
				
		return result;
	}
  
    /** 
     * @return the jSON object from input stream 
     */
    public JSONObject getJSONObjectFromInputStream() { 
        JSONObject jObj = null;
      
        /** try parse the string to a JSON object */
        try { 
            /** add the read string to JSONObject */
            jObj = new JSONObject(mJsonString); 
        } catch (JSONException e) { 
            Log.e("JSON Parser", "Error parsing data " + e.toString()); 
        } 
  
        return jObj; 
    }
  
    /** 
     * @return the jSON arrau from input stream 
     */
    public JSONArray getJSONArrayFromInputStream() { 
    	JSONArray jArray = null;
      
        /** try parse the string to a JSON object */
        try { 
            /** add the read string to JSONObject */
            jArray = new JSONArray(mJsonString); 
        } catch (JSONException e) { 
            Log.e("JSON Parser", "Error parsing data " + e.toString()); 
        } 
  
        return jArray; 
    }
} 
