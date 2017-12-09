package async.example.com.mappingapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        TextView tv = findViewById(R.id.title);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "Anabelle Script.ttf"));

        TextView stv = findViewById(R.id.sub);
        stv.setTypeface(Typeface.createFromAsset(getAssets(), "Stay_Writer.ttf"));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashScreen.this, MapsActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
