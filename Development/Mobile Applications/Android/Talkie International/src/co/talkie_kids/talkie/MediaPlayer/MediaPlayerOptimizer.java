package co.talkie_kids.talkie.mediaplayer;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class MediaPlayerOptimizer {
	public static MediaPlayer create(Context context, int resid, boolean looping) {
		try {
			AssetFileDescriptor afd = context.getResources().openRawResourceFd(
					resid);
			MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			afd.close();

			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			if (looping)
				mp.setLooping(true);

			mp.prepare();
			return mp;
		} catch (IOException ex) {
			Log.d("MediaPlayer", "create failed:", ex);
			// fall through
		} catch (IllegalArgumentException ex) {
			Log.d("MediaPlayer", "create failed:", ex);
			// fall through
		} catch (SecurityException ex) {
			Log.d("MediaPlayer", "create failed:", ex);
			// fall through
		}
		return null;
	}
}
