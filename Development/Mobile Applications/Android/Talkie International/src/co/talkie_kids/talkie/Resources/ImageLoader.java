package co.talkie_kids.talkie.resources;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import co.talkie_kids.talkie.network.utilities.DownloadFileTask;
import co.talkie_kids.talkie.network.utilities.ServerResponseListener;
import co.talkie_kids.talkie.utilities.StorageHelper;

public class ImageLoader extends FileLoader {
	
	private static final String TAG = ImageLoader.class.getSimpleName();
	
	public ImageLoader(Context context) {
		super(context);
	}

	public void loadImage(String imageUrl,
			final ServerResponseListener responseListener ) {
		String hashedImageName = StorageHelper.getHashedFileName(imageUrl);
		
		final String filePath = mFilePath + hashedImageName;
		
		Log.v(TAG, "filePath: " + filePath );
		
		if( !new File(mFilePath + hashedImageName).exists() ) {
			DownloadFileTask downloadTask =  new DownloadFileTask();
			/*
			ServerResponseListener serverResponseListener =
					new ServerResponseListener() {
				
						@Override
						public void preExecuteAction() {
							if(responseListener != null) {
								responseListener.preExecuteAction();
							}
						}
						
						@Override
						public void postAction(boolean isSuccessful, Object result) {
							
							InputStream input = (InputStream) result;
							
							if (isSuccessful) {
								isSuccessful = FileLoader.saveInputStreamToStorage(
										input, filePath);
							}
							
							if(responseListener != null) {
								
								Bitmap bitmap = inputStreamToBitmap(input);
								
								responseListener.postAction(isSuccessful, bitmap);
							}
						}
					};
			downloadTask.setServerResponseListener(serverResponseListener);
			*/
			
			downloadTask.execute(imageUrl);
		} else {
			if(responseListener != null) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
				
				responseListener.postAction(true, bitmap);
			}
		}
	}

    private Bitmap inputStreamToBitmap(InputStream input) {
    	return BitmapFactory.decodeStream(input);
    }
}
