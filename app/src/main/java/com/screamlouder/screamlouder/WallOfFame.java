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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WallOfFame extends AppCompatActivity {

    private static final String AUDIO_RECORDER_FOLDER = "screamLouder";
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String FILE_SEPARATOR = "/";
    MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_of_fame);

        ListView wallOfFame = (ListView) findViewById(R.id.wallOfFame);

        final String path = Environment.getExternalStorageDirectory()+FILE_SEPARATOR+AUDIO_RECORDER_FOLDER;
        //Log.i("info", path);
        TextView skjiit = (TextView)findViewById(R.id.skjiit);
        //skjiit.setText(path);

        File f = new File(path);
        /*
        if (!f.exists()) {
            f.mkdirs();
        }
        */
        File file[] = f.listFiles();

        ArrayList<String> files = new ArrayList<String>();

        for(int i = 0; i < file.length; i++){
            files.add((file[i].getName().toString()));
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
                //mp.stop();











                //Toast.makeText(getBaseContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            }
        });


/*
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        //List<File> files = getListFiles(new File(Environment.getExternalStorageDirectory().getPath()));





        final ArrayList<String> people = new ArrayList<String>(); //final pga skal brukes i annen class
        people.add("Wall of Fame");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, people);
        wallOfFame.setAdapter(arrayAdapter);



        File dir = new File("screamLouder");
        File[] filelist = dir.listFiles();

        String[] theNamesOfFiles = new String[filelist.length];

        for (int i = 0; i < theNamesOfFiles.length; i++) {
            theNamesOfFiles[i] = filelist[i].getName();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, theNamesOfFiles);
        //new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theNamesOfFiles);
        wallOfFame.setAdapter(arrayAdapter);
*/

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
    }

}//class()
