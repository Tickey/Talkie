package co.talkie_kids.talkie.utilities.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class DownloadFileTask extends AsyncTask<String, Void, File> {

	private ServerResponseListener mServerResponseListener;

	public DownloadFileTask setServerResponseListener(
			ServerResponseListener serverResponseListener) {
		this.mServerResponseListener = serverResponseListener;

		return this;
	}

	@Override
	protected File doInBackground(String... params) {

		String fileUrl = params[0];
		String outputFileUri = params[1];

		if (!TextUtils.isEmpty(fileUrl)) {

			InputStream input = DownloadFileTask.getStreamFromURL(fileUrl);

			if (input != null) {
				return DownloadFileTask.saveInputStreamToStorage(input,
						outputFileUri);
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(File result) {
		super.onPostExecute(result);

		if (mServerResponseListener != null) {
			mServerResponseListener.postAction(result != null, result);
		}
	}

	public static File saveInputStreamToStorage(InputStream input,
			String filePath) {
		File file;
		try {
			file = new File(filePath);
			OutputStream output = new FileOutputStream(file);
			try {
				try {
					final byte[] buffer = new byte[1024];
					int read;

					while ((read = input.read(buffer)) != -1)
						output.write(buffer, 0, read);

					output.flush();
				} catch (Exception e) {
					file = null;
					e.printStackTrace();
				} finally {
					output.close();
				}
			} catch (Exception e) {
				file = null;
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			file = null;
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				file = null;
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * Downloads a file from the given URL, then decodes and returns an
	 * InputStream object
	 */
	public static InputStream getStreamFromURL(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();

			return input;

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
	}
}
