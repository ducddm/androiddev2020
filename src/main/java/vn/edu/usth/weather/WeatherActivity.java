package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WeatherActivity extends AppCompatActivity {

    MediaPlayer music;

    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // This method is executed in main thread
            String content = msg.getData().getString("server_response");
            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
        }
    };


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

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String content = msg. getData(). getString("server_response");
                Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
            }
        };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // wait for 5 seconds to simulate a long network access
                    Thread. sleep(5000);
                }
                catch (InterruptedException e) {
                    e. printStackTrace();
                }
                // Assume that we got our data from server
                Bundle bundle = new Bundle();
                bundle.putString("server_response", "some sample json here");
                // notify main thread
                Message msg = new Message();
                msg. setData(bundle);
                handler. sendMessage(msg);
            }
        });
        t. start();
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String> {
        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // do some preparation here, if needed
            progressDialog = ProgressDialog.show(WeatherActivity.this,
                    "Updating weather...",
                    "Wait for 5 seconds!");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(5000);
                resp = "Sleep for 5 seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            // Assume that we got our data from server
            Bundle bundle = new Bundle();
            bundle.putString("server_response", "some sample json here");
            // notify main thread
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // Do something here
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.refresh:
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("5000");
                return true;
            case R.id.settings:
                Intent intent = new Intent(this,PrefActivity.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
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
