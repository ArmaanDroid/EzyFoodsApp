package sanguinebits.com.ezyfoods;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sanguinebits.com.ezyfoods.home.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.start(MainActivity.this);
                MainActivity.this.finish();
            }
        },1000);
    }
}
