package co.talkie_kids.talkie.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import co.talkie_kids.talkie.R;
import co.talkie_kids.talkie.utilities.DeviceSpecifications;

public class SplashScreenVersion4Activity extends BaseTalkieActivity {
	 
    // used to know if the back button was pressed in the splash screen activity and avoid opening the next activity
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000; // 2 seconds
 
 
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String screenSizeValue = DeviceSpecifications.getScreenSize(getResources());
        
        String densityDpiValue = DeviceSpecifications.getDpiDensity(getResources());
        
        String deviceId = DeviceSpecifications.getDeviceId(getApplication());
        
        Toast.makeText(getApplicationContext(),
        		"Screen size: " + screenSizeValue + "\nDensity DPI: " + densityDpiValue
        		+ "\nDevice ID: " + deviceId,
        		Toast.LENGTH_SHORT).show();
        
        setContentView(R.layout.splash_screen);
 
        splashScreen();
 
    }

	private void splashScreen() {
		Handler handler = new Handler();
 
        // run a thread after 2 seconds to start the LevelChooseActivity
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {
 
                // make sure we close the splash screen so the user won't come back when it presses back key
 
                finish();
                 
                if (!mIsBackButtonPressed) {
                    // start the home screen if the back button wasn't pressed already 
                    Intent intent = new Intent(SplashScreenVersion4Activity.this, CategoryChooseActivity.class);
                    SplashScreenVersion4Activity.this.startActivity(intent);
               }
                 
            }
 
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
	}
 
    @Override
   public void onBackPressed() {
 
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();
 
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onPause();
    }
    
}