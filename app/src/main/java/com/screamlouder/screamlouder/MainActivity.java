package com.screamlouder.screamlouder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Øyvind Grimstad
 * @author Vegard Brestad
 * @author Erik Mikalsen
 * Date: 11.10-2016
 * Description: A simple app that records and displays the current volume input measured in Decibel.
 */

public class MainActivity extends AppCompatActivity {
// https://developer.android.com/reference/android/media/AudioRecord.Builder.html
// http://stackoverflow.com/questions/8499042/android-audiorecord-example
    //http://stackoverflow.com/questions/31305163/getmaxamplitude-always-returns-0
    //http://stackoverflow.com/questions/21119846/amplitude-from-audiorecord
    //http://stackoverflow.com/questions/15685752/how-to-use-an-android-handler-to-update-a-textview-in-the-ui-thread

    /**
     * Creating classvariabels accesible throughout the class
     */
    TextView dbResult;
    TextView maxDb;
    TextView nameField;

    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "screamLouder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private Handler handler;
    int max = 0;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gathering all the views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbResult = (TextView) findViewById(R.id.dbResult);
        maxDb = (TextView) findViewById(R.id.maxDb);
        dbResult.setText("-");
        maxDb.setText("-");
        nameField = (TextView) findViewById(R.id.nameField);
        handler = new Handler();

        ((Button) findViewById(R.id.btnStop)).setVisibility(View.INVISIBLE);


        Intent i = getIntent();//getting the name from the input from startactivity.class
        nameField.setText(i.getStringExtra("nameInfo"));

        setButtonHandlers();//initializing button "onclick" listeners on Start/stop buttons
        enableButtons(false);
        bufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        //Setting the buffersize of the input from microphone, the channel (stereo/mono), and encodingtype (quality)


    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
    }

    /**
     *
     * @param id Button id eg. R.id.btnStart
     * @param isEnable
     * Switches state of the appropriate button
     */
    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    /**
     *
     * @param isRecording
     * only one of start/stop buttons is clickable at the time. Switches when the state of recording changes
     */
    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    /**
     *
     * @return String of the created filenmae (recording)
     * Getting filename of the created file. If first time running, create the appropriate filedirectory eg "emulated/0/screamLouder"
     * Storing the filename based on the actual time in milliseconds when recording with .wav ext.
     */
    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
    }

    /**
     *
     * @return String of the temporary file that is created during the recording
     * if temporary file already exists, overwrite (delete and create new)
     */
    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);
        if (tempFile.exists())
            tempFile.delete();
        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    /**
     * Returns void.
     * starts recording, while simoltainously updating a textview with the input decibel from the microphone
     */
    private void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);
        int i = recorder.getState();
        if (i == 1)
            recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() { //Runs the recording in its own thread so that it doesnt influence the performance of the app.

            @Override
            public void run() {
                writeAudioDataToFile(); //writes the file (in its own thread as stated above)
            }
        }, "AudioRecorder Thread");//name thread
        recordingThread.start();//initialize thread

        //Animates the buttons opacity at a duration of 300ms on UI thread
        animateView((Button)findViewById(R.id.btnStart),true);
        animateView((Button)findViewById(R.id.btnStop),false);


    }

    /**
     *
     * @param view eg Button view
     * @param in boolean which determines if button is to dissapear/appear
     *
     */
    public void animateView(final View view, boolean in){
        float animIn = 1.0f;
        float animOut = 0.0f;
        if(in){
            view.animate()
                    .alpha(animOut)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.INVISIBLE);
                        }
                    });
        }else{
            view.animate()
                    .alpha(animIn)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
        }

    }//animateView()

    /**
     * returns void
     * Writes the recording to a file, eg "1476179695828.wav"
     */
    private void writeAudioDataToFile() {
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read = 0;
        if (null != os) {
            while (isRecording) {
                read = recorder.read(data, 0, bufferSize);
                //Insert code for getting maxAmplitude and printing to dbResult
                //Continous loop that loops through the continous input from  the microphone
                String maxValue="";
                String input = "";
                int curMax = 0;
                for (short s : data) {
                    curMax = Math.abs(s);
                    if(curMax > max){
                        max = curMax;
                    }
                    input = String.valueOf(curMax);
                }
                maxValue = String.valueOf(max);

                final String finalInput = input;
                final String finalMaxValue = maxValue;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //updates UI with the proper decibel
                        dbResult.setText(finalInput);
                        maxDb.setText(finalMaxValue);
                    }
                });


                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns void
     * stops the recording, reset the recorder and sets the recording thread to null.
     */
    private void stopRecording() {
        if (null != recorder) {
            isRecording = false;

            int i = recorder.getState();
            if (i == 1)
                recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(), getFilename());
        deleteTempFile();
        Toast.makeText(getApplicationContext(), "Recording Saved",Toast.LENGTH_LONG).show();
        animateView((Button)findViewById(R.id.btnStart),false);
        animateView((Button)findViewById(R.id.btnStop),true); //resets the buttons to default, with animation

    }

    /**
     * deletes the temporary file
     */
    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    /**
     *
     * @param inFilename The temporary file
     * @param outFilename The final stored file
     */
    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;
        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            AppLog.logString("File size: " + totalDataLen);
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param out
     * @param totalAudioLen
     * @param totalDataLen
     * @param longSampleRate
     * @param channels
     * @param byteRate
     * @throws IOException
     * http://stackoverflow.com/questions/5760048/audio-playback-headaches
     */
    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    /**
     *
     * @param view switches view to WallOfFame.class
     */
    public void changeView(View view){
        Intent i = new Intent(this,WallOfFame.class);
        startActivity(i);
    }

    /**
     * ClickListener for the start/stop buttons. Switches "active" state at the same time.
     */
    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart: {
                    AppLog.logString("Start Recording");
                    enableButtons(true);

                    startRecording();
                    break;
                }
                case R.id.btnStop: {
                    AppLog.logString("Start Recording");
                    enableButtons(false);
                    stopRecording();
                    break;
                }
            }
        }
    };
}



