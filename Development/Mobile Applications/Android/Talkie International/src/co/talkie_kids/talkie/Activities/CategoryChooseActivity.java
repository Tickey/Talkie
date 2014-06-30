package co.talkie_kids.talkie.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.Data.Animals;
import co.talkie_kids.talkie.Data.FruitsAndVeggies;
import co.talkie_kids.talkie.Parcelable.JsonDataParser;

public class CategoryChooseActivity extends BaseTalkieActivity {
    private JsonDataParser jpars;
    private boolean isAnimalsOpened = false;
    private boolean isVeggiesOpened = false;
 
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.category_choose);
        
        jpars = new JsonDataParser(this);

    	ImageView box = (ImageView) findViewById(R.id.animals);
        if( jpars.isBoxOpened(Animals.ANIMALS_KEY) )
        {
        	box.setImageResource(R.drawable.chest_opened_animals);
        	isAnimalsOpened = true;
        }
        else
        {
        	box.setImageResource(R.drawable.chest_closed);
        }

    	box = (ImageView) findViewById(R.id.vegetables);
        if( jpars.isBoxOpened(FruitsAndVeggies.FRU_AND_VEGGY_KEY) ) {
        	box.setImageResource(R.drawable.chest_opened_fruits);
        	isVeggiesOpened = true;
        } else {
        	box.setImageResource(R.drawable.chest_closed);
        }

        // The image is coming from resource folder but it could also 
        // load from the internet or whatever
        
        ImageView cloud = (ImageView) findViewById(R.id.chooseCategoryCloudsTopUpper);
        
        Animation shake = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.shake);
        cloud.startAnimation(shake);
        
        cloud = (ImageView) findViewById(R.id.chooseCategoryCloudsTopLower);
        shake = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.shake2);
        cloud.startAnimation(shake);

    }
    
    public void animalsClickListener(View v) {
    	if( isAnimalsOpened ) {
            Intent i = new Intent(getApplicationContext(), LevelChooseActivity.class);
            
            i.putExtra("key", Animals.ANIMALS_KEY);
            
            startActivity(i);
    	} else {
	    	jpars.setBoxOpened(Animals.ANIMALS_KEY);
	    	ImageView animalsBox = (ImageView) v;
	    	
	    	animalsBox.setImageResource(R.drawable.chest_opened_animals);
	
	        Handler handler = new Handler();
	
	        handler.postDelayed(new Runnable() {
	 
	            @Override
	            public void run() {
	                Intent i = new Intent(getApplicationContext(), LevelChooseActivity.class);
	                
	                i.putExtra("key", Animals.ANIMALS_KEY);
	                
	                startActivity(i);
	            }
	 
	        }, 200);
    	}
    }
    
    public void vegetablesClickListener(View v) {
    	if( isVeggiesOpened ) {
            Intent i = new Intent(getApplicationContext(), LevelChooseActivity.class);
            
            i.putExtra("key", FruitsAndVeggies.FRU_AND_VEGGY_KEY);

            startActivity(i);
    	} else {
	    	jpars.setBoxOpened(FruitsAndVeggies.FRU_AND_VEGGY_KEY);
	    	ImageView veggieBox = (ImageView) v;
	    	
	    	veggieBox.setImageResource(R.drawable.chest_opened_fruits);
	
	        Handler handler = new Handler();
	
	        handler.postDelayed(new Runnable() {
	 
	            @Override
	            public void run() {
	                Intent i = new Intent(getApplicationContext(), LevelChooseActivity.class);
	                
	                i.putExtra("key", FruitsAndVeggies.FRU_AND_VEGGY_KEY);
	
	                startActivity(i);
	            }
	 
	        }, 200);
    	}
        
    	//Toast.makeText(this, "��������� �� ������������", Toast.LENGTH_LONG).show();
    }
    
    public void newLevelsClickListener(View v) {
    	Toast.makeText(this, "Secret levels will be available in the new version!", Toast.LENGTH_LONG).show();
    }

	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
    }
}