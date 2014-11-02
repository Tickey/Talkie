package co.talkie_kids.talkie.resources;

import java.io.File;

import android.content.Context;
import android.util.Log;
import co.talkie_kids.talkie.network.utilities.DownloadFileTask;
import co.talkie_kids.talkie.network.utilities.ServerResponseListener;
import co.talkie_kids.talkie.utilities.StorageHelper;

public class FileLoader {
	
	private static final String APP_RESOURCES_PATH_NAME = "app_resources";

	private static final String TAG = FileLoader.class.getSimpleName();
	
	protected String mFilePath;
	protected Context mContext;
	
	public FileLoader(Context context) {

		File filePath = StorageHelper.getAbosoluteFile(APP_RESOURCES_PATH_NAME,
				context);
		
		filePath.mkdir();
		
		mFilePath =  filePath.getAbsolutePath();
		
		this.mContext = context;
	}

	public void cacheFile(String imageUrl,
			final ServerResponseListener responseListener ) {
		
		if(responseListener != null ) {
			responseListener.preExecuteAction();
		}
		
		String hashedImageName = StorageHelper.getHashedFileName(imageUrl);
		
		final String filePath = mFilePath + "/" + hashedImageName;
		
		if( !(new File(filePath).exists()) ) {
			DownloadFileTask downloadTask =  new DownloadFileTask();
			
			downloadTask.setServerResponseListener(responseListener);
			
			downloadTask.execute(imageUrl, filePath);
		} else {
			if(responseListener != null) {
				responseListener.postAction(true, null);
			}
		}
	}
}
