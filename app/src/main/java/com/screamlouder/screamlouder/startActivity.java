package com.screamlouder.screamlouder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Øyvind Grimstad
 * @author Vegard Brestad
 * @author Erik Mikalsen
 * Date: 11.10-2016
 * Description: A simple app that records and displays the current volume input measured in Decibel.
 */

public class startActivity extends AppCompatActivity {
    EditText nameInput;
    Button buttonInput;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        nameInput = (EditText) findViewById(R.id.nameInput);
        buttonInput = (Button) findViewById(R.id.buttonInput);

    }//onCreate()

    /**
     *
     * @param view Switches view to MainActivity.class
     */
    public void changeView(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("nameInfo", nameInput.getText().toString());
        startActivity(i);
    }//changeView()

}//class
