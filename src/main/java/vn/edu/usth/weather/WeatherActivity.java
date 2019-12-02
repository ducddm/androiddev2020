package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WeatherActivity extends AppCompatActivity {

    MediaPlayer music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        HomeFragmentPaperAdapter adapter = new HomeFragmentPaperAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);


        // Give the TabLayout the ViewPager
        //ForecastFragment firstFragment = new ForecastFragment();
        // Add the fragment to the ' container' FrameLayout
        //getSupportFragmentManager(). beginTransaction(). add(
        //R.id.container, firstFragment). commit();

        copyFileToExternalStorage(R.raw.musique,"musique.mp3");

        music = MediaPlayer.create(WeatherActivity.this,R.raw.musique);
        music.start();



    }

    private void copyFileToExternalStorage(int resourceId, String resourceName){
        String pathSDCard = Environment.getExternalStorageDirectory()
                + "/Android/data/vn.edu.usth.weather/" + resourceName;
        try{
            InputStream in = getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
