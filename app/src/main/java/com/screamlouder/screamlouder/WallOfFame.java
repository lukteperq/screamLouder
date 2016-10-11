package com.screamlouder.screamlouder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class WallOfFame extends AppCompatActivity {

    private static final String AUDIO_RECORDER_FOLDER = "screamLouder";
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_of_fame);

        ListView wallOfFame = (ListView) findViewById(R.id.wallOfFame);
        TextView skjiit = (TextView)findViewById(R.id.skjiit);
        Log.i("infor", "heisann");

        //final ArrayList<String> files = new ArrayList<String>();

        File directory = new File(AUDIO_RECORDER_FOLDER);
        File[] contents = directory.listFiles();
        skjiit.setText("start");
        if (directory.isDirectory()) {


            String[] files = directory.list();
            if (files.length == 0) {
                //directory is empty
                Log.i("info", "empty");
            }else{
                Log.i("info", "Not empty");
            }
        }else{
            Log.i("infor", "Not directory");
        }




/*
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
        /*
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, theNamesOfFiles);
        //new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theNamesOfFiles);
        wallOfFame.setAdapter(arrayAdapter);

*/
    }

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


}
