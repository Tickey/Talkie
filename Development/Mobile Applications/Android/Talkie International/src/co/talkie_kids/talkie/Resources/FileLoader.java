package co.talkie_kids.talkie.Resources;

import java.io.File;

import android.content.Context;
import co.talkie_kids.talkie.Network.Utilities.DownloadFileTask;
import co.talkie_kids.talkie.Network.Utilities.ServerResponseListener;
import co.talkie_kids.talkie.utilities.StorageHelper;

public class FileLoader {
	
	private static final String APP_RESOURCES_PATH_NAME = "app_resources";
	
	protected String mFilePath;
	protected Context mContext;
	
	public FileLoader(Context context) {

		mFilePath =  StorageHelper.getAbosoluteFile(APP_RESOURCES_PATH_NAME,
				context).getAbsolutePath();
		
		this.mContext = context;
	}

	public void cacheFile(String imageUrl,
			final ServerResponseListener responseListener ) {
		String hashedImageName = StorageHelper.getHashedFileName(imageUrl);
		
		//Log.v(TAG, "filePathName: " + mFilePath + hashedImageName );
		
		final String filePath = mFilePath + "/" + hashedImageName;
		
		if( !new File(mFilePath + hashedImageName).exists() ) {
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
