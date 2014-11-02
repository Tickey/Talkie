package co.talkie_kids.talkie.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.data.FruitsAndVeggies;
import co.talkie_kids.talkie.network.utilities.ConnectionCheck;
import co.talkie_kids.talkie.parcelable.JsonDataParser;

public class LevelChooseActivity extends BaseTalkieActivity {

	
	private boolean resumeHasRun = false;
	private View v;
	private JsonDataParser jparser;
	private String mCategoryKey;
	private boolean mLevelChoosen = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.level_choose);

        Intent i = getIntent();

        mCategoryKey = i.getStringExtra("key");
		
		v = findViewById(R.id.chooseLevelView);
		
		View animView = v.findViewWithTag("anim-1");
		animView.setClickable(false);
		animView = v.findViewWithTag("anim-2");
		animView.setClickable(false);
		animView = v.findViewWithTag("anim-3");
		animView.setClickable(false);
		
		jparser = new JsonDataParser(this);
		
		setupGameViews();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
    	this.mLevelChoosen = false;
	    if (!resumeHasRun) {
	        resumeHasRun = true;
	        return;
	    }
	    
		setupGameViews();
	}

	private void setupGameViews() {
		
		for(int i = 0 ; i < 3 ; i++ )
		{
			View currentView = v.findViewWithTag("anim-" + Integer.toString(i+1));
			currentView.setClickable(true);
			
			if( i < jparser.getUnlockedAnimationsNumber(mCategoryKey) && !mCategoryKey.equalsIgnoreCase(FruitsAndVeggies.FRU_AND_VEGGY_KEY) )
			{
				ImageView img = (ImageView) currentView.findViewById(R.id.animation);
				
				img.setImageResource(R.drawable.choose_object_animation_active);
				
				currentView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						animationChoose(v);
					}
					
				});
			}
			else
			{
				currentView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Vibrate();
					}
					
				});
			}
		}
		
		for(int i = 0 ; i < 9 ; i++)
		{
			View currentView = v.findViewWithTag(Integer.toString(i+1));
			
			if(currentView == null)
				Log.v("tag not found", "tag not found");
			
			ImageView img = (ImageView) currentView.findViewById(R.id.bubble);
			img.setImageResource(R.drawable.choose_object_object_active);
			currentView.setClickable(true);
			
			if(jparser.isUnlocked(i, mCategoryKey))
			{
				currentView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						levelChoose(v);
					}
					
				});

				img = (ImageView) currentView.findViewById(R.id.star);
				if(jparser.isAnswerdCorrect(i, mCategoryKey)){
					img.setImageResource(R.drawable.star_on);
				}
				else
				{
					img.setImageResource(R.drawable.star_off);
				}
			}
			else
			{
				currentView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Vibrate();
					}
					
				});
				img.setImageResource(R.drawable.choose_object_object_inactive);
				img = (ImageView) currentView.findViewById(R.id.star);
				img.setImageResource(R.drawable.padlock_small);
			}
		}
	}
	
	public void levelChoose(View v)
	{
		if( !this.mLevelChoosen )
		{
	        this.mLevelChoosen  = true;
			if ( ConnectionCheck.isOnline(this))
			{
				Log.v("tag", (String) v.getTag());
				int position = Integer.parseInt((String) v.getTag());
		
		        Intent i = new Intent(getApplicationContext(), LevelActivity.class);
		
		        i.putExtra("position", position-1);
		        i.putExtra("key", mCategoryKey);
		        startActivity(i);
			}
			else
			{
		        this.mLevelChoosen  = false;
				Toast.makeText(this, "Connect to internet", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void animationChoose(View v)
	{
		if( !mCategoryKey.equalsIgnoreCase( FruitsAndVeggies.FRU_AND_VEGGY_KEY ) )
		{
	        Intent i = new Intent(getApplicationContext(), AnimationActivity.class);
	
	        String idStr = (String) v.getTag();
	
	        idStr = idStr.substring(idStr.length()-1);
	        
	        int position;
	        int id = Integer.parseInt(idStr);
	        switch(id)
	        {
		        case 1:
		        	position = 3;
		        	break;
		        case 2:
		        	position = 6;
		        	break;
		        case 3:
		        	position = 9;
		        	break;
		        default:
		        	position = 0;
		        	break;
	        }
	        
	        
	        i.putExtra("position", position-1);
	        i.putExtra("key", mCategoryKey);
	        startActivity(i);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	    super.onBackPressed();
	
	 }

	private void Vibrate() {
		if( !this.mLevelChoosen )
		{
			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(100);
		}
	}
}