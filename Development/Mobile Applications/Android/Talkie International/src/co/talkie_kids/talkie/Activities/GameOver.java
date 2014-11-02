package co.talkie_kids.talkie.activities;

import android.os.Bundle;
import android.widget.TextView;
import co.talkie_kids.talkie.R;

public class GameOver extends BaseTalkieActivity {

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_game_over);
        
        long score = getIntent().getLongExtra(GameActivity.TAG_SCORE, 0);
        
        if( score > 0 ) {
        	((TextView) findViewById(R.id.tvGameOver))
        			.setText( getString(R.string.your_score_is) + " " + Long.toString(score));
        }
    }
}
