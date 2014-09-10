package co.talkie_kids.talkie.Network.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

/** 
 * Makes http requests with both POST and GET methods and  gives InputStream 
 * result or null. 
 * <p> 
 * NULL is returned when request timeout is left or response exception occur 
 */
public class HttpRequestHelper { 
      
    private static final String TAG = HttpRequestHelper.class.getSimpleName();

	/** The connection timeout in milliseconds. */
    private final int mConnectionTimeout = 10000; 
      
    /** The socket timeout in milliseconds. */
    private final int mSocketTimeout = 10000; 
      
    /** The url. */
    private String mUrl; 
      
    /** The request method type. */
    private String mRequestMethodType; 
      
    /** The request parameters. */
    private ArrayList<NameValuePair> mRequestParams;

	private String mJSONString = ""; 
      
    /** 
     * Instantiates a new http request. 
     *  
     * @param url 
     *            the url 
     * @param requestmethod 
     *            the method 
     * @param requestParams 
     *            the params 
     */
    public HttpRequestHelper(String url, String requestMethod, 
            ArrayList<NameValuePair> requestParams) { 
        this.mRequestParams = requestParams; 
        this.mUrl = url; 
        this.mRequestMethodType = requestMethod; 
    }
    
    public HttpRequestHelper(String url, String requestMethod, 
            String jsonString) { 
        this.mJSONString  = jsonString; 
        this.mUrl = url; 
        this.mRequestMethodType = requestMethod; 
    }
      
    /** 
     * Makes http request and handles its response 
     * @param isResponseRequested 
     *  
     * @return the http request result 
     */
    public InputStream getHttpRequestResult(boolean isResponseRequested) { 
        InputStream inputStream = null; 
        /** check for request method */
        if(mRequestMethodType == "POST"){ 
            try{
            	//readUrl(mUrl);
                  
                HttpPost request = new HttpPost(mUrl); 
                HttpParams httpParameters = new BasicHttpParams(); 
                 
                if( mJSONString == "") {
                    /** adds json application content type */
                    //request.addHeader("Content-Type", "application/json");
	                /** add params to the entity object */
	                HttpEntity inputEntity = new UrlEncodedFormEntity(mRequestParams);
		            
	                /** adds content type of the entity to the request */
	                request.addHeader(inputEntity.getContentType()); 
	                /** adds the eintities to the request */
	                request.setEntity(inputEntity);
	                
                } else {
                	Log.v(TAG, "json");
                    StringEntity se = new StringEntity(mJSONString);
                    se.setContentEncoding("UTF-8");
                    se.setContentType("application/json");
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Accept", "application/json");

                    request.setEntity(se);
                    Log.v("json", mJSONString);
                }
                /**  
                 * Set the timeout in milliseconds until a connection is established. 
                 * The default value is zero, that means the timeout is not used.  
                 */
                HttpConnectionParams.setConnectionTimeout(httpParameters, mConnectionTimeout); 
                /**  
                 * Set the default socket timeout (SO_TIMEOUT) 
                 * in milliseconds which is the timeout for waiting for data. 
                 */
                HttpConnectionParams.setSoTimeout(httpParameters, mSocketTimeout); 
                /** create object of DefaultHttpClient adds the parameters */
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                /** executes the request and gets the response */
                HttpResponse response = httpClient.execute(request);
                /** gets the response status line */
                StatusLine statusLine = response.getStatusLine(); 
                /** gets the status code from the statis line */
                int statusCode = statusLine.getStatusCode(); 
                /** 
                 * checks for status code.. status code needs to be 200 or 
                 * HttpStatus.SC_OK 
                 */
                if (statusCode == HttpStatus.SC_OK) {
                	Log.v("Status Line", Integer.toString(statusCode) + ": " + statusLine.getReasonPhrase());
                    if( isResponseRequested ) {
	                    /** get response entities */
	                    HttpEntity entity = response.getEntity(); 
	                    /**get inputstream of entities */
	                    inputStream = entity.getContent();
                    } else {
                    	inputStream =  new InputStream() {
							
							@Override
							public int read() throws IOException {
								return 0;
							}
						};
                    }
                } else {
                    Log.v("response", response.getEntity().getContent().toString());
                	Log.v("Status line", Integer.toString(statusCode) + ": " + statusLine.getReasonPhrase());
                    return null;
                }
                /** convert entity response to string */
           } catch (SocketException e) {
              e.printStackTrace(); 
             return null; 
          } catch (Exception e) { 
              e.printStackTrace(); 
             return null; 
          } 
      
        } else if(mRequestMethodType == "GET") { 
            try{ 
                  
                HttpGet request; 
                HttpParams httpParameters = new BasicHttpParams();
                
               if( mJSONString == "") {
            	   request = new HttpGet(mUrl);
                   /** adds json application content type */
                   request.addHeader("Content-Type", "application/json");
	                /** add params to the entity object */
	                HttpEntity inputEntity = new UrlEncodedFormEntity(mRequestParams); 
	              
	                /** adds content type of the entity to the request */
	                request.addHeader(inputEntity.getContentType());
               } else {
            	   mUrl += "?" + mJSONString;
            	   String encodedUrl = URLEncoder.encode(mUrl, "UTF-8");
            	   request = new HttpGet(encodedUrl);
                   request.setHeader("Content-Type", "application/json");
                   request.setHeader("Accept", "application/json");
               }
                /**  
                 * Set the timeout in milliseconds until a connection is established. 
                 * The default value is zero, that means the timeout is not used.  
                 */
                HttpConnectionParams.setConnectionTimeout(httpParameters, mConnectionTimeout); 
                /**  
                 * Set the default socket timeout (SO_TIMEOUT) 
                 * in milliseconds which is the timeout for waiting for data. 
                 */
                HttpConnectionParams.setSoTimeout(httpParameters, mSocketTimeout); 
                /** create object of DefaultHttpClient adds the parameters */
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters); 
                /** adds json application content type */
                request.addHeader("Content-Type", "application/json"); 
                  
                /** executes the request and gets the response */
                HttpResponse response = httpClient.execute(request); 
                /** gets the response status line */
                StatusLine statusLine = response.getStatusLine(); 
                /** gets the status code from the statis line */
                int statusCode = statusLine.getStatusCode(); 
                /** 
                 * checks for status code.. status code needs to be 200 or 
                 * HttpStatus.SC_OK 
                 */
                if (statusCode == HttpStatus.SC_OK) { 
                    if( isResponseRequested ) {
	                    /** get response entities */
	                    HttpEntity entity = response.getEntity(); 
	                    /**get inputstream of entities */
	                    inputStream = entity.getContent();
                    } else {
                    	inputStream =  new InputStream() {
							
							@Override
							public int read() throws IOException {
								return 0;
							}
						};
                    }
                } else {
                    Log.v("response", response.getEntity().getContent().toString());
                	Log.v("Status line", Integer.toString(statusCode) + ": " + statusLine.getReasonPhrase());
                	return null;
                }
            } catch (SocketException e) {
                e.printStackTrace(); 
               return null; 
            } catch (Exception e) { 
                e.printStackTrace(); 
               return null; 
            } 
        } 
      
        return inputStream; 
    }
}