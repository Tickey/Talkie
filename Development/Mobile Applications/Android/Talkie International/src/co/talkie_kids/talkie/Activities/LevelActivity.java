package co.talkie_kids.talkie.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.data.Animals;
import co.talkie_kids.talkie.data.FruitsAndVeggies;
import co.talkie_kids.talkie.data.models.TalkieObject;
import co.talkie_kids.talkie.mediaplayer.MediaPlayerOptimizer;
import co.talkie_kids.talkie.parcelable.JsonDataParser;

public class LevelActivity extends BaseTalkieActivity {

   public static final String TAG = LevelActivity.class.getSimpleName();
   private SpeechRecognizer mSpeechRecognizer;
   private int mIndex;
   private TalkieObject mTalkieObj;
   private WordRecognitionListener mWordRecognitionListener;
   private Set<MediaPlayer> mMediaPlayers;
   private boolean mIsPaused = false;
   protected boolean mMicAvailable =  false;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.level);
	    
	    BaseTalkieActivity.mMediaPlayer.pause();
	    
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
	    Intent i = getIntent();
	
	    int index = i.getIntExtra("position", 0);
	    if(savedInstanceState != null)
		    if(savedInstanceState.getInt("index") != 0)
		    	index = savedInstanceState.getInt("index");
	    
	    String key = i.getStringExtra("key");
	    
	    if( key.equalsIgnoreCase(FruitsAndVeggies.FRU_AND_VEGGY_KEY)  ) {
	    	mTalkieObj = new FruitsAndVeggies();
	    } else {
	    	mTalkieObj = new Animals();
	    }
	    
	    this.mIndex = index;
	    
	    if(key != null ) {
	        int startPosition = 0;
	        
	    	if( index > 5) {
	    		startPosition = 6;
	    	} else 
	    		if( index > 2 ) {
	    			startPosition = 3;
	    		}
	
	        JsonDataParser jpars = new JsonDataParser(this);
	        
	        for(int ind = startPosition ; ind < startPosition + 3 ; ind++) {
	        	if( jpars.isAnswerdCorrect(ind, key)) {
	        		ImageView star;
	        		
	        		switch(ind-startPosition) {
	        			case 1:
	        				star = (ImageView) findViewById(R.id.star_2);
	        				break;
	        			case 2:
	        				star = (ImageView) findViewById(R.id.star_3);
	        				break;
	        			default:
	        				star = (ImageView) findViewById(R.id.star_1);
	        				break;
	        		}
	        		
	        		star.setImageResource(R.drawable.star_on);
	        	}
	        }
	    }
	
	    ImageView animal = (ImageView) findViewById(R.id.animal);
	    
	    animal.setImageResource(mTalkieObj.getDrawable(this.mIndex));
	    
	    mMediaPlayers = new HashSet<MediaPlayer>();
		
	    startSound(mTalkieObj.getSound(this.mIndex), mOnFirstPlayerCompletionListener);
	    
	    ((RelativeLayout) findViewById(R.id.rlMic)).setOnClickListener(mOnMicClickListener);
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		final Integer index = Integer.valueOf(mIndex);
		
		outState.putInt("index", index);
	}
   
   /*
   @Override
   protected void onRestoreInstanceState(Bundle savedState) {
	   Integer index = savedState.getInt("index");
	   
	   this.mIndex = index;
	}
   */
   
   class WordRecognitionListener implements RecognitionListener {
	   boolean flag = false;
	   private int mMistakesCount = 0;
	   TalkieObject obj;
	   private ImageView mic = (ImageView) findViewById(R.id.mic);
	   
	   //private static final String TAG = "-WordRecognitionListener";
		
	    private boolean isCorrect = false;
        public WordRecognitionListener(int index, TalkieObject obj) {
        	super();
        	this.obj = obj;
		}
        
		@Override
		public void onReadyForSpeech(Bundle params) {
                 //Log.d(TAG, "onReadyForSpeech");
			mMicAvailable = false;
			mic.setImageResource(R.drawable.mic_on);
        }
		
        @Override
		public void onBeginningOfSpeech() {
        	Log.d(TAG, "onBeginningOfSpeech");
        }
        
        private short mDbLevel = 0 ;
        
        @Override
		public void onRmsChanged(float rmsdB) {
        	short newDbLevel;
        	
        	if( rmsdB < -2 || rmsdB > 10)
        		Log.v(TAG, "rmsdB: " + rmsdB);

            float quiet_max = 1.92f;
            float medium_max = 5.96f;

            if (rmsdB < quiet_max) {
            	newDbLevel = 0;
                // quiet
            } else if (rmsdB >= quiet_max && rmsdB < medium_max) {
                // medium
                newDbLevel = 1;
            } else {
                // loud
                newDbLevel = 2;
            }
            
        	/*
        	short newDbLevel;
        	if( rmsdB < - 75 ) {
        		newDbLevel = 0;
        	} else if( rmsdB < -30 ) {
        		newDbLevel = 1;
        	} else if(rmsdB < 15) {
        		newDbLevel = 2;
        	} else {
				newDbLevel = 3;
			}
        	*/
        	
        	if( mDbLevel != newDbLevel ) {
        		mDbLevel = newDbLevel;
        		
        		switch (mDbLevel) {
        		case 0:
                    Log.v(TAG, "Quite: " + rmsdB);
        			break;
        		case 1:
                    Log.v(TAG, "Medium: " + rmsdB);
        			break;
        		case 2:
                    Log.v(TAG, "Loud: " + rmsdB);
        			break;
        		}
        	}
        }
        
        @Override
		public void onBufferReceived(byte[] buffer) {
        	Log.d(TAG, "onBufferReceived: ");
        }
        
        @Override
		public void onEndOfSpeech() {
                 //Log.d(TAG, "onEndofSpeech");
			mic.setImageResource(R.drawable.mic_off);
        }

        @Override
		public void onError(int error) {
			mic.setImageResource(R.drawable.mic_off);
        	
        	switch(error) {
        	case SpeechRecognizer.ERROR_CLIENT:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_CLIENT");
        		break;
        	case SpeechRecognizer.ERROR_AUDIO:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_AUDIO");
        		break;
        	case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS");
        		break;
        	case SpeechRecognizer.ERROR_NETWORK:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_NETWORK");
        		break;
        	case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_NETWORK_TIMEOUT");
        		break;
        	case SpeechRecognizer.ERROR_NO_MATCH:
                notCorrectRecognition();
        		//Log.v(TAG, "SpeechRecognizer.ERROR_NO_MATCH");
        		break;
        	case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_RECOGNIZER_BUSY");
        		break;
        	case SpeechRecognizer.ERROR_SERVER:
        		//Log.v(TAG, "SpeechRecognizer.ERROR_SERVER");
        		break;
        	case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                notCorrectRecognition();
        		//Log.v(TAG, "SpeechRecognizer.ERROR_SPEECH_TIMEOUT");
        		break;
        	}
        	
            //Log.d(TAG, "onError " + Integer.toString(error));
        }

        @Override
		public void onResults(Bundle results) {
                 //Log.d(TAG, "onResults " + results);
                 ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                 //Log.v("words", data.toString());
                 
                 if( data.contains(obj.getWord(mIndex)) ) {
                	this.mMistakesCount = 0;
            	 	startSound(R.raw.right,
            	 			mOnSRPlayerCompletionListener);
                    
                     isCorrect = true;
                     
                     int starPosition = mIndex + 1;
                     
                     if( starPosition > 3 ) {
                    	 if( starPosition > 6 ) {
                    		 starPosition -= 6;
                    	 } else {
                    		 starPosition -= 3;
                    	 }
                    	 
                     }
                     
                     ImageView star;
                     
                     switch(starPosition) {
	                  	case 1:
	                  		star = (ImageView) findViewById(R.id.star_1);
	                 		break;
	                 	case 2:
	                  		star = (ImageView) findViewById(R.id.star_2);
	                 		break;
	                 	case 3:
	                  		star = (ImageView) findViewById(R.id.star_3);
	                 		break;
	                 	default:
	                  		star = (ImageView) findViewById(R.id.star_1);
	                 		break;
                     }
                     
                     star.setImageResource(R.drawable.star_on);
                     
                     JsonDataParser jpars = new JsonDataParser(getApplicationContext());

                     jpars.setAsAnswerdCorrect(mIndex, obj.getKey());
                     jpars.setAsUnlocked(mIndex + 1, obj.getKey());
                     
                 } else {
         			this.mMistakesCount++;
                    notCorrectRecognition();
                 }
        }
        
		private void notCorrectRecognition() {
			
			if( this.mMistakesCount % 2 == 0 ) {
				startSound(R.raw.wrong, new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
			            startSound(obj.getSound(mIndex),
			            		mOnSRPlayerCompletionListener);
					}
			    	
			    });
			} else {
				startSound(R.raw.wrong, mOnSRPlayerCompletionListener);
			}
			
			isCorrect = false;
		}
        
        @Override
		public void onPartialResults(Bundle partialResults) {
        	Log.d(TAG, "onPartialResults");
        }
        
        @Override
		public void onEvent(int eventType, Bundle params) {
        	Log.d(TAG, "onEvent " + eventType);
        }
        
        MediaPlayer.OnCompletionListener mOnSRPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {
            
            @Override
            public void onCompletion(MediaPlayer mp) {
        		//Log.v("zzzzz", Boolean.toString(mIsPaused));
            	if(!mIsPaused) {
    	        	stopSound(mp);
    	            if( isCorrect ) {
    	            	if( (mIndex + 1) % 3 == 0 ) {
    	            		if( !obj.getKey().equalsIgnoreCase(FruitsAndVeggies.FRU_AND_VEGGY_KEY) ) {
    		                    
    		                    Intent i = new Intent(getApplicationContext(), AnimationActivity.class);
    		
    		                    i.putExtra("position", mIndex);
    		                    i.putExtra("key", obj.getKey());
    	
    		                    startActivity(i);
    	
    		                    mSpeechRecognizer.stopListening();
    		                    mSpeechRecognizer.cancel();
    		                    
    		                    mSpeechRecognizer.destroy();
    		                    
    		                    finish();
    		                    
    		                    if(BaseTalkieActivity.mMediaPlayer.isPlaying())
    		                    	BaseTalkieActivity.mMediaPlayer.stop();
    		                    
    		                    BaseTalkieActivity.mMediaPlayer.start();
    	            		} else {
    	                		if( mIndex >= obj.getCount() - 1 ) {
    	                            mSpeechRecognizer.stopListening();
    	                            mSpeechRecognizer.cancel();
    	
    	    	                    mSpeechRecognizer.destroy();
    	    	                    finish();
    	    	                    
    	    	                    if(BaseTalkieActivity.mMediaPlayer.isPlaying())
    	    	                    	BaseTalkieActivity.mMediaPlayer.stop();
    	    	                    
    	    	                    BaseTalkieActivity.mMediaPlayer.start();
    	                		} else {
    	
    	        	            	//Log.d("shit test", "correct ");
    	        	            	
    	        	            	mIndex++;
    	        	            	
    	        	            	/*
    	        	                sr.stopListening();
    	        	                sr.cancel();
    	        	                */
    	        	            	
    	        	                
    	        	                ImageView animal = (ImageView) findViewById(R.id.animal);
    	        	                
    	        	                animal.setImageResource(obj.getDrawable(mIndex));
    	
    	    			            startSound(obj.getSound(mIndex), this);
    	        	                
    	        	                isCorrect =  false;
    	        	                
    	            	            int startPosition = 0;
    	            	            
    	            	        	if( mIndex > 5)
    	            	        	{
    	            	        		startPosition = 6;
    	            	        	} else if( mIndex > 2 ) {
    	        	        			startPosition = 3;
    	        		            }
    	            	
    	            	            JsonDataParser jpars = new JsonDataParser(getApplicationContext());
    	            	            
    	            	            for(int ind = startPosition ; ind < startPosition + 3 ; ind++) {
    	        	            		ImageView star;
    	        	            		
    	        	            		switch(ind-startPosition) {
    	        	            			case 1:
    	        	            				star = (ImageView) findViewById(R.id.star_2);
    	        	            				break;
    	        	            			case 2:
    	        	            				star = (ImageView) findViewById(R.id.star_3);
    	        	            				break;
    	        	            			default:
    	        	            				star = (ImageView) findViewById(R.id.star_1);
    	        	            				break;
    	        	            		}
    	        	            		
    	            	            	if( jpars.isAnswerdCorrect(ind, obj.getKey())) {
    	            	            		star.setImageResource(R.drawable.star_on);
    	            	            	} else {
    	            	            		star.setImageResource(R.drawable.star_off);
    	            	            	}
    	            	            }
    	                			
    	                		}
    	            		}
    	            		
    	            	} else {
    		            	//Log.d("shit test", "correct ");
    		            	
    		            	mIndex++;
    		            	
    		            	/*
    		                sr.stopListening();
    		                sr.cancel();
    		                */
    		            	
    		                
    		                ImageView animal = (ImageView) findViewById(R.id.animal);
    		                
    		                animal.setImageResource(obj.getDrawable(mIndex));
    	
    			            startSound(obj.getSound(mIndex), this);
    		                
    		                isCorrect =  false;
    	            	}
    	            	
    	            } else {
    	            	
    	            	mMicAvailable =  true;
    	            	
    	            	/*
    					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    					if (currentapiVersion < android.os.Build.VERSION_CODES.JELLY_BEAN) {
    						startSound(R.raw.beep, null);
    					}
    					
    					//Log.d("shit test", "not correct ");
    					Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.UK.toString());
    					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
    					intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
    					
    					intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10); 
    					mSpeechRecognizer.startListening(intent);
    					*/
    	            }
    	        }
            }
		};
   
   }
   
   	MediaPlayer.OnCompletionListener mOnFirstPlayerCompletionListener = 
   			new MediaPlayer.OnCompletionListener() {
		   		@Override
		   		public void onCompletion(MediaPlayer mp) {
		   			//Log.v("zzzzz", Boolean.toString(mIsPaused));
		   			if(!mIsPaused) {
		   				stopSound(mp);
		   			    
		   				mMicAvailable = true;
		   			}
			}
	};

	OnClickListener mOnMicClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(mMicAvailable ) {
		    	if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN){
					startSound(R.raw.beep, null);
		    	}
		
		        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.UK.toString());
		        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
		
		        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10); 
		        mSpeechRecognizer.startListening(intent);
			}
		}
	};
	
	@Override
	public void onBackPressed() {

        if(BaseTalkieActivity.mMediaPlayer.isPlaying())
        	BaseTalkieActivity.mMediaPlayer.stop();
        
        BaseTalkieActivity.mMediaPlayer.start();
        
		finish();
	    super.onBackPressed();
	 }
    
	protected MediaPlayer startSound(int resId, MediaPlayer.OnCompletionListener listener) {
	    MediaPlayer mp = MediaPlayerOptimizer.create(LevelActivity.this, resId, false);

	    mp.setOnCompletionListener(listener);
	    
	    mp.start();
	    
	    mMediaPlayers.add(mp);
	    
	    mp.setOnErrorListener( new MyOnErrorListener(resId, listener) );
	    
	    return mp;
	}
    
	protected void stopSound(MediaPlayer mp) {
		mp.setOnCompletionListener(null);
        mp.stop();   // It's always safe to call stop()
        mp.release();  // release resources internal to the MediaPlayer
        mMediaPlayers.remove(mp); // remove reference to MediaPlayer to allow GC
	}
	
    @Override
    public void onPause() {
    	
    	mIsPaused = true;
    	
	    for (Object mediaPlayer : mMediaPlayers.toArray()) {
	    	stopSound((MediaPlayer) mediaPlayer); // stop, release, and free for GC, each mp.
	    } 
    	
		if(mSpeechRecognizer != null) {
	        mSpeechRecognizer.stopListening();
	        mSpeechRecognizer.cancel();
	        mSpeechRecognizer.destroy();
		}
		
		mSpeechRecognizer = null;
		
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	
    	mIsPaused = false;
    	
    	super.onResume();
    	
    	if( mMediaPlayer.isPlaying() )
    		mMediaPlayer.pause();
	    
	    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
	    mWordRecognitionListener = new WordRecognitionListener(this.mIndex, mTalkieObj);
	    mSpeechRecognizer.setRecognitionListener(mWordRecognitionListener);
    }
    
    class MyOnErrorListener implements OnErrorListener {

    	private MediaPlayer.OnCompletionListener mOnCompletionListener;
    	private int mResourceId;
    	
    	MyOnErrorListener( int resourceId, MediaPlayer.OnCompletionListener onCompletionListener ) {
    		mResourceId = resourceId;
    		mOnCompletionListener = onCompletionListener;
    	}
    	
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			startSound(mResourceId, mOnCompletionListener);
			return false;
		}
    	
    }
}