package co.talkie_kids.talkie.Activities;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.talkie_kids.talkie.R;

public class GameActivity extends BaseTalkieActivity {
	
	private static final int SCORE_PER_STAR = 20;

	protected final static ArrayList<Integer> COLUMN_IDS_LIST = new ArrayList<Integer>() {
		private static final long serialVersionUID = 1L;

		{
	        add(R.id.rlFirstColumn);
	        add(R.id.rlSecondColumn);
	        add(R.id.rlThirdColumn);
	        add(R.id.rlLastColumn);
	    }
	};
	
	protected final static ArrayList<Integer> FALLING_OBJECTS_LAYOUTS_LIST = new ArrayList<Integer>() {
		private static final long serialVersionUID = 1L;

		{
	        add(R.layout.star_bad);
	        add(R.layout.star_good);
	    }
	};

	public static final String TAG_SCORE = "score";

	protected long mMaxFallingTimeMiliseconds = 5000L;
	protected long mMinFallingTimeMiliseconds = 1000L;
	
	protected long mMaxDropNewObjectDelayMiliseconds = 5000L;
	protected long mMinDropNewObjectDelayMiliseconds = 1000L;
	
	protected int mLimitFallingObjects = 6;
	protected int mMaximumFallingObjects = 1;
	protected int mFallinObjectsCount = 0;
	protected int mMaxFallingCount = 0;
	
	protected ImageView mIvCharacter;
	protected Random mRand = new Random();
	
	private Handler mMyHandler = new Handler();

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_game);
        
        mIvCharacter = (ImageView) findViewById(R.id.ivCharacter);
        
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setClickable(true);
        ivLeft.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goLeft();				
			}
		});
        
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setClickable(true);
        ivRight.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goRight();				
			}
		});
        
        startDroppingObjects();
    }
    
	private void startDroppingObjects() {

		if(mFallinObjectsCount <= mMaximumFallingObjects) {
			dropObject();
			
			try {
				long milSecToDelay = mMinDropNewObjectDelayMiliseconds 
						+ ((long)(mRand.nextDouble()*(mMaxDropNewObjectDelayMiliseconds
								- mMinDropNewObjectDelayMiliseconds)));
				
				mMyHandler.postDelayed(new Runnable() {
		 
		            @Override
		            public void run() {
		            	dropObject();
		            }
		 
		        }, milSecToDelay);
			} catch (Exception e) {
				Log.v("error", "-----------------------------------------");
				Log.v("mMinDropNewObjectDelayMiliseconds", Long.toString(mMinDropNewObjectDelayMiliseconds));
				Log.v("mMaxDropNewObjectDelayMiliseconds", Long.toString(mMaxDropNewObjectDelayMiliseconds));
				Log.v("end error", "-----------------------------------------");
			}
		}
	}

	@SuppressLint("NewApi")
	private void dropObject() {

		mFallinObjectsCount++;
		
		if(mMaxFallingCount < mFallinObjectsCount)
			mMaxFallingCount++;
		
		final int fallenObjectListIndex = mRand.nextInt(FALLING_OBJECTS_LAYOUTS_LIST.size());
		final ImageView fallingObject = (ImageView) getLayoutInflater()
				.inflate(FALLING_OBJECTS_LAYOUTS_LIST.get(fallenObjectListIndex), null);
		
		int chosenColumnListIndex = mRand.nextInt(COLUMN_IDS_LIST.size());
		final RelativeLayout rlColumn = (RelativeLayout)findViewById(COLUMN_IDS_LIST
				.get(chosenColumnListIndex));
		
		rlColumn.addView(fallingObject);
		
    	RelativeLayout.LayoutParams params =
    			(RelativeLayout.LayoutParams) fallingObject.getLayoutParams();
        params.topMargin = - dpToPx(30);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        fallingObject.setLayoutParams(params);
		
		
		ViewTreeObserver vto = fallingObject.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

		    @SuppressWarnings("deprecation")
			@Override
		    public void onGlobalLayout() {
				
				final int newLeftMargin = rlColumn.getHeight() + fallingObject.getHeight() ;

				
				final Animation a = new Animation() {

				    @Override
				    protected void applyTransformation(float interpolatedTime, Transformation t) {
				    	RelativeLayout.LayoutParams params =
				    			(RelativeLayout.LayoutParams) fallingObject.getLayoutParams();
				        params.topMargin = (int)(newLeftMargin * interpolatedTime) - fallingObject.getHeight();
				        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				        fallingObject.setLayoutParams(params);
				        
				        if( params.topMargin >= newLeftMargin - mIvCharacter.getHeight() - fallingObject.getHeight()) {
				        	this.cancel();
				        	 
			        		if(((RelativeLayout) mIvCharacter.getParent()).getId() == rlColumn.getId())
			        			eatObject(fallenObjectListIndex);
				        }
				    }
				};
				
				a.setDuration(mMaxFallingTimeMiliseconds);
				
				a.setAnimationListener(new Animation.AnimationListener(){
				    @Override
				    public void onAnimationStart(Animation arg0) {
				    }           
				    @Override
				    public void onAnimationRepeat(Animation arg0) {
				    }           
				    @Override
				    public void onAnimationEnd(Animation arg0) {
				    	mFallinObjectsCount--;
				    	rlColumn.removeView(fallingObject);
				    	startDroppingObjects();
				    }
				});

			    Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
							fallingObject.setVisibility(View.VISIBLE);
							
							fallingObject.startAnimation(a);
			         } 
			    }, 100); 

				/*
				final android.view.animation.Animation fadein = 
						AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
				fadein.setDuration(10);
				fallingObject.startAnimation(fadein);
				
				fallingObject.setVisibility(View.INVISIBLE);
				
				fadein.setAnimationListener( new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
				    	RelativeLayout.LayoutParams params =
				    			(RelativeLayout.LayoutParams) fallingObject.getLayoutParams();
				        params.topMargin = - fallingObject.getHeight();
				        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				        fallingObject.setLayoutParams(params);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						fallingObject.setVisibility(View.VISIBLE);
						
						fallingObject.startAnimation(a);
					}
				});
		    	*/
		    	
		        ViewTreeObserver obs = fallingObject.getViewTreeObserver();

		        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		            obs.removeOnGlobalLayoutListener(this);
		        } else {
		            obs.removeGlobalOnLayoutListener(this);
		        }
		    }

		});
	}
	protected void goLeft() {
		int columnId = ((RelativeLayout) mIvCharacter.getParent()).getId();
		
		if(columnId != COLUMN_IDS_LIST.get(0) && columnId != View.NO_ID) {
			moveCharacterTo(COLUMN_IDS_LIST.get(COLUMN_IDS_LIST.indexOf(columnId) -1));
		}
	}

	protected void goRight() {
		int columnId = ((RelativeLayout) mIvCharacter.getParent()).getId();

		if(columnId != COLUMN_IDS_LIST.get( COLUMN_IDS_LIST.size() -1 )
				&& columnId != View.NO_ID) {
			moveCharacterTo(COLUMN_IDS_LIST.get(COLUMN_IDS_LIST.indexOf(columnId) + 1));
		}
	}

	private void moveCharacterTo(int columnId) {
		
		((RelativeLayout) (mIvCharacter).getParent()).removeView(mIvCharacter);
		
		((RelativeLayout) findViewById(columnId)).addView(mIvCharacter);
	}

	protected void eatObject(int fallenObjectListIndex) {
		//System.gc();
		if( fallenObjectListIndex == 0) {
			TextView tvLife = (TextView) findViewById(R.id.tvLife);
			int life = Integer.valueOf(tvLife.getText().toString());
			
			life--;
			if( life < 0) {
				gameOver();
			} else {
				tvLife.setText(Integer.toString(life));
			}
			
		} else {
			if(mMaximumFallingObjects <= mLimitFallingObjects) {
				mMaximumFallingObjects++;
			}
			/*
			//if( mMaxDropNewObjectDelayMiliseconds > mMinDropNewObjectDelayMiliseconds) {
			mMaxDropNewObjectDelayMiliseconds += SCORE_PER_STAR 
					* (mMaxDropNewObjectDelayMiliseconds / mMaxFallingTimeMiliseconds) * 10;
			
			long increaseMinTimeWith = SCORE_PER_STAR 
					* (mMinDropNewObjectDelayMiliseconds / mMinFallingTimeMiliseconds);
			
			if(mMaxDropNewObjectDelayMiliseconds > mMinDropNewObjectDelayMiliseconds + increaseMinTimeWith) {
				mMinDropNewObjectDelayMiliseconds += increaseMinTimeWith;
			} else {
				mMinDropNewObjectDelayMiliseconds -= 1000L;
			}
			
			//}
			*/
			if( mMaxFallingTimeMiliseconds > mMinFallingTimeMiliseconds) {
				mMaxFallingTimeMiliseconds -= SCORE_PER_STAR;
			}
			
			TextView tvScore = (TextView) findViewById(R.id.tvScore);
			long score = Long.valueOf(tvScore.getText().toString()) + SCORE_PER_STAR;

			if( score > 6000 ) {
				Log.v("log", "-----------------------------------------");
				Log.v("mMinFallingTimeMiliseconds", Long.toString(mMinFallingTimeMiliseconds));
				Log.v("mMaxFallingTimeMiliseconds", Long.toString(mMaxFallingTimeMiliseconds));
				Log.v("mMinDropNewObjectDelayMiliseconds", Long.toString(mMinDropNewObjectDelayMiliseconds));
				Log.v("mMaxDropNewObjectDelayMiliseconds", Long.toString(mMaxDropNewObjectDelayMiliseconds));
				Log.v("mMaximumFallingObjects", Long.toString(mMaximumFallingObjects));
				Log.v("mLimitFallingObjects", Long.toString(mLimitFallingObjects));
				Log.v("mFallinObjectsCount", Long.toString(mFallinObjectsCount));
				Log.v("end log", "-----------------------------------------");
			}
			
			tvScore.setText(Long.toString(score));
		}
	}

	private void gameOver() {
		
		Toast.makeText(getApplicationContext(), Integer.toString(mMaxFallingCount), Toast.LENGTH_SHORT).show();
		
		mMyHandler.removeCallbacksAndMessages(null);
		
		finish();

		TextView tvScore = (TextView) findViewById(R.id.tvScore);
		long score = Long.valueOf(tvScore.getText().toString()) + SCORE_PER_STAR;
        Intent intent = new Intent(GameActivity.this, GameOver.class);
        intent.putExtra(TAG_SCORE, score);
        startActivity(intent);
	}
	
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
}
