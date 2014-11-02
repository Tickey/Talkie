package co.talkie_kids.talkie.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import co.talkie_kids.talkie.R;

public class LanguageChooseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_language_choose);

		initializeViews();
	}

	private void initializeViews() {
		ImageView cloud = (ImageView) findViewById(R.id.chooseCategoryCloudsTopUpper);

		Animation shake = AnimationUtils.loadAnimation(
				this.getApplicationContext(), R.anim.shake);
		cloud.startAnimation(shake);

		cloud = (ImageView) findViewById(R.id.chooseCategoryCloudsTopLower);
		shake = AnimationUtils.loadAnimation(this.getApplicationContext(),
				R.anim.shake2);
		cloud.startAnimation(shake);
	}

}
