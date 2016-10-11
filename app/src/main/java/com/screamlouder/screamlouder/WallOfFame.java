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
import java.util.List;

public class WallOfFame extends AppCompatActivity {

    private static final String AUDIO_RECORDER_FOLDER = "screamLouder";
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String FILE_SEPARATOR = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_of_fame);

        ListView wallOfFame = (ListView) findViewById(R.id.wallOfFame);
        TextView skjiit = (TextView)findViewById(R.id.skjiit);

        //final ArrayList<String> files = new ArrayList<String>();

/*
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        */
        //List<File> files = getListFiles(new File(Environment.getExternalStorageDirectory().getPath()));





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




    private int readLogList( String filePath )
    {
        File directory = Environment.getExternalStorageDirectory();

        File folder = new File( directory + FILE_SEPARATOR + filePath );

        if ( !folder.exists() )
        {
            return 0;
        }

        return  folder.list().length;
    }


}
