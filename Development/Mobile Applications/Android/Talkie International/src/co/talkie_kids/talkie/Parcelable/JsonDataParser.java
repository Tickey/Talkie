package co.talkie_kids.talkie.Parcelable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class JsonDataParser {

	private static final String JSON_LEVEL_FILENAME = "words_data.json";
	private static final String JSON_LEVEL_FILE_DEFAULT = "words_data_default.json";
	private static final String JSON_BOX_FILENAME = "boxes.json";
	private Context mContext;
	private static final String isUnlockedKey = "isUnlocked";
	private static final String isAnswerdCorrectKey = "isAnswerdCorrect";
	private static final String JSON_BOXES = "{ \"animals\" : false, \"veggies\" : false }";
	
	public JsonDataParser(Context context)
	{
		this.mContext = context;
		File file = this.mContext.getFileStreamPath(JsonDataParser.JSON_LEVEL_FILENAME);
		if(!file.exists())
		{
			createFile(null, JsonDataParser.JSON_LEVEL_FILENAME);
		}
	}
	
	public void setAsUnlocked(int position, String type)
	{
		writeValueInJson(position, type, true, JsonDataParser.isUnlockedKey);
	}
	
	public void setAsAnswerdCorrect(int position, String type)
	{
		writeValueInJson(position, type, true, JsonDataParser.isAnswerdCorrectKey);
	}

	private void writeValueInJson(int position, String type, boolean value,
			String key) {
		try {
            String result = getStringFromFile(JsonDataParser.JSON_LEVEL_FILENAME);
            
            //Log.v("result", result);
            
            JSONObject jobjHead = new JSONObject(result);
            
            JSONObject jobj = jobjHead.getJSONObject(type);
            
            //Log.v("jobj", jobj.toString());
            
            jobj = jobj.getJSONObject(Integer.toString(position+1));
            
            //Log.v("jobj", jobj.toString());
            
            jobj.put(key, value);
            
            createFile(jobjHead.toString(4), JsonDataParser.JSON_LEVEL_FILENAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public boolean isUnlocked(int position, String type) {
    	boolean ret = false;
    	
        ret = readJsonLevelValue(position, type, JsonDataParser.isUnlockedKey);
 
        return ret;
    }

    public boolean isBoxOpened(String key) {
    	boolean ret = false;

		File file = this.mContext.getFileStreamPath(JsonDataParser.JSON_BOX_FILENAME);
		if(!file.exists())
		{
			Log.v("boxes", JsonDataParser.JSON_BOXES);
			createFile(JsonDataParser.JSON_BOXES, JsonDataParser.JSON_BOX_FILENAME);
		}
		else
		{
            String result;
			try {
				result = getStringFromFile(JsonDataParser.JSON_BOX_FILENAME);

	            JSONObject jobj = new JSONObject(result);
	            
	            ret = jobj.getBoolean(key);
	            
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
 
        return ret;
    }

    public void setBoxOpened(String key) {
		File file = this.mContext.getFileStreamPath(JsonDataParser.JSON_BOX_FILENAME);
		if(!file.exists())
		{
			Log.v("boxes", JsonDataParser.JSON_BOXES);
			createFile(JsonDataParser.JSON_BOXES, JsonDataParser.JSON_BOX_FILENAME);
		}
		
		try {
            String result = getStringFromFile(JsonDataParser.JSON_BOX_FILENAME);
            
            //Log.v("result", result);
            
            JSONObject jobjHead = new JSONObject(result);
            
            jobjHead.put(key, true);
            
            createFile(jobjHead.toString(4), JsonDataParser.JSON_BOX_FILENAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public boolean isAnswerdCorrect(int position, String type) {
    	boolean ret = false;
    	
        ret = readJsonLevelValue(position, type, JsonDataParser.isAnswerdCorrectKey);
 
        return ret;
    }

	private boolean readJsonLevelValue(int position, String type, String key) {
		boolean ret = false;
		try {
            String result = getStringFromFile(JsonDataParser.JSON_LEVEL_FILENAME);
            
            //Log.v("result", result);
            
            JSONObject jobj = new JSONObject(result);
            
            jobj = jobj.getJSONObject(type);
            
            //Log.v("jobj", jobj.toString());
            
            jobj = jobj.getJSONObject(Integer.toString(position+1));
            
            //Log.v("jobj", jobj.toString());

    		ret = jobj.getBoolean(key);

        } catch (Exception e) {
            e.printStackTrace();
        }
		return ret;
	}

	public Integer getUnlockedAnimationsNumber(String type) {
		Integer ret = 0;
		try {
            String result = getStringFromFile(JsonDataParser.JSON_LEVEL_FILENAME);
            
            //Log.v("result", result);
            
            JSONObject jobj = new JSONObject(result);
            
           jobj = jobj.getJSONObject(type);
            
            //Log.v("jobj", jobj.toString());
            
            for(int index = 0 ; index < 9 ; index++)
            {
            	JSONObject jobj2 = jobj.getJSONObject(Integer.toString(index+1));
            
            	//Log.v("jobj", jobj.toString());

            	if( jobj2.getBoolean(JsonDataParser.isAnswerdCorrectKey) )
            		ret++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
		return ret/3;
	}
    
    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private String getStringFromFile (String filePath) throws Exception {

    	File file = this.mContext.getFileStreamPath(filePath);
		InputStream fin = new FileInputStream(file);

        //File fl = new File(filePath);
        //FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();        
        return ret;
    }

    private String getStringFromAssetsFile (String filePath) throws Exception {

		AssetManager assetManager = mContext.getAssets();
		InputStream fin =  assetManager.open(filePath);

        //File fl = new File(filePath);
        //FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();        
        return ret;
    }
    
	private boolean createFile(String json,String filename)
	{
		
		boolean isSuccessful = false;

		Log.v("LoginStorage", "createFile start");
		try {
			FileOutputStream fOut = this.mContext.openFileOutput(filename, Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			
			if(json == null )
			{
				json = getStringFromAssetsFile(JsonDataParser.JSON_LEVEL_FILE_DEFAULT);
			}
			
			osw.write(json);
			osw.flush();
			osw.close();
			isSuccessful = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	    	isSuccessful = false;
		} catch (IOException e) {
			e.printStackTrace();
	    	isSuccessful = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    	isSuccessful = false;
		}

		Log.v("LoginStorage", "createFile: " + Boolean.toString(isSuccessful));
		return isSuccessful;
	}
}