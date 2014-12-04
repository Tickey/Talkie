package co.talkie_kids.talkie.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.resources.Animations;
import co.talkie_kids.talkie.utilities.network.ConnectionCheck;

public class AnimationActivity extends BaseTalkieActivity {
 
	private int mIndex;
	private String mKey;
	private int layoutIndex;
	private ArrayList<Integer> mAnimationsShowed = new ArrayList<Integer>();
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.animation);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent i = getIntent();

        int index = i.getIntExtra("position", 0);
        String key = i.getStringExtra("key");
        
        this.mIndex = index;
        this.mKey = key;
        
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout;

        layoutIndex = index/3;
        
    	layout = (RelativeLayout) inflater.inflate(Animations.layouts[layoutIndex], null, false);

        RelativeLayout animationContent = (RelativeLayout) findViewById(R.id.animationContent);
        animationContent.addView(layout);
        
        fadeAnimations(0,Animations.comicsImagesIds[layoutIndex].length);
        
    }
    
	private void fadeAnimations(final int current, final int end) {
		
		Log.v("fadeAnimations", "----BEGIN-----: " + Integer.toString(current));
		
    	final ImageView comicsImage = (ImageView) findViewById(Animations.comicsImagesIds[layoutIndex][current]);

		comicsImage.setVisibility(View.VISIBLE);
		final android.view.animation.Animation fadein = 
				AnimationUtils.loadAnimation(this, R.anim.fadein);
		
		comicsImage.startAnimation(fadein);
		comicsImage.setVisibility(View.INVISIBLE);
		fadein.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(
					android.view.animation.Animation animation) {
				if( !mAnimationsShowed.contains(current))
				{
					comicsImage.setVisibility(View.VISIBLE);
					if( current < end-1 )
					{
						fadeAnimations(current+1, end);
					}
					else
					{
						animationEnd();
					}
					
					mAnimationsShowed.add(current);
				}
			}

			@Override
			public void onAnimationRepeat(
					android.view.animation.Animation animation) {
				
			}

			@Override
			public void onAnimationStart(final android.view.animation.Animation animation) {

				View continueAnimation = (View) findViewById(R.id.continueAnimation);
				
				continueAnimation.setOnClickListener( new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						if( !mAnimationsShowed.contains(current))
						{
							comicsImage.setVisibility(View.VISIBLE);
							animation.cancel();
						}
					}
					
				});
				
			}
		});

		Log.v("fadeAnimations", "----END-----: " + Integer.toString(current));
	}
	
	void animationEnd(){
		Log.v("animation", "end");
		
		final ImageView nextImage = (ImageView) findViewById(R.id.next);
		
		android.view.animation.Animation fadein = 
				AnimationUtils.loadAnimation(this, R.anim.fadein);
		nextImage.startAnimation(fadein);
		fadein.setAnimationListener(new AnimationListener() {


			@Override
			public void onAnimationEnd(
					android.view.animation.Animation animation) {
					nextImage.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(
					android.view.animation.Animation animation) {
				
			}

			@Override
			public void onAnimationStart(
					android.view.animation.Animation animation) {
				
			}
		});
	}
	
	public void goToNextLevel(View v)
	{
		finish();
        if( mIndex < 8)
        {
        	if( ConnectionCheck.isOnline(getApplicationContext()) )
        	{
                Intent i = new Intent(getApplicationContext(), LevelActivity.class);

                i.putExtra("position", mIndex+1);
                i.putExtra("key", mKey);
                
                startActivity(i);
        	}
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), CategoryChooseActivity.class);
            
            startActivity(i);
        }
	}

	@Override
	public void onBackPressed() {
		finish();
	    super.onBackPressed();
	
	 }
}