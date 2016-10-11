package com.screamlouder.screamlouder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Øyvind Grimstad
 * @author Vegard Brestad
 * @author Erik Mikalsen
 * Date: 11.10-2016
 * Description: A simple app that records and displays the current volume input measured in Decibel.
 */

public class WallOfFame extends AppCompatActivity {

    //Classvariables
    private static final String AUDIO_RECORDER_FOLDER = "screamLouder";
    private static final String FILE_SEPARATOR = "/";
    MediaPlayer mp = new MediaPlayer();

    /**
     *
     * @param savedInstanceState
     * initializes the ListView with all the files/recordings, and initialize a mediaplayer so you can hear the recording when a recording is clicked
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_of_fame);

        ListView wallOfFame = (ListView) findViewById(R.id.wallOfFame);

        final String path = Environment.getExternalStorageDirectory()+FILE_SEPARATOR+AUDIO_RECORDER_FOLDER;

        File f = new File(path);
        /*
        if (!f.exists()) {
            f.mkdirs();
        }
        */
        File file[] = f.listFiles();

        ArrayList<String> files = new ArrayList<String>();

        for(int i = 0; i < file.length; i++){
            files.add((file[i].getName())); //files.add((file[i].getName().toString())); redundant
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, files);
        wallOfFame.setAdapter(arrayAdapter);


        wallOfFame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp = new MediaPlayer();
                Log.i("info", parent.getAdapter().getItem(position).toString());
                try {
                    Uri myUri = Uri.parse(path +"/"+parent.getAdapter().getItem(position).toString());
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setDataSource(getApplicationContext(), myUri);
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//onItemClick()
        });//setOnItemClickListener()

    }//onCreate()

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsItemSelected()

    @Override
    public void onPause() {
        super.onPause();
        if (mp != null) {
            mp.stop();
            mp = null;
        }
    }//onPause()

}//class()
