package co.talkie_kids.talkie.utilities;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class StorageHelper {

	public static File getAbosoluteFile(String relativePath, Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return new File(context.getExternalFilesDir(null), relativePath);
		} else {
			return new File(context.getFilesDir(), relativePath);
		}
	}

	public static String getHashedFileName(String fileName) {
		if (!TextUtils.isEmpty(fileName)) {
			return String.valueOf(fileName.hashCode());
		} else {
			return null;
		}
	}
}
