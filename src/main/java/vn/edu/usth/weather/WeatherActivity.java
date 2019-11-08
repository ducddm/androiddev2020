package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForecastFragment firstFragment = new ForecastFragment();
        // Add the fragment to the ' container' FrameLayout
        getSupportFragmentManager(). beginTransaction(). add(
                R.id.container, firstFragment). commit();


    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("THIS","Welcome to USTH Weather");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("THIS","Welcome to  Weather");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("THIS","USTH Weather");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("THIS","Weather");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("THIS","to");
    }


}
