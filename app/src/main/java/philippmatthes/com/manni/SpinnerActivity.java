package philippmatthes.com.manni;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class SpinnerActivity extends AppCompatActivity {

    protected void startAnimatingSpinner() {
        ImageView spinner = (ImageView) findViewById(R.id.spinner);

        spinner.setVisibility(View.VISIBLE);

        spinner.post(() -> {
            AnimationDrawable spinnerAnim = (AnimationDrawable) spinner.getBackground();
            if (!spinnerAnim.isRunning()) {
                spinnerAnim.start();
            }
        });
    }

    protected void stopAnimatingSpinner() {
        ImageView spinner = (ImageView) findViewById(R.id.spinner);

        spinner.setVisibility(View.GONE);

        spinner.post(() -> {
            AnimationDrawable spinnerAnim = (AnimationDrawable) spinner.getBackground();
            if (spinnerAnim.isRunning()) {
                spinnerAnim.stop();
            }
        });
    }

}
